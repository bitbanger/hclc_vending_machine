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
	 * Creates an instance with the specified <tt>id</tt> and <tt>location</tt>.
	 * Initially, the machine is considered to be active, but lacks any product layout.
	 * This method is intended to be invoked only by classes that know a good value for this <tt>id</tt>
	 * @param id the instance's primary key
	 * @param location the instance's abode
	 * @throws IllegalArgumentException if the <tt>id</tt> is invalid
	 */
	public VendingMachine(int id, Location location) throws IllegalArgumentException
	{
		if(id>=MIN_ID)
		{
			machineId=id;
			active=true;
			this.location=location;
			currentLayout=null;
			nextLayout=null;
		}
		else throw new IllegalArgumentException("ID too low");
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
}
