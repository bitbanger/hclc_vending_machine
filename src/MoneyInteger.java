/**
 * This class allows you to represent an integer as money string, but to sort it as an integer.
 * We're very sorry.
 *
 * @author Matthew Koontz <mjk3979@rit.edu>
 * @author Lane Lawley <lxl5734@rit.edu>
 */
public class MoneyInteger implements Comparable<MoneyInteger> {
	/** The value of the money integer */
	private int value;
	
	/**
	 * Constructor for the MoneyInteger class
	 *
	 * @param value	The value of the money integer
	 */
	public MoneyInteger(int value) {
		this.value = value;
	}
	
	/** @inheritDoc */
	public String toString() {
		return U.formatMoney(value);
	}
	
	/** @inheritDoc */
	public int compareTo(MoneyInteger other) {
		return ((Integer)value).compareTo(other.value);
	}
}