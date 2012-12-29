/**
 * Represents a user of the system.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public abstract class User implements ModelBase
{
	/** The minimum allowable primary key. */
	public static final int MIN_ID=-1;

	/** The person's primary key. */
	private int userId;

	/**
	 * Fresh constructor.
	 * Creates an instance with the specified primary key.
	 * This method is intended to be invoked only by classes that know a good value for this <tt>id</tt>
	 * @param id the instance's primary key
	 * @throws IllegalArgumentException if the <tt>id</tt> is invalid
	 */
	public User(int id) throws IllegalArgumentException
	{
		if(id<MIN_ID)
			throw new IllegalArgumentException("ID too low");
		
		userId=id;
	}

	/**
	 * Copy constructor.
	 * Creates a copy of the supplied instance.
	 * @param existing the instance to clone
	 */
	public User(User existing)
	{
		this.userId=existing.userId;
	}

	/**
	 * @return the primary key
	 */
	public int getId()
	{
		return userId;
	}
}
