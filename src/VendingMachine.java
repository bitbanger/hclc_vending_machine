/**
 * Represents a vending machine.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public class VendingMachine implements ModelBase
{
	/** The machine's primary key. */
	private int machineId;

	/** Whether the machine is currently active. */
	private boolean active;

	/** The machine's physical location. */
	private Location location;

	/** The machine's current product layout. */
	private VMLayout currentLayout;

	/** The machine's queued product layout, which will be instated upon restocking. */
	private VMLayout nextLayout;

	/**
	 * Fresh constructor.
	 * Creates an instance with the specified <tt>id</tt>, <tt>location</tt>, and layout.
	 * Initially, the machine is considered to be active, but lacks any next layout.
	 * This method is intended to be invoked only by classes that know a good value for this <tt>id</tt>
	 * @param id the instance's primary key
	 * @param location the <tt>VendingMachine</tt>'s abode
	 * @param currentLayout the <tt>VendingMachine</tt>'s layout
	 * @throws IllegalArgumentException if the <tt>id</tt> is invalid or <tt>location</tt> or <tt>currentLayout</tt> is <tt>null</tt>
	 */
	public VendingMachine(int id, Location location, VMLayout currentLayout) throws IllegalArgumentException
	{
		if(id<MIN_ID)
			throw new IllegalArgumentException("ID too low");
		else if(location==null)
			throw new IllegalArgumentException("Location cannot be null");
		else if(currentLayout==null)
			throw new IllegalArgumentException("Current layout cannot be null");
		
		machineId=id;
		active=true;
		this.location=location;
		this.currentLayout=currentLayout;
		nextLayout=null;
	}

	public VendingMachine(int id, Location location, VMLayout currentLayout, VMLayout nextLayout, boolean active)
	{
		this(id, location, currentLayout);
		this.active=active;
		this.nextLayout = nextLayout;
	}

	/**
	 * Copy constructor.
	 * Creates a copy of the supplied instance.
	 * @param existing the instance to clone
	 */
	public VendingMachine(VendingMachine existing)
	{
		this.machineId=existing.machineId;
		this.active=existing.active;
		this.location=existing.location;
		this.currentLayout=existing.currentLayout;
		this.nextLayout=existing.nextLayout;
	}

	/**
	 * @return the primary key
	 */
	public int getId()
	{
		return machineId;
	}

	/**
	 * @param location a replacement location
	 * @throws IllegalArgumentException if a <tt>null</tt> value is supplied
	 */
	public void setLocation(Location location) throws IllegalArgumentException
	{
		if(location==null)
			throw new IllegalArgumentException("Location cannot be null");
		
		this.location=location;
	}

	/**
	 * @return the location
	 */
	public Location getLocation()
	{
		return location;
	}

	/**
	 * @return the current layout
	 */
	public VMLayout getCurrentLayout()
	{
		return currentLayout;
	}

	/**
	 * @param nextLayout a replacement future layout
	 */
	public void setNextLayout(VMLayout nextLayout)
	{
		this.nextLayout=nextLayout;
	}

	/**
	 * @return the next layout
	 */
	public VMLayout getNextLayout()
	{
		return nextLayout;
	}

	/**
	 * Swaps the next layout into the current layout.
	 * At the end of this process, there is no next layout left.
	 * In order for this to work, there needs to be a next layout available.
	 * @return whether the operation succeeded (i.e. a next layout existed)
	 */
	public boolean swapInNextLayout()
	{
		if(nextLayout!=null)
		{
			currentLayout=nextLayout;
			nextLayout=null;
			
			return true;
		}
		else
			return false;
	}
}
