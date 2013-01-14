/**
 * Represents a user of the system.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public abstract class User extends ModelBase
{
	/** The person's name. */
	private String name;

	/**
	 * Fresh constructor.
	 * Creates an instance with the specified <tt>name</tt>.
	 * @param name the <tt>User</tt>'s name
	 * @throws BadArgumentException if the supplied <tt>name</tt> is <tt>null</tt>
	 */
	public User(String name) throws BadArgumentException
	{
		if(name==null)
			throw new BadArgumentException("Name cannot be null");
		
		this.name=name;
	}

	/**
	 * Copy constructor.
	 * Creates a copy of the supplied instance.
	 * @param existing the instance to clone
	 */
	public User(User existing)
	{
		super(existing);
		this.name=existing.name;
	}

	/**
	 * @param name a replacement name
	 * @throws BadArgumentException if given a <tt>null</tt> value
	 */
	public void setName(String name) throws BadArgumentException
	{
		if(name==null)
			throw new BadArgumentException("Name cannot be null");
		
		this.name=name;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Checks whether two instances contain the same data.
	 * @param another another instance
	 * @return whether their contents match
	 */
	@Override
	public boolean equals(Object another)
	{
		if(!(another instanceof User))
			return false;
		User other=(User)another;
		
		return super.equals(another) && name.equals(other.name);
	}

	@Override
	public String toString() {
		return super.toString() + " " + name;
	}
}
