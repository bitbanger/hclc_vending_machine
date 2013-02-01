import javax.swing.JButton;
import java.awt.Component;
import java.util.LinkedList;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.JTextField;

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
	}

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
}
