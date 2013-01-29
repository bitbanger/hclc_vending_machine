import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.Box;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import java.util.HashMap;
import java.util.ArrayList;
/**
 * Content panel for the restocker task list.
 * @author Piper Chester
 **/
public class RestockerTaskListScreenGUI extends JPanel
{
	/**
	 * Controller instance for this screen.
	 **/
	private RestockerTaskListScreen controller;
	
	/**
	 * Master for this panel
	 */
	private BaseGUI master;
	

	/**
	 * Array of tasks to perform.
	 */
	private HashMap<Integer, Pair<String, Boolean>> tasks;

	/**
	 * Creates the panel with the given controller instance.
	 * @param controller The controller instance to use.
	 * @param master the master BaseGUI to use
	 **/
	public RestockerTaskListScreenGUI(RestockerTaskListScreen controller, BaseGUI master)
	{
		this.controller = controller;
		this.master = master;
		tasks = controller.getInstructions();
		addComponents();
	}

	/**
	 * Lays out the components on the panel.
	 **/
	public void addComponents()
	{
		// Components will be laid out vertically in the main panel
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Align the components to the left side
		this.setAlignmentX(LEFT_ALIGNMENT);

		// Panel to hold id text box and login button
		JPanel loginPanel = new JPanel();
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
		loginPanel.setAlignmentX(LEFT_ALIGNMENT);

		// Panel to hold id label and text box
		JPanel idPanel = new JPanel();
		idPanel.setLayout(new BoxLayout(idPanel, BoxLayout.X_AXIS));


		// Gap between label and text box
		idPanel.add(Box.createRigidArea(new Dimension(10, 0)));


		// Add box id label and text box to login panel
		idPanel.setMaximumSize(idPanel.getPreferredSize());
		loginPanel.add(idPanel);

		// Gap between customer id text box and login button
		loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Panel to align the login button to the right
		JPanel loginButtonPanel = new JPanel();
		loginButtonPanel.setLayout(new BoxLayout(loginButtonPanel, BoxLayout.X_AXIS));

		// Aligns the button to the right
		loginButtonPanel.add(Box.createGlue());

		// Add the login panel to main panel
		loginPanel.setMaximumSize(loginPanel.getPreferredSize());
		this.add(loginPanel);

		// Gap between above and pay with cash button
		this.add(Box.createRigidArea(new Dimension(0,20)));

		// Cancel button
		JButton	cancelButton = new JButton("Cancel");
		//cancelButton.addActionListener(this);
		this.add(cancelButton);

		// Gap between above and cancel button
		this.add(Box.createRigidArea(new Dimension(50,0)));

		// Done button
		JButton doneButton = new JButton("Done");
		//doneButton.addActionListener(this);
		this.add(doneButton);

		JPanel toDoPanel = new JPanel();
		toDoPanel.setLayout(new BoxLayout(toDoPanel, BoxLayout.Y_AXIS));
		toDoPanel.setAlignmentX(RIGHT_ALIGNMENT);

		//tasks=controller.getInstructions();
		ArrayList<String> insts = new ArrayList<String>();
		for ( Integer next : tasks.keySet() ) {
			Pair<String, Boolean> inst = tasks.get(next);
			if ( inst.second )
				insts.add(next+": "+inst.first+"\tREQUIRED");
			else
				insts.add(next+": "+inst.first);
		}
		
		//JLabel toDoLabel = new JLabel(insts.toArray());
		//toDoPanel.add(toDoLabel);
	}
}
