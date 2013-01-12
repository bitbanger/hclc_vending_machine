/**
 * Indicates that your argument is bad and you should feel bad.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public class BadArgumentException extends Exception
{
	/**
	 * Create an exception to signify we're in a messy place.
	 * @param mess the clarification message
	 */
	public BadArgumentException(String mess)
	{
		super(mess);
	}
}
