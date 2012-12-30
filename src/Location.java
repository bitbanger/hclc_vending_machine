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
	 * @throws IllegalArgumentException if <tt>zipCode</tt> is negative or a <tt>null</tt> value is given
	 */
	public Location(int zipCode, String state, String[] nearbyBusinesses) throws IllegalArgumentException
	{
		if(zipCode < 0) throw new IllegalArgumentException("ZIP code must not be negative");
		else if(state==null) throw new IllegalArgumentException("State must be non-null");
		else if(nearbyBusinesses==null) throw new IllegalArgumentException("Nearby businesses must be non-null");
		
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
	 * Be aware that the <tt>retriveFormattedZipCode()</tt> method may be your best friend.
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
	 * @throws IllegalArgumentException if a negative value is provided
	 */
	public void setZipCode(int zipCode) throws IllegalArgumentException
	{
		if(zipCode<0)
			throw new IllegalArgumentException("ZIP code must not be negative");
		
		this.zipCode = zipCode;
	}

	/**
	 * @param state	The new state.
	 * @throws IllegalArgumentException if a <tt>null</tt> value is provided
	 */
	public void setState(String state) throws IllegalArgumentException
	{
		if(state==null)
			throw new IllegalArgumentException("State must not be null");
		
		this.state = state;
	}

	/**
	 * @param nearbyBusinesses	The new array of nearby businesses.
	 * @throws IllegalArgumentException if a <tt>null</tt> value is provided
	 */
	public void setNearbyBusinesses(String[] nearbyBusinesses) throws IllegalArgumentException
	{
		if(nearbyBusinesses==null)
			throw new IllegalArgumentException("Nearby businesses must not be null");
		
		this.nearbyBusinesses = nearbyBusinesses;
	}

	/**
	 * Retrieves the ZIP code, formatted to 5 digits as one might expect.
	 * @return the formatted code
	 */
	public String retriveFormattedZipCode()
	{
		return String.format("%05d", zipCode);
	}
}
