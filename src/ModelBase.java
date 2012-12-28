/**
 * The base class for data representations.
 * This contains general bounds on the allowable values for each type.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public interface ModelBase
{
	/** The minimum allowable primary key. */
	public static final int MIN_ID=0;
	
	/** Accessor for the item's ID. */
	public int getId();
}
