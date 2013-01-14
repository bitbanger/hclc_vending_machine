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
	 * @throws BadArgumentException if the <tt>height</tt>, <tt>width</tt>, or <tt>depth</tt> is negative
	 */
	public VMLayout(int height, int width, int depth) throws BadArgumentException
	{
		if(height<=0)
			throw new BadArgumentException("Height must be positive");
		else if(width<=0)
			throw new BadArgumentException("Width must be positive");
		else if(depth<=0)
			throw new BadArgumentException("Depth must be positive");
		
		rows=new Row[height][width];
		this.depth=depth;
		nextVisit=null;
	}

	/**
	 * Contents initialization constructor.
	 * Creates an instance with the provided contents.
	 * @param rows the rows to be held in the machine
	 * @param depth the size of each product row
	 * @throws BadArgumentException if <tt>rows</tt> is <tt>null</tt> or ragged, or if <tt>depth</tt> is negative
	 */
	public VMLayout(Row[][] rows, int depth) throws BadArgumentException
	{
		if(rows==null)
			throw new BadArgumentException("Rows cannot be null");
		else if(depth<=0)
			throw new BadArgumentException("Depth must be positive");
		for(Row[] line : rows)
		{
			if(line==null)
				throw new BadArgumentException("Rows cannot contain a null group");
			else if(line.length!=rows[0].length)
				throw new BadArgumentException("Rows cannot have ragged sizing");
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
	 * If asked to make a deep copy, the two instances' sets, and arrangements of rows, and rows themselves will be decoupled.
	 * Additionally, a deep copy has a different storage ID so as not to be confused with the original.
	 * Note that the latter property means that a deep-copied daughter <tt>VMLayout</tt> is <i>not</i> considered <tt>equal</tt> to its mother instance.
	 * @param existing the instance to clone
	 * @param deep whether to make a deep copy
	 */
	public VMLayout(VMLayout existing, boolean deep)
	{
		super(); //NB: *no* ID is being set here...
		
		if(deep) //deep copy
		{
			this.rows=new Row[existing.rows.length][existing.rows[0].length];
			for(int row=0; row<rows.length; ++row)
				for(int col=0; col<rows[row].length; ++col)
					this.rows[row][col]= existing.rows[row][col]==null ? null : new Row(existing.rows[row][col], true);
			if(existing.nextVisit!=null)
				this.nextVisit=(GregorianCalendar)existing.nextVisit.clone();
			else
				this.nextVisit=null;
		}
		else //shallow copy
		{
			try
			{
				setId(existing.getId());
			}
			catch(Exception impossible) //there can be no ID there!
			{
				System.err.println("CRITICAL : Model detected a problem not previously thought possible!");
				System.err.print("    DUMP : ");
				impossible.printStackTrace();
				System.err.println();
			}
			this.rows=existing.rows;
			this.nextVisit=existing.nextVisit;
		}
		
		this.depth=existing.depth;
	}

	/**
	 * Note that you shouldn't add any <tt>Row</tt> that stocks more items than <tt>getDepth()</tt>'s value.
	 * @return all the things!
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
	 * Note: instances sharing a deep-copy operation are<b>n't</b> <tt>equal</tt>.
	 * @param another another instance
	 * @return whether their contents match
	 */
	@Override
	public boolean equals(Object another)
	{
		if(!(another instanceof VMLayout))
			return false;
		VMLayout other=(VMLayout)another;
		
		if(this.rows.length!=other.rows.length)
			return false;
		for(int index=0; index<rows.length; ++index)
			if(!Arrays.equals(this.rows[index], other.rows[index]))
				return false;
		if(this.nextVisit==null ^ other.nextVisit==null)
			return false;
		return super.equals(another) && this.depth==other.depth && ((this.nextVisit==null && other.nextVisit==null) || nextVisit.equals(other.nextVisit));
	}

	/** @inheritDoc */
	@Override
	public String toString() {
		return super.toString() + " " + String.format("Vending machine layout of dimensions %d x %d x %d, next visited on %s", rows.length, rows[0].length, depth, nextVisit.getTime());
	}
}
