import javax.swing.JButton;
import java.awt.Component;
import java.util.LinkedList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

/**
 * A button that is enabled iff a all of the conditions in a set are true.
 * @author Matthew Koontz
 **/
public class ConditionButton extends JButton
{
	private LinkedList<ConditionButtonCondition> conditions;
	/**
	 * Creates a ConditionButton with no label.
	 **/
	public ConditionButton()
	{
		conditions = new LinkedList<ConditionButtonCondition>();
		setEnabled(false);
		checkAndSetEnabled();
	}

	/**
	 * Creates a ConditionButton with the given label.
	 * @param label The label to display on the button
	 **/
	public ConditionButton(String label)
	{
		super(label);
		conditions = new LinkedList<ConditionButtonCondition>();
		setEnabled(false);
	}

	/**
	 * Sets enabled to the true iff all of the conditions are true.
	 **/
	public void checkAndSetEnabled()
	{
		boolean enabled = true;
		for (ConditionButtonCondition condition : conditions)
			enabled = enabled && condition.checkCondition();
		setEnabled(enabled);
	}

	/**
	 * Adds the condition to the list of conditions that must be true for
	 * the button the be enabled.
	 * @param condition The condition to add.
	 **/
	public void addCondition(ConditionButtonCondition condition)
	{
		conditions.add(condition);
		checkAndSetEnabled(); //make sure we're immediately enabled if appropriate
	}

	/**
	 * Purges the list of necessary conditions.
	 */
	public void clearConditions()
	{
		conditions.clear();
	}

	/**
	 * 'Watches' a JTextField by checking our condition when the text changes.
	 * @param field The JTextField to watch.
	 **/
	public void watch(JTextField field)
	{
		final ConditionButton me = this;
		field.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void changedUpdate(DocumentEvent e)
			{
			}

			@Override
			public void insertUpdate(DocumentEvent e)
			{
				me.checkAndSetEnabled();
			}

			@Override
			public void removeUpdate(DocumentEvent e)
			{
				me.checkAndSetEnabled();
			}
		});
	}

	/**
	 * 'Watches' a JList by checking our conditions when the selected item
	 * in the list changes.
	 * @param list The JList to watch.
	 **/
	public void watch(JList list)
	{
		final ConditionButton me = this;
		list.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent ignored)
			{
				me.checkAndSetEnabled();
			}
		});
	}

	/**
	 * 'Watches' a VMLayoutPanel by checking out conditions whenever the
	 * selected item may have been changed.
	 * @param panel The VMLayout panel to watch.
	 **/
	public void watch(VMLayoutPanel panel)
	{
		final ConditionButton me = this;
		panel.addVendingMachineItemChangedListener(new VendingMachineItemChangedListener()
		{
			@Override
			public void itemChanged()
			{
				me.checkAndSetEnabled();
			}
		});
	}

	/**
	 * 'Watches' a JCheckBox, requiring it to be selected before continuing.
	 * @param box The box that needs must be selected
	 */
	public void watch(JCheckBox box)
	{
		final ConditionButton me = this;
		box.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ignored)
			{
				me.checkAndSetEnabled();
			}
		});
	}
}
