import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.Dimension;

/**
 * Content panel for the manger's alter layout screen.
 * @author Piper Chester
 **/
public class ManagerAlterLayoutScreenGUI extends JPanel implements ActionListener
{
	/**
	 * The controller for this screen.
	 **/
	private ManagerAlterLayoutScreen controller;

	/**
	 * The frame this panel is in.
	 **/
	private BaseGUI master;

	/**
	 * The button that allows the manager to change row.
	 **/
	private JButton changeRowButton;

	/**
	 * The button that allows the manager to empty row.
	 **/
	private JButton emptyRowButton;

	/**
	 * The button that allows the manager to commit changes.
	 **/
	private JButton commitChangesButton;

	/**
	 * The button that allows the manager to log out.
	 **/
	private JButton logoutButton;

	/**
	 * Creates a new manager home screen panel with the given controller and
	 * BaseGUI.
	 * @param controller The controller for this home screen.
	 * @param master The BaseGUI this panel will be inside.
	 **/
	public ManagerAlterLayoutScreenGUI(ManagerAlterLayoutScreen controller, BaseGUI master)
	{
		this.controller = controller;
		this.master = master;

		master.getStatusBar().setStatus("Let's alter some layouts", StatusBar.STATUS_GOOD_COLOR);

		changeRowButton = new JButton("Change Row");
		emptyRowButton = new JButton("Empty Row");
		commitChangesButton = new JButton("Commit Changes");
		logoutButton = new JButton("Return to home screen");

		addComponents();
	}

	/**
	 * Lays out the components on the panel.
	 **/
	private void addComponents()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		
		changeRowButton.addActionListener(this);
		changeRowButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		add(changeRowButton);

		add(Box.createRigidArea(new Dimension(0, 20)));

		emptyRowButton.addActionListener(this);
		emptyRowButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		add(emptyRowButton);

		add(Box.createRigidArea(new Dimension(0, 20)));

		commitChangesButton.addActionListener(this);
		commitChangesButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		add(commitChangesButton);

		add(Box.createRigidArea(new Dimension(0, 20)));

		logoutButton.addActionListener(this);
		logoutButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		add(logoutButton);
	}

	/**
	 * Handles any of the buttons being clicked.
	 * @param event Contains information regarding the event.
	 **/
	@Override
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		if (source == changeRowButton)
		{
			// Do stuff
		}
		else if (source == emptyRowButton)
		{
		}
		else if (source == commitChangesButton)
		{
			// Do stuff
		}
		else if (source == logoutButton)
		{
			master.popContentPanel();
		}
	}
}
