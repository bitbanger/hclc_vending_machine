import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.Dimension;
import javax.swing.JLabel;

public class ManagerMachineManagementScreenAddMachineGUI extends JPanel implements ActionListener
{
	private ManagerMachineManagementScreen controller;
	private BaseGUI master;
	private ManagerMachineManagementScreenGUI parent;

	private LocationPickerPanel locationPicker;
	private NumberField stockingIntervalField;
	private NumberField rowField;
	private NumberField colField;
	private NumberField depthField;
	private ConditionButton confirmButton;
	private JButton cancelButton;

	public ManagerMachineManagementScreenAddMachineGUI(ManagerMachineManagementScreen controller, BaseGUI master, ManagerMachineManagementScreenGUI parent)
	{
		this.controller = controller;
		this.master = master;
		this.parent = parent;

		locationPicker = new LocationPickerPanel(null);
		stockingIntervalField = new NumberField(NumberField.POSITIVE_Z);
		rowField = new NumberField(NumberField.POSITIVE_Z);
		colField = new NumberField(NumberField.POSITIVE_Z);
		depthField = new NumberField(NumberField.POSITIVE_Z);
		confirmButton = new ConditionButton("Add Machine");
		cancelButton = new JButton("Cancel");

		addComponents();
		addLogic();
	}

	private void addLogic()
	{
		confirmButton.addCondition(new ConditionButtonCondition()
		{
			@Override
			public boolean checkCondition()
			{
				System.out.println("HERE");
				return stockingIntervalField.areContentsValid() && rowField.areContentsValid() && colField.areContentsValid() && depthField.areContentsValid();
			}
		});

		confirmButton.watch(stockingIntervalField);
		confirmButton.watch(rowField);
		confirmButton.watch(colField);
		confirmButton.watch(depthField);

		cancelButton.addActionListener(this);
		confirmButton.addActionListener(this);
	}

	private void addComponents()
	{
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		locationPicker.setAlignmentY(TOP_ALIGNMENT);
		add(locationPicker);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setAlignmentY(TOP_ALIGNMENT);
		add(rightPanel);

		JPanel attributePanel = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints con = new GridBagConstraints();
		attributePanel.setLayout(gridbag);
		rightPanel.add(attributePanel);

		con.gridwidth = 1;
		con.gridheight = 1;
		con.weightx=1;
		con.weighty=1;
		con.fill = GridBagConstraints.HORIZONTAL;

		con.gridx=0;
		con.gridy=0;
		con.weightx=0;
		attributePanel.add(new JLabel("Stocking interval (days):"), con);
		con.weightx=1;
		con.gridx=1;
		stockingIntervalField.setColumns(20);
		stockingIntervalField.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)stockingIntervalField.getPreferredSize().getHeight()));
		attributePanel.add(stockingIntervalField, con);
		
		con.gridx=0;
		con.gridy=1;
		con.weightx=0;
		attributePanel.add(new JLabel("Number of rows:"), con);
		con.weightx=1;
		con.gridx=1;
		rowField.setColumns(20);
		rowField.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)rowField.getPreferredSize().getHeight()));
		attributePanel.add(rowField, con);
		
		con.gridx=0;
		con.gridy=2;
		con.weightx=0;
		attributePanel.add(new JLabel("Number of columns:"), con);
		con.weightx=1;
		con.gridx=1;
		colField.setColumns(20);
		colField.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)colField.getPreferredSize().getHeight()));
		attributePanel.add(colField, con);

		con.gridx=0;
		con.gridy=3;
		con.weightx=0;
		attributePanel.add(new JLabel("Depth of machine:"), con);
		con.weightx=1;
		con.gridx=1;
		depthField.setColumns(20);
		depthField.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)depthField.getPreferredSize().getHeight()));
		attributePanel.add(depthField, con);

		rightPanel.add(Box.createGlue());

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		rightPanel.add(bottomPanel);

		bottomPanel.add(Box.createGlue());

		bottomPanel.add(cancelButton);

		bottomPanel.add(Box.createRigidArea(new Dimension(10, 0)));

		bottomPanel.add(confirmButton);

	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		if (source == cancelButton)
		{
			master.popContentPanel();
		}
	}
}
