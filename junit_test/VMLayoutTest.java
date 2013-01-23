import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.GregorianCalendar;

/**
 * Unit test suite for the <tt>VMLayout</tt> class.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public class VMLayoutTest
{
	@Test
	public void testSizeConstruction() throws BadArgumentException
	{
		VMLayout triad=new VMLayout(3, 4, 5);
		
		assertEquals(triad.getRows().length, 3);
		for(Row[] rowOfRows : triad.getRows())
			assertEquals(rowOfRows.length, 4);
		assertEquals(triad.getDepth(), 5);
		assertEquals(triad.getNextVisit(), null);
	}

	@Test(expected=BadArgumentException.class)
	public void testZeroRowConstruction() throws BadArgumentException
	{
		VMLayout triad=new VMLayout(0, 4, 5); }

	@Test(expected=BadArgumentException.class)
	public void testZeroColumnConstruction() throws BadArgumentException
	{
		VMLayout triad=new VMLayout(3, 0, 5);
	}

	@Test(expected=BadArgumentException.class)
	public void testZeroDropConstruction() throws BadArgumentException
	{
		VMLayout triad=new VMLayout(3, 4, 0);
	}

	@Test
	public void testArrayConstruction() throws BadArgumentException
	{
		VMLayout triad=new VMLayout(new Row[3][4], 5);
		
		assertEquals(triad.getRows().length, 3);
		for(Row[] rowOfRows : triad.getRows())
			assertEquals(rowOfRows.length, 4);
		assertEquals(triad.getDepth(), 5);
		assertEquals(triad.getNextVisit(), null);
	}

	@Test(expected=BadArgumentException.class)
	public void testNullArrayConstruction() throws BadArgumentException
	{
		VMLayout triad=new VMLayout(null, 5);
	}

	@Test(expected=BadArgumentException.class)
	public void testZeroArrayConstruction() throws BadArgumentException
	{
		VMLayout triad=new VMLayout(new Row[3][4], 0);
	}

	@Test(expected=BadArgumentException.class)
	public void testLooselyDefinedArrayConstruction() throws BadArgumentException
	{
		VMLayout triad=new VMLayout(new Row[3][], 0);
	}

	@Test(expected=BadArgumentException.class)
	public void testRaggedArrayConstruction() throws BadArgumentException
	{
		Row[][] foosball=new Row[3][];
		for(int sizeItem=0; sizeItem<foosball.length; ++sizeItem)
			foosball[sizeItem]=new Row[sizeItem+1];
		VMLayout triad=new VMLayout(foosball, 0);
	}

	@Test
	public void testShallowCopyConstruction() throws BadArgumentException, BadStateException
	{
		VMLayout orig=new VMLayout(1, 2, 3);
		orig.setNextVisit(new GregorianCalendar());
		orig.setId(8);
		VMLayout bak=new VMLayout(orig);
		
		assertTrue(orig.getRows()==bak.getRows());
		assertEquals(orig.getDepth(), bak.getDepth());
		assertTrue(orig.getNextVisit()==bak.getNextVisit());
		assertEquals(orig, bak);
		assertEquals(orig.getId(), bak.getId());
	}

	@Test(expected=BadStateException.class)
	public void testDeepCopyConstruction() throws BadArgumentException, BadStateException
	{
		VMLayout orig=new VMLayout(1, 2, 3);
		orig.setNextVisit(new GregorianCalendar());
		orig.setId(9);
		VMLayout bak=new VMLayout(orig, true);
		
		assertEquals(orig.getRows().length, bak.getRows().length);
		for(int index=0; index<orig.getRows().length; ++index)
			assertArrayEquals(orig.getRows()[index], bak.getRows()[index]);
		assertFalse(orig.getRows()==bak.getRows());
		assertEquals(orig.getDepth(), bak.getDepth());
		assertFalse(orig.getNextVisit()==bak.getNextVisit());
		assertEquals(orig.getNextVisit(), bak.getNextVisit());
		assertFalse(orig==bak);
		assertFalse(orig.getId()==bak.getId()); //should except (no bak ID)
	}

	@Test
	public void testSetObjectiveNextVisit() throws BadArgumentException
	{
		VMLayout visitee=new VMLayout(2, 4, 8);
		GregorianCalendar appointment=new GregorianCalendar();
		
		visitee.setNextVisit(appointment);
		assertEquals(visitee.getNextVisit(), appointment);
	}

	@Test
	public void testSetNullNextVisit() throws BadArgumentException
	{
		VMLayout visitee=new VMLayout(2, 4, 8);
		
		visitee.setNextVisit(null);
		assertEquals(visitee.getNextVisit(), null);
	}
}
