/**
 * The base class for data representations.
 * It keeps track of and manages access to each type's primary keys.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public abstract class ModelBase
{
	/** The minimum allowable primary key. */
	public static final int MIN_ID=0;

	/** The sentinel indicating a temporary ID value. */
	private static final int TEMP_ID=-1;

	/** The instance's primary key. */
	private int id;

	/**
	 * Default constructor.
	 * Does not assign a primary key, instead allowing it to be changed once in the future.
	 */
	public ModelBase()
	{
		id=TEMP_ID;
	}

	/**
	 * Copy constructor.
	 * Makes a duplicate of the supplied instance.
	 * @param existing the instance to copy
	 */
	public ModelBase(ModelBase existing)
	{
		this.id=existing.id;
	}

	/**
	 * This method may only be invoked once in order to assign a permanent primary key.
	 * @param id the permanent primary key
	 * @throws BadStateException if the instance has already been assigned a primary key
	 * @throws BadArgumentException if an invalid ID is supplied
	 */
	public void setId(int id) throws BadStateException, BadArgumentException
	{
		if(this.id!=TEMP_ID)
			throw new BadStateException("Cannot replace existing ID");
		else if(id<MIN_ID)
			throw new BadArgumentException("Invalid ID supplied");
		
		this.id=id;
	}

	/**
	 * This method may only be used to obtain the primary key if one has actually been set.
	 * @return the primary key
	 * @throws BadStateException if the instance has never been assigned a primary key
	 */
	public int getId() throws BadStateException
	{
		if(id==TEMP_ID)
			throw new BadStateException("No ID has been assigned");
		
		return id;
	}

	/**
	 * Determines whether no permanent ID has been manually assigned.
	 * @return whether this is a new object, unbacked by the database
	 */
	public boolean isTempId()
	{
		return id==TEMP_ID;
	}

	/**
	 * Checks whether two instances' IDs are the same.
	 * @return whether the primary keys match
	 */
	@Override
	public boolean equals(Object another)
	{
		return another instanceof ModelBase && this.id==((ModelBase)another).id;
	}
}
