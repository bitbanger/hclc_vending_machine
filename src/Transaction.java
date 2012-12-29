import java.util.GregorianCalendar;

/**
 * Represents an event during which a customer bought an item from a vending machine.
 * @author Lane Lawley <lxl5734@rit.edu>
 */
public class Transaction implements ModelBase {
	/** The transaction's primary key. */
	private int transactionId;

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

	/**
	 * Transaction constructor.
	 *
	 * @param transactionId	The transaction's primary key.
	 * @param timestamp		The time the transaction occurred.
	 * @param machine		The machine at which the transaction occurred.
	 * @param customer		The customer who purchased the product.
	 * @param product		The product purchased.
	 * @param whichRow		The row the product was purchased from.
	 */
	public Transaction(int transactionId, GregorianCalendar timestamp, VendingMachine machine, Customer customer, FoodItem product, Pair<Integer, Integer> whichRow) {
		if(transactionId < MIN_ID) throw new IllegalArgumentException("ID too low");
		if(whichRow.first < 0 || whichRow.second < 0) throw new IllegalArgumentException("Invalid row position");

		this.transactionId = transactionId;
		this.timestamp = timestamp;
		this.machine = machine;
		this.customer = customer;
		this.product = product;
		this.whichRow = whichRow;
	}

	/**
	 * Shallow copy constructor.
	 *
	 * @param old	Transaction to copy.
	 */
	public Transaction(Transaction old) {
		this.transactionId = old.transactionId;
		this.timestamp = old.timestamp;
		this.machine = old.machine;
		this.customer = old.customer;
		this.product = old.product;
		this.whichRow = old.whichRow;
	}

	/** @return	The transaction's primary key. */
	public int getId() {
		return transactionId;
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

	/** @param timestamp	The new timestamp. */
	public void setTimestamp(GregorianCalendar timestamp) {
		this.timestamp = timestamp;
	}

	/** @param machine	The new machine. */
	public void setMachine(VendingMachine machine) {
		this.machine = machine;
	}

	/** @param customer	The new customer. */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/** @param product	The new product. */
	public void setProduct(FoodItem product) {
		this.product = product;
	}

	/** @param whichRow	The new row. */
	public void setRow(Pair<Integer, Integer> whichRow) {
		this.whichRow = whichRow;
	}
}
