/**
 * Number format functor specifying a particular minimum acceptable number.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public class MinValueNumberFormat extends NumberFormat
{
	/** An instance allowing any nonnegative number. */
	public static final MinValueNumberFormat ZERO=new MinValueNumberFormat(0);

	/** An instance allowing only positive numbers. */
	public static final MinValueNumberFormat ONE=new MinValueNumberFormat(1);

	/** The lowest number allowed to validate successfully. */
	private final int MINIMUM;

	/**
	 * Constructor.
	 * Sets the minimum value to be used.
	 * @param minimum the lowest number to be accepted
	 */
	public MinValueNumberFormat(int minimum)
	{
		this.MINIMUM=minimum;
	}

	/** @inheritDoc */
	@Override
	public boolean validate(int input)
	{
		return input>=MINIMUM;
	}

	/** @inheritDoc */
	public String toString()
	{
		if(this.MINIMUM==ZERO.MINIMUM)
			return "Please enter only a nonnegative number";
		else if(this.MINIMUM==ONE.MINIMUM)
			return "Please enter only a positive number";
		else
			return "Please enter only a number greater than or equal to "+MINIMUM;
	}

	/** @return the minimum acceptable value */
	public int getMinimum()
	{
		return MINIMUM;
	}
}
