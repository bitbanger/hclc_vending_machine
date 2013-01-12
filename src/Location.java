import java.util.Arrays;

/**
 * Represents a vending machine's location.
 * @author Lane Lawley <lxl5734@rit.edu>
 */
public class Location extends ModelBase {
	/** The location's five-digit ZIP code. */
	private int zipCode;

	/** The location's state (of all the US states). */
	private String state;

	/** An array of the location's nearby businesses. */
	private String[] nearbyBusinesses;

	/**
	 * Location constructor.
	 *
	 * @param zipCode			The location's five-digit ZIP code.
	 * @param state				The location's state (of all the US states).
	 * @param nearbyBusinesses	An array of the location's nearby businesses.
	 * @throws BadArgumentException if <tt>zipCode</tt> is negative or a <tt>null</tt> value is given
	 */
	public Location(int zipCode, String state, String[] nearbyBusinesses) throws BadArgumentException
	{
		if(zipCode < 0) throw new BadArgumentException("ZIP code must not be negative");
		else if(state==null) throw new BadArgumentException("State must be non-null");
		else if(nearbyBusinesses==null) throw new BadArgumentException("Nearby businesses must be non-null");
		
		this.zipCode = zipCode;
		this.state = state;
		this.nearbyBusinesses = nearbyBusinesses;
	}

	/**
	 * Shallow copy constructor.
	 *
	 * @param old	Location to copy.
	 */
	public Location(Location old) {
		super(old);
		this.zipCode = old.zipCode;
		this.state = old.state;
		this.nearbyBusinesses = old.nearbyBusinesses;
	}

	/**
	 * Be aware that the <tt>retrieveFormattedZipCode()</tt> method may be your best friend.
	 * @return	The location's five-digit ZIP code.
	 */
	public int getZipCode() {
		return zipCode;
	}

	/** @return	The location's state (of all the US states). */
	public String getState() {
		return state;
	}

	/** @return	An array of the location's nearby businesses. */
	public String[] getNearbyBusinesses() {
		return nearbyBusinesses;
	}

	/**
	 * @param zipCode	The new ZIP code.
	 * @throws BadArgumentException if a negative value is provided
	 */
	public void setZipCode(int zipCode) throws BadArgumentException
	{
		if(zipCode<0)
			throw new BadArgumentException("ZIP code must not be negative");
		
		this.zipCode = zipCode;
	}

	/**
	 * @param state	The new state.
	 * @throws BadArgumentException if a <tt>null</tt> value is provided
	 */
	public void setState(String state) throws BadArgumentException
	{
		if(state==null)
			throw new BadArgumentException("State must not be null");
		
		this.state = state;
	}

	/**
	 * @param nearbyBusinesses	The new array of nearby businesses.
	 * @throws BadArgumentException if a <tt>null</tt> value is provided
	 */
	public void setNearbyBusinesses(String[] nearbyBusinesses) throws BadArgumentException
	{
		if(nearbyBusinesses==null)
			throw new BadArgumentException("Nearby businesses must not be null");
		
		this.nearbyBusinesses = nearbyBusinesses;
	}

	/**
	 * Retrieves the ZIP code, formatted to 5 digits as one might expect.
	 * @return the formatted code
	 */
	public String retrieveFormattedZipCode()
	{
		return String.format("%05d", zipCode);
	}

	/**
	 * Checks whether two instances contain the same data.
	 * @param another another instance
	 * @return whether their contents match
	 */
	@Override
	public boolean equals(Object another)
	{
		if(!(another instanceof Location))
			return false;
		Location other=(Location)another;
		
		return super.equals(another) && this.zipCode==other.zipCode && state.equals(other.state) && Arrays.equals(this.nearbyBusinesses, other.nearbyBusinesses);
	}
}
