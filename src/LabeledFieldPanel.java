import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Insets;

/**
 * A panel that is used to easily layout text fields and their labels.
 * @author Matthew Koontz
 **/
public class LabeledFieldPanel extends JPanel
{
	/**
	 * Row to place the next text field on
	 **/
	private int row;

	/**
	 * Constraints that are used when placing components
	 **/
	private GridBagConstraints con;

	/**
	 * Constructs a new panel for laying out text fields and their labels.
	 **/
	public LabeledFieldPanel()
	{
		row = 0;
		con = new GridBagConstraints();
		addComponents();
	}

	/**
	 * Sets the layout and some constant fields in the constraints
	 **/
	private void addComponents()
	{
		setLayout(new GridBagLayout());
		con.gridwidth = 1;
		con.fill = GridBagConstraints.HORIZONTAL;
	}

	/**
	 * Adds the given text field to the panel with the given label.
	 * @param label The label to place next to the text field.
	 * @param field The field to add to the panel.
	 **/
	public void addLabeledTextField(String label, JTextField field)
	{
		// Add the label
		con.gridx=0;
		con.gridy=row;
		con.weightx=0;
		con.insets = new Insets(0, 0, 20, 10);
		add(new JLabel(label), con);

		// Add the field
		con.weightx=1;
		con.gridx=1;
		con.insets = new Insets(0, 0, 20, 0);
		add(field, con);

		// Increment the row for the next field
		++row;

		// Adjust the size
		setMaximumSize(getPreferredSize());

		// Redraw
		revalidate();
	}
}
