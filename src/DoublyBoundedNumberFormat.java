/**
 * Number format functor specifying an integer between two bounds.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public class DoublyBoundedNumberFormat extends MinValueNumberFormat
{
	/** The exclusive upper bound of the input. */
	private final int UPPER;

	/**
	 * Constructor.
	 * Sets the boundaries.
	 * @param lower the lower boundary (inclusive)
	 * @param upper the upper boundary (exclusive)
	 */
	public DoublyBoundedNumberFormat(int lower, int upper)
	{
		super(lower);
		this.UPPER=upper;
	}

	/** @inheritDoc */
	@Override
	public boolean validate(int input)
	{
		return super.validate(input) && input<UPPER;
	}

	/** @inheritDoc */
	@Override
	public String toString()
	{
		return "Please enter an integer between "+getMinimum()+" and "+(UPPER-1);
	}
}
