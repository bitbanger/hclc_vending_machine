/**
 * Utility class: a pair of two arbitrary classes.
 * @author Lane Lawley <lxl5734@rit.edu>
 */
public class Pair<L,R> {
	/** First object. */
	public A first;

	/** Second object. */
	public B second;

	/**
	 * Blank constructor.
	 * Sets both fields to null.
	 */
	public Pair() {
		this.first = null;
		this.second = null;
	}

	/**
	 * Normal constructor.
	 *
	 * @param first		The first object in the pair.
	 * @param second	The second object in the pair.
	 */
	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * Shallow copy constructor.
	 *
	 * @param old	Pair to copy.
	 */
	public Pair(Pair old) {
		this.first = old.first;
		this.second = old.second;
	}
}