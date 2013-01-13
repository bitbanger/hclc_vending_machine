import java.util.GregorianCalendar;

/**
 * Represents a row of products in a vending machine.
 * Each such row contains a certain number of the same product.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public class Row extends ModelBase
{
	/** The product stored in the row. */
	private FoodItem product;

	/** The number of the product remaining. */
	private int remainingQuantity;

	/** The expiration date of all products in the row. */
	private GregorianCalendar expirationDate;

	/**
	 * Fresh constructor.
	 * Creates an instance with the specified values.
	 * @param product the product carrried by the <tt>Row</tt>
	 * @param quantity the number of items stocked in the <tt>Row</tt>
	 * @param sellBy the expiration date of the items in the <tt>Row</tt>
	 * @throws BadArgumentException if the <tt>quantity</tt> is invalid or something else is <tt>null</tt>
	 */
	public Row(FoodItem product, int quantity, GregorianCalendar sellBy) throws BadArgumentException
	{
		if(product==null)
			throw new BadArgumentException("Product cannot be null");
		else if(quantity<0)
			throw new BadArgumentException("Quantity cannot be negative");
		else if(sellBy==null)
			throw new BadArgumentException("Expiration date cannot be null");
		
		this.product=product;
		remainingQuantity=quantity;
		expirationDate=sellBy;
	}

	/**
	 * (Completely shallow) copy constructor.
	 * Creates a copy of the supplied instance.
	 * @param existing the instance to clone
	 */
	public Row(Row existing)
	{
		this(existing, false);
	}

	/**
	 * (Optionally mixed-depth) copy constructor.
	 * If asked to make a deep copy, the two instances' IDs and expiration dates&mdash;but <i>not</i> their products&mdash;will become independent and/or be recursively copied to avoid changes' clashing.
	 * From the consequential discrepancy between the primary keys, we see that deep-copied daughter instances are never <tt>equal</tt> to their mothers.
	 * @param existing the instance to clone
	 * @param deepShallows whether to do the partial-decoupling
	 */
	public Row(Row existing, boolean deepShallows)
	{
		super();
		
		if(deepShallows)
			this.expirationDate=(GregorianCalendar)existing.expirationDate.clone();
		else
		{
			try
			{
				setId(existing.getId());
			}
			catch(Exception impossible) {} //there can be no ID there!
			this.expirationDate=existing.expirationDate;
		}
			
		this.product=existing.product;
		this.remainingQuantity=existing.remainingQuantity;
	}

	/**
	 * @param product a replacement product
	 * @throws BadArgumentException if a <tt>null</tt> value is supplied
	 */
	public void setProduct(FoodItem product) throws BadArgumentException
	{
		if(product==null)
			throw new BadArgumentException("Product cannot be null");
		
		this.product=product;
	}

	/**
	 * @return the product
	 */
	public FoodItem getProduct()
	{
		return product;
	}

	/**
	 * Note that <tt>decrementRemainingQuantity()</tt> is more appropriate for fulfilling purchases.
	 * @param remainingQuantity a new (positive) quantity
	 * @throws BadArgumentException if a negative value is supplied
	 */
	public void setRemainingQuantity(int remainingQuantity) throws BadArgumentException
	{
		if(remainingQuantity<0)
			throw new BadArgumentException("Quantity cannot be negative");
		
		this.remainingQuantity=remainingQuantity;
	}

	/**
	 * @return the remaining quantity
	 */
	public int getRemainingQuantity()
	{
		return remainingQuantity;
	}

	/**
	 * @param expirationDate a replacement expiration date
	 * @throws BadArgumentException if a <tt>null</tt> value is supplied
	 */
	public void setExpirationDate(GregorianCalendar expirationDate) throws BadArgumentException
	{
		if(expirationDate==null)
			throw new BadArgumentException("Expiration date cannot be null");
		
		this.expirationDate=expirationDate;
	}

	/**
	 * @return the expiration date
	 */
	public GregorianCalendar getExpirationDate()
	{
		return expirationDate;
	}

	/**
	 * Diminishes the remaining quantity by one.
	 * This is provided as a convenience and safety facility to be used when making sales.
	 * @return whether the operation succeeded (i.e. there was an item to remove)
	 */
	public boolean decrementRemainingQuantity()
	{
		if(remainingQuantity>0)
		{
			--remainingQuantity;
			
			return true;
		}
		else
			return false;
	}

	/**
	 * Checks whether two instances contain the same data.
	 * Note: this is <tt>false</tt> for daughters of deep-copy operations!
	 * @param another another instance
	 * @return whether their contents match
	 */
	@Override
	public boolean equals(Object another)
	{
		if(!(another instanceof Row))
			return false;
		Row other=(Row)another;
		
		return super.equals(another) && product.equals(other.product) && this.remainingQuantity==other.remainingQuantity && expirationDate.equals(other.expirationDate);
	}
}
