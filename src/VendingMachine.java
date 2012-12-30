/**
 * Represents a vending machine.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public class VendingMachine extends ModelBase
{
	/** Whether the machine is currently active. */
	private boolean active;

	/** The machine's physical location. */
	private Location location;

	/** The machine's current product layout. */
	private VMLayout currentLayout;

	/** The machine's queued product layout, which will be instated upon restocking. */
	private VMLayout nextLayout;

	/**
	 * Simple constructor.
	 * Creates an instance with the specified <tt>location</tt> and layout.
	 * Initially, the machine is considered to be active, but lacks any next layout.
	 * @param location the <tt>VendingMachine</tt>'s abode
	 * @param currentLayout the <tt>VendingMachine</tt>'s layout
	 * @throws IllegalArgumentException if <tt>location</tt> or <tt>currentLayout</tt> is <tt>null</tt>
	 */
	public VendingMachine(Location location, VMLayout currentLayout) throws IllegalArgumentException
	{
		if(location==null)
			throw new IllegalArgumentException("Location cannot be null");
		else if(currentLayout==null)
			throw new IllegalArgumentException("Current layout cannot be null");
		
		active=true;
		this.location=location;
		this.currentLayout=currentLayout;
		nextLayout=null;
	}

	/**
	 * Thorough constructor.
	 * Creates an instance with the specified <tt>location</tt> and layout.
	 * Both the future layout and the machine's activity are also supplied.
	 * @param location the <tt>VendingMachine</tt>'s abode
	 * @param currentLayout the <tt>VendingMachine</tt>'s present layout
	 * @param nextLayout the <tt>VendingMachine</tt>'s future layout
	 * @param active whether the <tt>VendingMachine</tt> is currently activated
	 * @throws IllegalArgumentException if anything is <tt>null</tt>
	 */
	public VendingMachine(Location location, VMLayout currentLayout, VMLayout nextLayout, boolean active)
	{
		this(location, currentLayout);
		
		if(nextLayout==null)
			throw new IllegalArgumentException("Next layout cannot be null");
		
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
		super(existing);
		this.active=existing.active;
		this.location=existing.location;
		this.currentLayout=existing.currentLayout;
		this.nextLayout=existing.nextLayout;
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
