/**
 * Number format functor specifying a particular minimum acceptable number.
 * Implementations are used to customize integral input validation.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public abstract class NumberFormat
{
	/** The means of communicating validation problems outward. */
	private static ValidationComplainer communicator=null;

	/**
	 * This should be called once to point all format functors at the correct error relayer.
	 * @param communicator the place to send a report of the validation problem
	 * @return whether the operation was permitted (replaced a <tt>null</tt> with a non-<tt>null</tt>)
	 */
	public static boolean setCommunicator(ValidationComplainer communicator)
	{
		if(NumberFormat.communicator==null && communicator!=null) //never before set and good value
		{
			NumberFormat.communicator=communicator;
			return false;
		}
		else
			return true;
	}

	/**
	 * Checks whether the specified number classifies as valid input.
	 * @param input the guess to validate
	 * @return whether it is considered valid
	 */
	public abstract boolean validate(int input);

	/**
	 * Prints the validation error associated with a bad value.
	 * @return a message to be printed on bad input
	 */
	public abstract String toString();

	/**
	 * Checks the input and prints any validation error that occurs.
	 * @param input the number to be validated
	 * @return the result of the validation
	 */
	public final boolean checkLoudly(int input)
	{
		return checkLoudly(input, false);
	}

	/**
	 * Checks the input and maybe prints the output.
	 * Validates a number and prints the validation error only on failure.
	 * @param input the number to be validated
	 * @param cont whether to skip the check and return false
	 * @return the result of the validation
	 */
	public final boolean checkLoudly(int input, boolean cont)
	{
		if(cont)
			return false;
		
		boolean valid=validate(input);
		
		if(!valid)
			communicator.alert(String.valueOf(this));
		
		return valid;
	}
}
