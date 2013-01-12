import java.util.GregorianCalendar;

/**
 * Represents an event during which a customer bought an item from a vending machine.
 * @author Lane Lawley <lxl5734@rit.edu>
 */
public class Transaction extends ModelBase {
	/** The time the transaction occurred. */
	private GregorianCalendar timestamp;

	/** The machine at which the transaction occurred. */
	private VendingMachine machine;

	/** The customer who purchased the product. */
	private Customer customer;

	/** The product purchased. */
	private FoodItem product;

	/** The row the product was purchased from. */
	private Pair<Integer, Integer> whichRow;

	/** The amount of money that changed hands. */
	private int balance;

	/**
	 * Preferred constructor.
	 *
	 * @param timestamp		The time the transaction occurred.
	 * @param machine		The machine at which the transaction occurred.
	 * @param customer		The customer who purchased the product.
	 * @param product		The product purchased.
	 * @param whichRow		The row the product was purchased from.
	 * @throws BadArgumentException if a <tt>null</tt> is passed in or a coordinate is negative
	 * @throws NullPointerException if <tt>product</tt> happens to be negative ... avoid!
	 */
	public Transaction(GregorianCalendar timestamp, VendingMachine machine, Customer customer, FoodItem product, Pair<Integer, Integer> whichRow) throws BadArgumentException
	{
		this(timestamp, machine, customer, product, whichRow, product.getPrice());
	}
	
	/**
	 * Strongly discouraged constructor; please avoid using.
	 *
	 * @param timestamp		The time the transaction occurred.
	 * @param machine		The machine at which the transaction occurred.
	 * @param customer		The customer who purchased the product.
	 * @param product		The product purchased.
	 * @param whichRow		The row the product was purchased from.
	 * @param balance		The amount of money that changed hands.
	 * @throws BadArgumentException if a <tt>null</tt> is passed in or a coordinate is negative
	 */
	public Transaction(GregorianCalendar timestamp, VendingMachine machine, Customer customer, FoodItem product, Pair<Integer, Integer> whichRow, int balance) throws BadArgumentException
	{
		if(timestamp==null)
			throw new BadArgumentException("Timestamp cannot be null");
		else if(machine==null)
			throw new BadArgumentException("Machine cannot be null");
		else if(customer==null)
			throw new BadArgumentException("Customer cannot be null");
		else if(whichRow==null)
			throw new BadArgumentException("Row specification cannot be null");
		else if(whichRow.first==null || whichRow.second==null)
			throw new BadArgumentException("Row specification cannot contain null coordinate");
		else if(whichRow.first<0 || whichRow.second<0)
			throw new BadArgumentException("Row specification cannot contain negative coordinate");
		
		this.timestamp = timestamp;
		this.machine = machine;
		this.customer = customer;
		this.product = product;
		this.whichRow = whichRow;
		this.balance = balance;
	}

	/**
	 * Shallow copy constructor.
	 *
	 * @param old	Transaction to copy.
	 */
	public Transaction(Transaction old) {
		super(old);
		this.timestamp = old.timestamp;
		this.machine = old.machine;
		this.customer = old.customer;
		this.product = old.product;
		this.whichRow = old.whichRow;
		this.balance = old.balance;
	}

	/** @return	The time the transaction occurred. */
	public GregorianCalendar getTimestamp() {
		return timestamp;
	}

	/** @return	The machine at which the transaction occurred. */
	public VendingMachine getMachine() {
		return machine;
	}

	/** @return The customer who purchased the product.*/
	public Customer getCustomer() {
		return customer;
	}

	/** @return The product purchased. */
	public FoodItem getProduct() {
		return product;
	}

	/** @return The row the product was purchased from. */
	public Pair<Integer, Integer> getRow() {
		return whichRow;
	}

	/**
	 * Danger, Will Robinson!  Use this instead of asking the <tt>Product</tt> for its <tt>getPrice()</tt>.
	 * Otherwise, you may get an inaccurate result.
	 * @return The amount of money that changed hands.
	 */
	public int getBalance()
	{
		return balance;
	}

	/**
	 * @param timestamp	The new timestamp.
	 * @throws BadArgumentException if a <tt>null</tt> value is provided
	 */
	public void setTimestamp(GregorianCalendar timestamp) throws BadArgumentException
	{
		if(timestamp==null)
			throw new BadArgumentException("Timestamp must not be null");
		
		this.timestamp = timestamp;
	}

	/**
	 * @param machine	The new machine.
	 * @throws BadArgumentException if a <tt>null</tt> value is provided
	 */
	public void setMachine(VendingMachine machine) throws BadArgumentException
	{
		if(machine==null)
			throw new BadArgumentException("Machine must not be null");
		
		this.machine = machine;
	}

	/**
	 * @param customer	The new customer.
	 * @throws BadArgumentException if a <tt>null</tt> value is provided
	 */
	public void setCustomer(Customer customer) throws BadArgumentException
	{
		if(customer==null)
			throw new BadArgumentException("Customer must not be null");
		
		this.customer = customer;
	}

	/**
	 * @param product	The new product.
	 * @throws BadArgumentException if a <tt>null</tt> value is provided
	 */
	public void setProduct(FoodItem product) throws BadArgumentException
	{
		if(product==null)
			throw new BadArgumentException("Product must not be null");
		
		this.product = product;
	}

	/**
	 * @param whichRow	The new row.
	 * @throws BadArgumentException if a <tt>null</tt> or negative value is provided
	 */
	public void setRow(Pair<Integer, Integer> whichRow) throws BadArgumentException
	{
		if(whichRow==null)
			throw new BadArgumentException("Row specification must not be null");
		else if(whichRow.first==null || whichRow.second==null)
			throw new BadArgumentException("Row specification mustn't contain null coordinate");
		else if(whichRow.first<0 || whichRow.second<0)
			throw new BadArgumentException("Row specification mustn't contain negative coordinate");
		
		this.whichRow = whichRow;
	}

	/**
	 * Checks whether two instances contain the same data.
	 * @param another another instance
	 * @return whether their contents match
	 */
	@Override
	public boolean equals(Object another)
	{
		if(!(another instanceof Transaction))
			return false;
		Transaction other=(Transaction)another;
		
		return super.equals(another) && timestamp.equals(other.timestamp) && machine.equals(other.machine) && customer.equals(other.customer) && product.equals(other.product) && whichRow.equals(other.whichRow) && this.balance==other.balance;
	}
}
