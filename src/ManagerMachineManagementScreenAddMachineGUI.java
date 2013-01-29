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
	private JButton confirmButton;
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
		confirmButton = new JButton("Add Machine");
		cancelButton = new JButton("Cancel");

		addComponents();
	}

	private void addComponents()
	{
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(locationPicker);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		add(rightPanel);

		JPanel attributePanel = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints con = new GridBagConstraints();
		attributePanel.setLayout(gridbag);
		rightPanel.add(attributePanel);

		con.gridwidth = 1;
		con.gridheight = 1;
		
		con.gridx = GridBagConstraints.RELATIVE;
		attributePanel.add(new JLabel("Stocking interval (days):"), con);
		con.gridx = GridBagConstraints.REMAINDER;
		attributePanel.add(stockingIntervalField);
		
		con.gridx = GridBagConstraints.RELATIVE;
		attributePanel.add(new JLabel("Number of rows:"), con);
		con.gridx = GridBagConstraints.REMAINDER;
		attributePanel.add(rowField);
		
		con.gridx = GridBagConstraints.RELATIVE;
		attributePanel.add(new JLabel("Number of columns:"), con);
		con.gridx = GridBagConstraints.REMAINDER;
		attributePanel.add(colField);

		con.gridx = GridBagConstraints.RELATIVE;
		attributePanel.add(new JLabel("Depth of machine:"), con);
		con.gridx = GridBagConstraints.REMAINDER;
		attributePanel.add(depthField);

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

	}
}
