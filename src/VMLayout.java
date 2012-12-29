/**
 * Encapsulates a vending machine's layout.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public class VMLayout implements ModelBase
{
	/** The layout's primary key. */
	private int layoutId;

	/** The rows of products. */
	private Row[][] rows;

	/**
	 * Fresh constructor.
	 * Creates an instance of the specified size and with the specified identifier.
	 * The actual rows are left empty until filled.
	 * This method is intended to be invoked only by classes that know a good value for this <tt>id</tt>
	 * @param id the instance's primary key
	 * @param height the major size
	 * @param width the minor size
	 * @throws IllegalArgumentException if the <tt>id</tt>, <tt>height</tt>, or <tt>width</tt> is invalid
	 */
	public VMLayout(int id, int height, int width) throws IllegalArgumentException
	{
		if(id<MIN_ID)
			throw new IllegalArgumentException("ID too low");
		else if(height<=0)
			throw new IllegalArgumentException("Height must be positive");
		else if(width<=0)
			throw new IllegalArgumentException("Width must be positive");
		
		layoutId=id;
		rows=new Row[height][width];
	}

	public VMLayout(int id, Row[][] rows)
	{
		this.layoutId = id;
		this.rows = rows;
	}

	/**
	 * Copy constructor.
	 * Creates a copy of the supplied instance.
	 * @param existing the instance to clone
	 */
	public VMLayout(VMLayout existing)
	{
		this.layoutId=existing.layoutId;
		this.rows=existing.rows;
	}

	/**
	 * @return the primary key
	 */
	public int getId()
	{
		return layoutId;
	}

	/**
	 * @return all the rows
	 */
	public Row[][] getRows()
	{
		return rows;
	}
}
