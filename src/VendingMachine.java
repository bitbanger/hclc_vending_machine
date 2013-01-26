import java.util.GregorianCalendar;

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

	/** How often the machine is restocked, in days. */
	private int stockingInterval;

	/** The machine's current product layout. */
	private VMLayout currentLayout;

	/** The machine's queued product layout, which will be instated upon restocking. */
	private VMLayout nextLayout;

	/**
	 * Simple constructor.
	 * Creates an instance with the specified <tt>location</tt> and layout.
	 * Initially, the machine is considered to be active, but lacks any next layout.
	 * @param location the <tt>VendingMachine</tt>'s abode
	 * @param stockingInterval how many days between consecutive restockings
	 * @param currentLayout the <tt>VendingMachine</tt>'s layout
	 * @throws BadArgumentException if <tt>location</tt> or <tt>currentLayout</tt> is <tt>null</tt>, or if <tt>stockingInterval</tt> is not positive
	 */
	public VendingMachine(Location location, int stockingInterval, VMLayout currentLayout) throws BadArgumentException
	{
		this(location, stockingInterval, currentLayout, new VMLayout(currentLayout, true), true);
	}

	/**
	 * Thorough constructor.
	 * Creates an instance with the specified <tt>location</tt> and layout.
	 * Both the future layout and the machine's activity are also supplied.
	 * @param location the <tt>VendingMachine</tt>'s abode
	 * @param stockingInterval how many days between consecutive restockings
	 * @param currentLayout the <tt>VendingMachine</tt>'s present layout
	 * @param nextLayout the <tt>VendingMachine</tt>'s future layout
	 * @param active whether the <tt>VendingMachine</tt> is currently activated
	 * @throws BadArgumentException if an instance is <tt>null</tt> or <tt>stockingInterval</tt> is not positive
	 */
	public VendingMachine(Location location, int stockingInterval, VMLayout currentLayout, VMLayout nextLayout, boolean active) throws BadArgumentException
	{
		if(location==null)
			throw new BadArgumentException("Location cannot be null");
		else if(stockingInterval<=0)
			throw new BadArgumentException("Stocking interval must be positive");
		else if(currentLayout==null)
			throw new BadArgumentException("Current layout cannot be null");
		if(nextLayout==null)
			throw new BadArgumentException("Next layout cannot be null");

		if (currentLayout.getNextVisit() == null)
			currentLayout.setNextVisit(lastPossibleVisit(stockingInterval));
		
		this.location=location;
		this.stockingInterval=stockingInterval;
		this.currentLayout=currentLayout;
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
		this.stockingInterval=existing.stockingInterval;
		this.currentLayout=existing.currentLayout;
		this.nextLayout=existing.nextLayout;
	}

	/**
	 * @param active the new activation status
	 */
	public void makeActive(boolean active)
	{
		this.active=active;
	}

	/**
	 * @return whether the machine is active
	 */
	public boolean isActive()
	{
		return active;
	}

	/**
	 * @param location a replacement location
	 * @throws BadArgumentException if a <tt>null</tt> value is supplied
	 */
	public void setLocation(Location location) throws BadArgumentException
	{
		if(location==null)
			throw new BadArgumentException("Location cannot be null");
		
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
	 * In addition to changing the stocking interval, this method conditionally updates the next restocking visit.
	 * In the case where the newly-mandated visit is sooner than the already-scheduled one, the new schedule is applied immediately.
	 * However, in all other cases, the next stocking takes place at the earlier proposed time to prevent items from expiring while in the machine.
	 * @param stockingInterval replacement stocking interval, in days
	 * @throws BadArgumentException if supplied with a nonpositive value
	 */
	public void setStockingInterval(int stockingInterval) throws BadArgumentException
	{
		if(stockingInterval<=0)
			throw new BadArgumentException("Stocking interval must be positive");
		
		GregorianCalendar latestStocking=lastPossibleVisit(stockingInterval);
		
		if(currentLayout.getNextVisit().compareTo(latestStocking)>0) //we now want to visit sooner
			currentLayout.setNextVisit(latestStocking);
		//otherwise, we're trying to postpone a prescheduled visit, which could allow products to expire while in the machine!
		
		this.stockingInterval=stockingInterval;
	}

	/**
	 * @return the restocking interval, in days
	 */
	public int getStockingInterval()
	{
		return stockingInterval;
	}

	/**
	 * This retrieves the current layout, which is guaranteed to have its next restocking visit defined.
	 * @return the current layout
	 */
	public VMLayout getCurrentLayout()
	{
		return currentLayout;
	}

	/**
	 * @param nextLayout a replacement future layout
	 * @throws BadArgumentException if supplied with a <tt>null</tt> value
	 */
	public void setNextLayout(VMLayout nextLayout) throws BadArgumentException
	{
		if(nextLayout==null)
			throw new BadArgumentException("Next layout cannot be null");
		
		this.nextLayout=nextLayout;
	}

	/**
	 * This retrieves the queued layout, which is not guaranteed to have a next restocking visit.
	 * @return the next layout
	 */
	public VMLayout getNextLayout()
	{
		return nextLayout;
	}

	/**
	 * Swaps the next layout into the current layout.
	 * This process automatically sets the layout's next stocking visit.
	 * @param next the new VMLayout
	 */
	public void swapInNextLayout( VMLayout next )
	{
		currentLayout=next;
		nextLayout=new VMLayout(currentLayout, true); //deep copy
		currentLayout.setNextVisit(lastPossibleVisit(stockingInterval)); //visit after stockingInterval
	}

	/**
	 * Determines when the next restocker visit should occur, according to the supplied offset from the current time.
	 * @param offset the number of days from now to next restock
	 * @return the resulting timestamp
	 */
	private GregorianCalendar lastPossibleVisit(int offset)
	{
		GregorianCalendar visitation=new GregorianCalendar(); //now
		
		visitation.add(visitation.DAY_OF_YEAR, offset);
		
		return visitation;
	}

	/**
	 * Checks whether two instances contain the same data.
	 * @param another another instance
	 * @return whether their contents match
	 */
	@Override
	public boolean equals(Object another)
	{
		if(!(another instanceof VendingMachine))
			return false;
		VendingMachine other=(VendingMachine)another;
		
		return super.equals(another) && this.active==other.active && location.equals(other.location) && this.stockingInterval==other.stockingInterval && currentLayout.equals(other.currentLayout) && nextLayout.equals(other.nextLayout);
	}

	/** @inheritDoc */
	@Override
	public String toString() {
		return super.toString() + " " + 
			   String.format("%s vending machine in %s, restocked every %d days", 
			   				 active ? "Active" : "Inactive",
			   				 location.toString().substring(ModelBase.ID_SPACES + 1),
			   				 stockingInterval);
	}
}
