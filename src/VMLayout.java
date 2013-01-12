import java.util.Arrays;
import java.util.GregorianCalendar;

/**
 * Encapsulates a vending machine's layout.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public class VMLayout extends ModelBase
{
	/** The rows of products. */
	private Row[][] rows; 

	/** The maximum number of objects in each row */
	private int depth;

	/** When the next restocking is due (<tt>null</tt> if unset). */
	private GregorianCalendar nextVisit;

	/**
	 * Dimension specification constructor.
	 * Creates an instance of the specified size.
	 * The actual rows are left empty until filled.
	 * @param height the major size
	 * @param width the minor size
	 * @param depth the size of each product row
	 * @throws IllegalArgumentException if the <tt>height</tt>, or <tt>width</tt> is invalid
	 */
	public VMLayout(int height, int width, int depth) throws IllegalArgumentException
	{
		if(height<=0)
			throw new IllegalArgumentException("Height must be positive");
		else if(width<=0)
			throw new IllegalArgumentException("Width must be positive");
		
		rows=new Row[height][width];
		this.depth=depth;
		nextVisit=null;
	}

	/**
	 * Contents initialization constructor.
	 * Creates an instance with the provided contents.
	 * @param rows the rows to be held in the machine
	 * @param depth the size of each product row
	 * @throws IllegalArgumentException if <tt>rows</tt> is <tt>null</tt> or ragged
	 */
	public VMLayout(Row[][] rows, int depth) throws IllegalArgumentException
	{
		if(rows==null)
			throw new IllegalArgumentException("Rows cannot be null");
		for(Row[] line : rows)
		{
			if(line==null)
				throw new IllegalArgumentException("Rows cannot contain a null group");
			else if(line.length!=rows[0].length)
				throw new IllegalArgumentException("Rows cannot have ragged sizing");
		}
		
		this.rows = rows;
		this.depth=depth;
		nextVisit=null;
	}

	/**
	 * (Shallow) copy constructor.
	 * Creates a shallow copy of the supplied instance, such that the two instances always have the exact same set and arrangement of rows.
	 * @param existing the instance to clone
	 */
	public VMLayout(VMLayout existing)
	{
		this(existing, false);
	}

	/**
	 * (Optionally deep) copy constructor.
	 * If asked to make a deep copy, the two instances' sets and arrangements of rows will be decoupled, although the rows themselves will initially be shared.
	 * Additionally, a deep copy has a different storage ID so as not to be confused with the original.
	 * @param existing the instance to clone
	 * @param deep whether to make a deep copy
	 */
	public VMLayout(VMLayout existing, boolean deep)
	{
		super(); //NB: *no* ID is being set here...
		
		if(deep) //deep copy
		{
			this.rows=new Row[existing.rows.length][];
			for(int index=0; index<rows.length; ++index)
				this.rows[index]=Arrays.copyOf(existing.rows[index], existing.rows[index].length);
		}
		else //shallow copy
		{
			try
			{
				setId(existing.getId());
			}
			catch(Exception impossible) {} //there can be no ID there!
			this.rows=existing.rows;
		}
		
		this.depth=existing.depth;
		this.nextVisit=existing.nextVisit;
	}

	/**
	 * Note that you shouldn't add any <tt>Row</tt> that stocks more items than <tt>getDepth()</tt>'s value.
	 * @return all the rows
	 */
	public Row[][] getRows()
	{
		return rows;
	}

	/**
	 * @return the maximum number of elements in any row
	 */
	public int getDepth()
	{
		return depth;
	}

	/**
	 * @param nextVisit the next restocking visit
	 */
	public void setNextVisit(GregorianCalendar nextVisit)
	{
		this.nextVisit=nextVisit;
	}

	/**
	 * @return the next restocking visit, or <tt>null</tt> if none is defined
	 */
	public GregorianCalendar getNextVisit()
	{
		return nextVisit;
	}

	/**
	 * Checks whether two instances contain the same data.
	 * @param another another instance
	 * @return whether their contents match
	 */
	@Override
	public boolean equals(Object another)
	{
		if(!(another instanceof VMLayout))
			return false;
		VMLayout other=(VMLayout)another;
		
		if(this.nextVisit==null ^ other.nextVisit==null)
			return false;
		return super.equals(another) && Arrays.equals(this.rows, other.rows) && this.depth==other.depth && ((this.nextVisit==null && other.nextVisit==null) || nextVisit.equals(other.nextVisit));
	}
}
