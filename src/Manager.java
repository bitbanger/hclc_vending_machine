/**
 * Represents a manager.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public class Manager extends User
{
	/** The person's password. */
	private String password;

	/**
	 * Fresh constructor.
	 * Creates an instance with the specified initial password.
	 * @param name the <tt>Manager</tt>'s name
	 * @param password the <tt>Manager</tt>'s initial password
	 * @throws BadArgumentException if the <tt>name</tt> is invalid or the <tt>password</tt> is <tt>null</tt>
	 */
	public Manager(String name, String password) throws BadArgumentException
	{
		super(name);
		
		if(password==null)
			throw new BadArgumentException("Password must not be null");
		
		this.password=password;
	}

	/**
	 * Copy constructor.
	 * Creates a copy of the supplied instance.
	 * @param existing the instance to clone
	 */
	public Manager(Manager existing)
	{
		super(existing);
		this.password=existing.password;
	}

	/**
	 * @param password the new password
	 * @throws BadArgumentException if a <tt>null</tt> value is supplied
	 */
	public void setPassword(String password) throws BadArgumentException
	{
		if(password==null)
			throw new BadArgumentException("Password must not be null");
		
		this.password=password;
	}

	/**
	 * Checks whether the supplied password is valid in combination with this <tt>User</tt> ID.
	 * @param attempt the user's password guess
	 * @return whether the authentication attempt was successful
	 */
	public boolean comparePassword(String attempt)
	{
		return password.equals(attempt);
	}

	/**
	 * @return this manager's password
	 **/
	public String getPassword()
	{
		return password;
	}

	/**
	 * Checks whether two instances contain the same data.
	 * @param another another instance
	 * @return whether their contents match
	 */
	@Override
	public boolean equals(Object another)
	{
		if(!(another instanceof Manager))
			return false;
		Manager other=(Manager)another;
		
		return super.equals(another) && password.equals(other.password);
	}
}
