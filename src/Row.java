import java.util.GregorianCalendar;

/**
 * Represents a row of products in a vending machine.
 * Each such row contains a certain number of the same product.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public class Row implements ModelBase
{
	/** The row's primary key. */
	private int rowId;

	/** The product stored in the row. */
	private FoodItem product;

	/** The number of the product remaining. */
	private int remainingQuantity;

	/** The expiration date of all products in the row. */
	private GregorianCalendar expirationDate;

	/**
	 * Fresh constructor.
	 * Creates an instance with the specified values.
	 * This method is intended to be invoked only by classes that know a good value for this <tt>id</tt>
	 * @param id the instance's primary key
	 * @param product the product carrried by the <tt>Row</tt>
	 * @param quantity the number of items stocked in the <tt>Row</tt>
	 * @param sellBy the expiration date of the items in the <tt>Row</tt>
	 * @throws IllegalArgumentException if the <tt>id</tt> or <tt>quantity</tt> is invalid or something else is <tt>null</tt>
	 */
	public Row(int id, FoodItem product, int quantity, GregorianCalendar sellBy) throws IllegalArgumentException
	{
		if(id<MIN_ID)
			throw new IllegalArgumentException("ID too low");
		else if(product==null)
			throw new IllegalArgumentException("Product cannot be null");
		else if(quantity<0)
			throw new IllegalArgumentException("Quantity cannot be negative");
		else if(sellBy==null)
			throw new IllegalArgumentException("Expiration date cannot be null");
		
		rowId=id;
		this.product=product;
		remainingQuantity=quantity;
		expirationDate=sellBy;
	}

	/**
	 * Copy constructor.
	 * Creates a copy of the supplied instance.
	 * @param existing the instance to clone
	 */
	public Row(Row existing)
	{
		this.rowId=existing.rowId;
		this.product=existing.product;
		this.remainingQuantity=existing.remainingQuantity;
		this.expirationDate=existing.expirationDate;
	}

	/**
	 * @return the primary key
	 */
	public int getId()
	{
		return rowId;
	}

	/**
	 * @param product a replacement product
	 * @throws IllegalArgumentException if a <tt>null</tt> value is supplied
	 */
	public void setProduct(FoodItem product) throws IllegalArgumentException
	{
		if(product==null)
			throw new IllegalArgumentException("Product cannot be null");
		
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
	 * @throws IllegalArgumentException if a negative value is supplied
	 */
	public void setRemainingQuantity(int remainingQuantity) throws IllegalArgumentException
	{
		if(remainingQuantity<0)
			throw new IllegalArgumentException("Quantity cannot be negative");
		
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
	 * @throws IllegalArgumentException if a <tt>null</tt> value is supplied
	 */
	public void setExpirationDate(GregorianCalendar expirationDate) throws IllegalArgumentException
	{
		if(expirationDate==null)
			throw new IllegalArgumentException("Expiration date cannot be null");
		
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
}
