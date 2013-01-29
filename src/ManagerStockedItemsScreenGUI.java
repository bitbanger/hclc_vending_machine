import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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

	/** Control for en/disabling items. */
	private JCheckBox enableBox;

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
		enableBox=new JCheckBox("Enabled for sale");
		enableBox.setSelected(true);
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
		controls.add(enableBox);
		controls.add(updateSelected);
		controls.add(addNew);
		add(controls, BorderLayout.WEST);
		
		//a way to get back to Kansas:
		JButton takeMeHome=new JButton("Back to home screen");
		add(takeMeHome, BorderLayout.SOUTH);
		takeMeHome.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ignored)
			{
				master.popContentPanel();
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
		products.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent ignored)
			{
				if(!products.isSelectionEmpty())
				{
					FoodItem chosen=(FoodItem)products.getSelectedValue();
					
					nameField.setText(chosen.getName());
					priceField.setText(String.valueOf((double)chosen.getPrice()/100));
					freshnessField.setText(String.valueOf(chosen.getFreshLength()));
					enableBox.setSelected(chosen.isActive());
				}
			}
		});
		
		//specify when the buttons should be available:
		updateSelected.addCondition(new ConditionButtonCondition()
		{
			public boolean checkCondition()
			{
				return !products.isSelectionEmpty() && priceField.areContentsValid() && freshnessField.areContentsValid();
			}
		});
		addNew.addCondition(new ConditionButtonCondition()
		{
			public boolean checkCondition()
			{
				return priceField.areContentsValid() && freshnessField.areContentsValid();
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
			boolean enablement=enableBox.isSelected();
			
			if(e.getSource().equals(updateSelected))
			{
				FoodItem chosen=(FoodItem)products.getSelectedValue();
				controller.changeItemName(chosen, name);
				controller.changeItemPrice(chosen, price);
				controller.changeItemFreshLength(chosen, freshness);
				controller.changeItemStatus(chosen, enablement);
			}
			else //addNew button hit
				controller.addItem(name, price, freshness, enablement);
			
			products.setListData(controller.listItems().toArray());
			products.clearSelection();
			nameField.setText("");
			priceField.clearMoneyEntered();
			freshnessField.clearNumberEntered();
			enableBox.setSelected(true);
		}
	}
}
