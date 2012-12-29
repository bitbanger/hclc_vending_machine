/**
 * Represents a vending machine's location.
 * @author Lane Lawley <lxl5734@rit.edu>
 */
public class Location implements ModelBase {
	/** The location's primary key. */
	private int locationId;

	/** The location's five-digit ZIP code. */
	private int zipCode;

	/** The location's state (of all the US states). */
	private String state;

	/** An array of the location's nearby businesses. */
	private String[] nearbyBusinesses;

	/**
	 * Location constructor.
	 *
	 * @param locationId		The location's primary key.
	 * @param zipCode			The location's five-digit ZIP code.
	 * @param state				The location's state (of all the US states).
	 * @param nearbyBusinesses	An array of the location's nearby businesses.
	 */
	public Location(int locationId, int zipCode, String state, String[] nearbyBusinesses) {
		if(locationId < MIN_ID) throw new IllegalArgumentException("ID too low");
		if(zipCode < 10000) throw new IllegalArgumentException("ZIP code must be at least five digits");
		if(state.equals("")) throw new IllegalArgumentException("State must be non-null");

		this.locationId = locationId;
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
		this.locationId = old.locationId;
		this.zipCode = old.zipCode;
		this.state = old.state;
		this.nearbyBusinesses = old.nearbyBusinesses;
	}

	/** @return	The location's primary key. */
	public int getId() {
		return locationId;
	}

	/** @return	The location's five-digit ZIP code. */
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

	/** @param zipCode	The new ZIP code. */
	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	/** @param state	The new state. */
	public void setState(String state) {
		this.state = state;
	}

	/** @param nearbyBusinesses	The new array of nearby businesses. */
	public void setNearbyBusinesses(String[] nearbyBusinesses) {
		this.nearbyBusinesses = nearbyBusinesses;
	}
}
