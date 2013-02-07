import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.util.Vector;
import java.util.Arrays;

/**
 * Allows the user to select a location.
 * @author Matthew Koontz
 **/
public class LocationPickerPanel extends JPanel implements ActionListener
{
	/**
	 * The location that is being edited.
	 **/
	private Location location;

	/**
	 * The field for the zip code.
	 **/
	private NumberField zipCodeField;

	/**
	 * The field for the state.
	 **/
	private JTextField stateField;

	/**
	 * The list of nearby businesses.
	 **/
	private JList businessList;

	/**
	 * The field to change the name of a nearby business.
	 **/
	private JTextField businessField;

	/**
	 * The button that adds a new business.
	 **/
	private JButton addBusinessButton;

	/**
	 * The button that changes the name of a nearby business.
	 **/
	private ConditionButton editBusinessButton;

	/**
	 * Button that removes a nearby business. KaPow
	 **/
	private ConditionButton removeBusinessButton;

	/**
	 * The list of nearby businesses.
	 **/
	private Vector<String> businesses;

	/**
	 * Creates a new location picker to edit the given location.
	 * @param location The location to edit.
	 **/
	public LocationPickerPanel(Location location)
	{
		this.location = location;

		zipCodeField = new NumberField(NumberField.NONNEGATIVE_Z);
		stateField = new JTextField();
		businessList = new JList();
		businessField = new JTextField();
		addBusinessButton = new JButton("Add Business");
		editBusinessButton = new ConditionButton("Edit Business");
		removeBusinessButton = new ConditionButton("Remove Business");
		if (location != null)
			businesses = new Vector<String>(Arrays.asList(location.getNearbyBusinesses()));
		else
			businesses = new Vector<String>();

		addComponents();
		addLogic();
		fillData();
	}

	/**
	 * Fills to form with the initial data.
	 **/
	private void fillData()
	{
		if (location != null)
		{
			zipCodeField.setText(location.getZipCode()+"");
			stateField.setText(location.getState());
		}
		refreshData();
	}

	/**
	 * Refreshes the data in the list.
	 **/
	private void refreshData()
	{
		businessList.setListData(businesses);
		businessList.clearSelection();
		businessField.setText("");
		businessField.setEnabled(false);
	}

	/**
	 * Lays out the form.
	 **/
	private void addComponents()
	{
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		JPanel businessPanel = new JPanel();
		businessPanel.setAlignmentY(TOP_ALIGNMENT);
		businessPanel.setLayout(new BoxLayout(businessPanel, BoxLayout.Y_AXIS));
		add(businessPanel);

		JScrollPane scrollPane = new JScrollPane(businessList);
		businessPanel.add(scrollPane);

		businessPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		businessField.setColumns(30);
		businessField.setMaximumSize(businessField.getPreferredSize());
		businessPanel.add(businessField);

		scrollPane.setMaximumSize(new Dimension((int)businessField.getPreferredSize().getWidth(), Integer.MAX_VALUE));

		JPanel businessButtonPanel = new JPanel();
		businessButtonPanel.setLayout(new BoxLayout(businessButtonPanel, BoxLayout.X_AXIS));
		businessPanel.add(businessButtonPanel);

		businessButtonPanel.add(addBusinessButton);
		businessButtonPanel.add(Box.createGlue());

		removeBusinessButton.setEnabled(false);
		businessButtonPanel.add(removeBusinessButton);
		businessButtonPanel.add(Box.createGlue());

		editBusinessButton.setEnabled(false);
		businessButtonPanel.add(editBusinessButton);

		add(Box.createRigidArea(new Dimension(20, 0)));

		JPanel zipAndStatePanel = new JPanel();
		zipAndStatePanel.setAlignmentY(TOP_ALIGNMENT);
		zipAndStatePanel.setLayout(new BoxLayout(zipAndStatePanel, BoxLayout.Y_AXIS));
		add(zipAndStatePanel);

		JPanel zipPanel = new JPanel();
		zipPanel.setLayout(new BoxLayout(zipPanel, BoxLayout.X_AXIS));
		zipAndStatePanel.add(zipPanel);

		zipPanel.add(new JLabel("Zip Code:"));
		zipCodeField.setColumns(10);
		zipCodeField.setMaximumSize(zipCodeField.getPreferredSize());
		zipPanel.add(zipCodeField);

		zipAndStatePanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JPanel statePanel = new JPanel();
		statePanel.setLayout(new BoxLayout(statePanel, BoxLayout.X_AXIS));
		zipAndStatePanel.add(statePanel);
	
		statePanel.add(new JLabel("State:"));
		stateField.setColumns(30);
		stateField.setMaximumSize(stateField.getPreferredSize());
		statePanel.add(stateField);
	}

	/**
	 * Adds the logic to the view.
	 **/
	private void addLogic()
	{
		addBusinessButton.addActionListener(this);
		removeBusinessButton.addActionListener(this);
		editBusinessButton.addActionListener(this);

		ConditionButtonCondition itemSelected = new ConditionButtonCondition()
		{
			@Override
			public boolean checkCondition()
			{
				return businessList.getSelectedValue() != null;
			}
		};

		editBusinessButton.addCondition(itemSelected);
		removeBusinessButton.addCondition(itemSelected);

		businessList.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent _)
			{
				editBusinessButton.checkAndSetEnabled();
				removeBusinessButton.checkAndSetEnabled();
				businessField.setText((String)businessList.getSelectedValue());
				businessField.setEnabled(true);
			}
		});

	}

	/**
	 * @return The zip code that the user entered.
	 **/
	public int getZipCode()
	{
		return Integer.parseInt(zipCodeField.getText());
	}
	
	/**
	 * @return The state that the user entered.
	 **/
	public String getState()
	{
		return stateField.getText();
	}

	/**
	 * @return An array of the names of the nearby businesses.
	 **/
	public String[] getNearbyBusinesses()
	{
		return businesses.toArray(new String[0]);
	}

	/**
	 * Handles the buttons getting clicked.
	 * @param event Contains information regarding the event.
	 **/
	@Override
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		if (source == addBusinessButton)
		{
			businesses.add("New Business");
			refreshData();
		}
		else if (source == editBusinessButton)
		{
			businesses.set(businessList.getSelectedIndex(), businessField.getText());
			refreshData();
		}
		else if (source == removeBusinessButton)
		{
			businesses.remove(businessList.getSelectedIndex());
			refreshData();
		}
	}
}
