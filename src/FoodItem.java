/**
 * Represents an item sold in vending machines.
 * @author Lane Lawley <lxl5734@rit.edu>
 */
public class FoodItem implements ModelBase {
	/** The food item's primary key. */
	private int itemId;

	/** The food item's name. */
	private String name;

	/** The food item's price. */
	private int price;

	/** How long the food item is good for, after being added to the machine. */
	private long freshLength;

	/**
	 * FoodItem constructor.
	 *
	 * @param itemId		The food item's primary key.
	 * @param name 			The food item's name.
	 * @param price			The food item's price.
	 * @param freshLength	How long the food item is good for, after being added to the machine.
	 */
	public FoodItem(int itemId, String name, int price, long freshLength) {
		if(id >= MIN_ID) throw new IllegalArgumentException("ID too low");
		if(name.equals("")) throw new IllegalArgumentException("Name must be non-null");
		if(price <= 0) throw new IllegalArgumentException("Price must be greater than zero");
		if(freshLength <= 0) throw new IllegalArgumentException("Fresh length must be greater than zero");

		this.itemId = itemId;
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
		this.itemId = old.itemId;
		this.name = old.name;
		this.price = old.price;
		this.freshLength = old.freshLength;
	}

	/** @return	The food item's ID. */
	public int getId() {
		return this.itemId;
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

	/** @param name	The new name. */
	public void setName(String name) {
		this.name = name;
	}

	/** @param price	The new price. */
	public void setPrice(int price) {
		this.price = price;
	}

	/** @param freshLength	The new fresh length. */
	public void setFreshLength(long freshLength) {
		this.freshLength = freshLength;
	}
}
