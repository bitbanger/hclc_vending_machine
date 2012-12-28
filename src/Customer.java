/**
 * Represents a customer.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public class Customer extends User
{
	/** The ID reserved for cash customers. */
	private static final int CASH_ID=-1;

	/** The person's account balance. */
	private int money;

	/**
	 * Default (cash customer) constructor.
	 * Creates a special <i>cash</i> customer with the ability to change its balance on the fly.
	 * The balance starts out at <tt>0</tt>, however, and remains there until funds are added.
	 */
	public Customer()
	{
		super(CASH_ID);
		money=0;
	}

	/**
	 * Fresh (account-backed customer) constructor.
	 * Creates an instance with the specified primary key.
	 * This method is intended to be invoked only by classes that know a good value for this <tt>id</tt>
	 * @param id the instance's primary key
	 * @param money the <tt>Customer</tt>'s initial balance
	 * @throws IllegalArgumentException if the <tt>id</tt> is invalid
	 */
	public Customer(int id, int money) throws IllegalArgumentException
	{
		super(id);
		
		if(id<ModelBase.MIN_ID)
			throw new IllegalArgumentException("ID too low");
		else if(money<0)
			throw new IllegalArgumentException("Money must not be negative");
		
		this.money=money;
	}

	/**
	 * Copy constructor.
	 * Creates a copy of the supplied instance.
	 * @param existing the instance to clone
	 */
	public Customer(Customer existing)
	{
		super(existing);
		this.money=existing.money;
	}

	/**
	 * Note that <tt>deductMoney(int)</tt> is more appropriate for fulfilling purchases.
	 * @param money the new balance
	 * @throws IllegalArgumentException if a negative value is supplied
	 */
	public void setMoney(int money)
	{
		if(money<0)
			throw new IllegalArgumentException("Money must not be negative");
		
		this.money=money;
	}

	/**
	 * @return the account balance
	 */
	public int getMoney()
	{
		return money;
	}

	/**
	 * Deducts the specified amount from the account.
	 * This amount may be positive, negative, or zero, but must not cause the balance to go negative.
	 * @param change the value by which to diminish the balance
	 * @return whether the operation succeeded (i.e. didn't exceed the available funds)
	 */
	public boolean deductMoney(int change)
	{
		if(change<=money) //balance wouldn't go negative
		{
			money-=change;
			
			return true;
		}
		else
			return false;
	}

	/**
	 * Indicates whether this customer is a cash customer.
	 * @return the answer to The Question
	 */
	public boolean isCashCustomer()
	{
		return getId()==CASH_ID;
	}
}
