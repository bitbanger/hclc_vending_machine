/**
 * Encapsulates a vending machine's layout.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public class VMLayout extends ModelBase
{
	/** The rows of products. */
	private Row[][] rows;

	/**
	 * Dimension specification constructor.
	 * Creates an instance of the specified size.
	 * The actual rows are left empty until filled.
	 * @param height the major size
	 * @param width the minor size
	 * @throws IllegalArgumentException if the <tt>height</tt>, or <tt>width</tt> is invalid
	 */
	public VMLayout(int height, int width) throws IllegalArgumentException
	{
		if(height<=0)
			throw new IllegalArgumentException("Height must be positive");
		else if(width<=0)
			throw new IllegalArgumentException("Width must be positive");
		
		rows=new Row[height][width];
	}

	/**
	 * Contents initialization constructor.
	 * Creates an instance with the provided contents.
	 * @param rows the rows to be held in the machine
	 * @throws IllegalArgumentException if <tt>rows</tt> is <tt>null</tt> or ragged
	 */
	public VMLayout(Row[][] rows)
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
	}

	/**
	 * Copy constructor.
	 * Creates a copy of the supplied instance.
	 * @param existing the instance to clone
	 */
	public VMLayout(VMLayout existing)
	{
		super(existing);
		this.rows=existing.rows;
	}

	/**
	 * @return all the rows
	 */
	public Row[][] getRows()
	{
		return rows;
	}
}
