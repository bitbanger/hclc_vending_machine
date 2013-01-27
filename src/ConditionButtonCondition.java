/**
 * A condition for the ConditionButton class.
 * @author Matthew Koontz
 **/
public interface ConditionButtonCondition
{
	/**
	 * Checks if the button should be enabled based on a boolean expression.
	 * @return True if the button should be enabled.
	 **/
	public boolean checkCondition();
}
