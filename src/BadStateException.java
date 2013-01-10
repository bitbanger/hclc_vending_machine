/**
 * Indicates that your request is bad and you should feel bad.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public class BadStateException extends Exception
{
	/**
	 * Create an exception to signify we're in a messy place.
	 * @param mess the clarification message
	 */
	public BadStateException(String mess)
	{
		super(mess);
	}
}
