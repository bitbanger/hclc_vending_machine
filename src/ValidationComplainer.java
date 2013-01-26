/**
 * Provides a generic way to communicate that input hasn't passed validation.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public interface ValidationComplainer
{
	/**
	 * Outputs a problem report to the user.
	 * @param message a description of what seems to be the issue
	 */
	public void alert(String message);
}
