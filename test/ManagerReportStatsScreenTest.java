import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.SQLException;
import java.util.*;

/**
 * test suite for ManagerReportStatsScreen
 * @author Kyle Savarese <kms7341@rit.edu>
 */

@RunWith(JUnit4.class)

public class ManagerReportStatsScreenTest {

	private DatabaseLayer db;
	private TestUtilities helper;

	@Before
	public void ManagerReportStatsScreenTest() throws SQLException,
		BadStateException, BadArgumentException {
		db = DatabaseLayer.getInstance();
		db.nuke();
		helper = new TestUtilities( true );
	}

	@Test
	public void testSalesByLocation() throws SQLException, BadStateException,
		BadArgumentException {
		ManagerReportStatsScreen test = new ManagerReportStatsScreen();
		ArrayList<Transaction> against = helper.transactions;
		against.remove(1);
		ArrayList<Transaction> result = new ArrayList<Transaction> (
			test.listLocationSales( helper.machines.get(0).getLocation() ) );
		Assert.assertTrue( result.equals( against ) );
	}

	@Test
	public void testSalesByLocationBad() throws SQLException, BadStateException,
		BadArgumentException {
		ManagerReportStatsScreen test = new ManagerReportStatsScreen();
		Collection<Transaction> result = test.listLocationSales( null );
		Assert.assertTrue( result == null );
	}
}
