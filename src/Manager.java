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
	 * Creates an instance of the specified primary key and initial password.
	 * This method is intended to be invoked only by classes that know a good value for this <tt>id</tt>
	 * @param id the instance's primary key
	 * @param password the <tt>Manager</tt>'s initial password
	 * @throws IllegalArgumentException if the <tt>id</tt> is invalid or the <tt>password</tt> is null
	 */
	public Manager(int id, String password) throws IllegalArgumentException
	{
		super(id);
		
		if(id<ModelBase.MIN_ID)
			throw new IllegalArgumentException("ID too low");
		else if(password==null)
			throw new IllegalArgumentException("Password must not be null");
		
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
		this.password=password;
	}

	/**
	 * @param password the new password
	 * @throws IllegalArgumentException if a <tt>null</tt> value is supplied
	 */
	public void setPassword(String password)
	{
		if(password==null)
			throw new IllegalArgumentException("Password must not be null");
		
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
}
