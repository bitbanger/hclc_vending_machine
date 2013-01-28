import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Content panel for managing the items carried in machines.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public class ManagerStockedItemsScreenGUI extends JPanel
{
	/** Controller behind the curtains. */
	private ManagerStockedItemsScreen controller;

	/** Master frame on the outside. */
	private BaseGUI master;

	/** List of items in our stocking database. */
	private JList products;

	/** Field for product name. */
	private JTextField nameField;

	/** Field for item price. */
	private MoneyField priceField;

	/** Field for shelf length. */
	private NumberField freshnessField;

	/** Button for updating the selected item. */
	private ConditionButton updateSelected;

	/** Button for adding a new item. */
	private ConditionButton addNew;

	/**
	 * Constructor.
	 * @param controller the controller running the actual logic show
	 * @param master the frame surrounding this panel
	 */
	public ManagerStockedItemsScreenGUI(ManagerStockedItemsScreen controller, BaseGUI master)
	{
		this.controller=controller;
		this.master=master;
		
		nameField=new JTextField();
		priceField=new MoneyField(master.getStatusBar());
		freshnessField=new NumberField(NumberField.POSITIVE_Z);
		updateSelected=new ConditionButton("Update selected item");
		addNew=new ConditionButton("Add new item");
		products=new JList(controller.listItems().toArray());
		products.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		addComponents();
	}

	/**
	 * Puts everything together so the user can see it.
	 */
	private void addComponents()
	{
		//overall layout and item selecter:
		setLayout(new BorderLayout());
		add(new JScrollPane(products), BorderLayout.CENTER);
		
		//panel of left controls:
		JPanel controls=new JPanel();
		controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
		controls.add(new JLabel("Item name"));
		controls.add(nameField);
		controls.add(new JLabel("Item price"));
		controls.add(priceField);
		controls.add(new JLabel("Item fresh length"));
		controls.add(freshnessField);
		controls.add(updateSelected);
		controls.add(addNew);
		add(controls, BorderLayout.WEST);
		
		//a way to get back to Kansas:
		JButton takeMeHome=new JButton("Back to home screen");
		final BaseGUI deBase=master;
		add(takeMeHome, BorderLayout.SOUTH);
		takeMeHome.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ignored)
			{
				deBase.popContentPanel();
			}
		});
		
		//teach our components how to behave:
		FieldUpdated changed=new FieldUpdated();
		PerformModification complete=new PerformModification();
		nameField.getDocument().addDocumentListener(changed);
		priceField.getDocument().addDocumentListener(changed);
		freshnessField.getDocument().addDocumentListener(changed);
		updateSelected.addActionListener(complete);
		addNew.addActionListener(complete);
		
		//handle selections made via the list:
		final JList finallyProducts=products;
		final JTextField finallyName=nameField;
		final MoneyField finallyPrice=priceField;
		final NumberField finallyFresh=freshnessField;
		products.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent ignored)
			{
				if(!finallyProducts.isSelectionEmpty())
				{
					FoodItem chosen=(FoodItem)finallyProducts.getSelectedValue();
					
					finallyName.setText(chosen.getName());
					finallyPrice.setText(String.valueOf((double)chosen.getPrice()/100));
					finallyFresh.setText(String.valueOf(chosen.getFreshLength()));
				}
			}
		});
		
		//specify when the buttons should be available:
		updateSelected.addCondition(new ConditionButtonCondition()
		{
			public boolean checkCondition()
			{
				return !finallyProducts.isSelectionEmpty() && finallyPrice.areContentsValid() && finallyFresh.areContentsValid();
			}
		});
		addNew.addCondition(new ConditionButtonCondition()
		{
			public boolean checkCondition()
			{
				return finallyPrice.areContentsValid() && finallyFresh.areContentsValid();
			}
		});
	}

	/**
	 * Announces that one of the fields has been updated.
	 */
	private class FieldUpdated implements DocumentListener
	{
		/** @inheritDoc */
		public void insertUpdate(DocumentEvent ignored)
		{
			updateSelected.checkAndSetEnabled();
			addNew.checkAndSetEnabled();
		}

		/** @inheritDoc */
		public void removeUpdate(DocumentEvent ignored)
		{
			insertUpdate(ignored);
		}

		/** @inheritDoc */
		public void changedUpdate(DocumentEvent ignored) {}
	}
	
	/**
	 * Handles the pressing of one of the modification buttons.
	 */
	private class PerformModification implements ActionListener
	{
		/** @inheritDoc */
		public void actionPerformed(ActionEvent e)
		{
			String name=nameField.getText();
			int price=priceField.getMonetaryAmount();
			int freshness=Integer.parseInt(freshnessField.getText());
			
			if(e.getSource().equals(updateSelected))
			{
				FoodItem chosen=(FoodItem)products.getSelectedValue();
				controller.changeItemName(chosen, name);
				controller.changeItemPrice(chosen, price);
				controller.changeItemFreshLength(chosen, freshness);
			}
			else //addNew button hit
				controller.addItem(name, price, freshness);
			
			products.setListData(controller.listItems().toArray());
			products.clearSelection();
			nameField.setText("");
			priceField.clearMoneyEntered();
			freshnessField.setText("");
		}
	}
}
