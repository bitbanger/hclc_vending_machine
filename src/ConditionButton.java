import javax.swing.JButton;
import java.awt.Component;

/**
 * A button that is enabled iff a condition is true.
 **/
public abstract class ConditionButton extends JButton
{
	/**
	 * Creates a ConditionButton with no label.
	 **/
	public ConditionButton()
	{
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
		setEnabled(false);
	}

	/**
	 * Sets enabled to the current result of checkCondition()
	 **/
	public void checkAndSetEnabled()
	{
		setEnabled(checkCondition());
	}

	/**
	 * Checks if the button should be enabled based on a boolean expression.
	 * @return True iff the button should be enabled
	 **/
	public abstract boolean checkCondition();
}
