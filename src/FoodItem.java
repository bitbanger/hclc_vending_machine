/**
 * Represents an item sold in vending machines.
 * @author Lane Lawley <lxl5734@rit.edu>
 */
public class FoodItem extends ModelBase {
	/** The food item's name. */
	private String name;

	/** The food item's price. */
	private int price;

	/** How long the food item is good for, after being added to the machine. */
	private long freshLength;

	/** Whether the food item is actually being sold. */
	private boolean active;

	/**
	 * Simpler constructor.
	 * Assumes the item is active.
	 *
	 * @param name 			The food item's name.
	 * @param price			The food item's price.
	 * @param freshLength	How long the food item is good for, after being added to the machine.
	 * @throws BadArgumentException if <tt>name</tt> is <tt>null</tt>, <tt>price</tt> is negative, or <tt>freshLength</tt> is nonpositive
	 */
	public FoodItem(String name, int price, long freshLength) throws BadArgumentException
	{
		this(name, price, freshLength, true);
	}

	/**
	 * Full constructor.
	 *
	 * @param name 			The food item's name.
	 * @param price			The food item's price.
	 * @param freshLength	How long the food item is good for, after being added to the machine.
	 * @param active		Whether the item is acutally available for sale.
	 * @throws BadArgumentException if <tt>name</tt> is <tt>null</tt>, <tt>price</tt> is negative, or <tt>freshLength</tt> is nonpositive
	 */
	public FoodItem(String name, int price, long freshLength, boolean active) throws BadArgumentException
	{
		if(name==null) throw new BadArgumentException("Name must be non-null");
		else if(price < 0) throw new BadArgumentException("Price must be nonnegative");
		else if(freshLength <= 0) throw new BadArgumentException("Fresh length must be greater than zero");
		
		this.name = name;
		this.price = price;
		this.freshLength = freshLength;
		this.active = active;
	}

	/**
	 * Shallow copy constructor.
	 *
	 * @param old	FoodItem to copy.
	 */
	public FoodItem(FoodItem old) {
		super(old);
		this.name = old.name;
		this.price = old.price;
		this.freshLength = old.freshLength;
		this.active = old.active;
	}

	/** @return	The food item's name. */
	public String getName() {
		return this.name;
	}

	/** @return	The food item's price. */
	public int getPrice() {
		return this.price;
	}

	/** @return	The food item's fresh length. */
	public long getFreshLength() {
		return this.freshLength;
	}

	/** @return Whether it is active. */
	public boolean isActive()
	{
		return active;
	}

	/**
	 * @param name	The new name.
	 * @throws BadArgumentException if a <tt>null</tt> value is supplied
	 */
	public void setName(String name) throws BadArgumentException
	{
		if(name==null)
			throw new BadArgumentException("Name must not be null");
		
		this.name = name;
	}

	/**
	 * @param price	The new price.
	 * @throws BadArgumentException if price is negative
	 */
	public void setPrice(int price) throws BadArgumentException
	{
		if(price<0)
			throw new BadArgumentException("Price must not be negative");
		
		this.price = price;
	}

	/**
	 * @param freshLength	The new fresh length.
	 * @throws BadArgumentException if the duration isn't positive
	 */
	public void setFreshLength(long freshLength) throws BadArgumentException
	{
		if(freshLength<=0)
			throw new BadArgumentException("Fresh length must be positive");
		
		this.freshLength = freshLength;
	}

	/**
	 * @param active Whether it will be active.
	 */
	public void makeActive(boolean active)
	{
		this.active=active;
	}

	/**
	 * Checks whether two instances contain the same data.
	 * @param another another instance
	 * @return whether their contents match
	 */
	@Override
	public boolean equals(Object another)
	{
		if(!(another instanceof FoodItem))
			return false;
		FoodItem other=(FoodItem)another;
		
		return super.equals(another) && name.equals(other.name) && this.price==other.price && this.freshLength==other.freshLength && this.active==other.active;
	}
}
