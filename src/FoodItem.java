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

	/**
	 * FoodItem constructor.
	 *
	 * @param name 			The food item's name.
	 * @param price			The food item's price.
	 * @param freshLength	How long the food item is good for, after being added to the machine.
	 * @throws IllegalArgumentException if <tt>name</tt> is <tt>null</tt>, <tt>price</tt> is negative, or <tt>freshLength</tt> is nonpositive
	 */
	public FoodItem(String name, int price, long freshLength) throws IllegalArgumentException
	{
		if(name==null) throw new IllegalArgumentException("Name must be non-null");
		else if(price < 0) throw new IllegalArgumentException("Price must be nonnegative");
		else if(freshLength <= 0) throw new IllegalArgumentException("Fresh length must be greater than zero");
		
		this.name = name;
		this.price = price;
		this.freshLength = freshLength;
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

	/**
	 * @param name	The new name.
	 * @throws IllegalArgumentException if a <tt>null</tt> value is supplied
	 */
	public void setName(String name) throws IllegalArgumentException
	{
		if(name==null)
			throw new IllegalArgumentException("Name must not be null");
		
		this.name = name;
	}

	/**
	 * @param price	The new price.
	 * @throws IllegalArgumentException if a <tt>null</tt> value is supplied
	 */
	public void setPrice(int price) throws IllegalArgumentException
	{
		if(name==null)
			throw new IllegalArgumentException("Name must not be null");
		
		this.price = price;
	}

	/**
	 * @param freshLength	The new fresh length.
	 * @throws IllegalArgumentException if a <tt>null</tt> value is supplied
	 */
	public void setFreshLength(long freshLength) throws IllegalArgumentException
	{
		if(name==null)
			throw new IllegalArgumentException("Name must not be null");
		
		this.freshLength = freshLength;
	}
}
