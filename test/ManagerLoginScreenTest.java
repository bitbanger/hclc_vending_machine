import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.sql.SQLException;

/**
 * Unit test suite for <tt>ManagerLoginScreen</tt>
 * @author Lane Lawley <lxl5734@rit.edu>
 */
@RunWith(JUnit4.class)

public class ManagerLoginScreenTest {
	
	/** The Database instance */
	private static DatabaseLayer db;

	/** The Manager instance */
	private static Manager manny;

	/** The Manager's database ID */
	private static int mannyId;

	@BeforeClass
	public static void setUpOnce() throws BadStateException, BadArgumentException, SQLException {
		db = DatabaseLayer.getInstance();
		db.nuke();
		
		manny = new Manager("Manny", "p4ssw0rd");
		db.updateOrCreateManager(manny);
		
		mannyId = manny.getId();
	}

	private boolean isLoginInfoCorrect(int id, String password) throws BadStateException, BadArgumentException, SQLException {
		Manager m = db.getManagerById(id);
		return m.comparePassword(password);
	}
	
	@Test
	public void testPerfectLogin() throws BadStateException, BadArgumentException, SQLException {
		Assert.assertTrue(isLoginInfoCorrect(mannyId, "p4ssw0rd"));
	}

	@Test
	public void testNonexistentManagerId() throws BadStateException, BadArgumentException, SQLException {
		Manager m = db.getManagerById(mannyId - 1);
		Assert.assertTrue(m == null);
	}

	@Test
	public void testInvalidPassword() throws BadStateException, BadArgumentException, SQLException {
		Assert.assertFalse(isLoginInfoCorrect(mannyId, "n0tp4ssw0rd"));
	}
}
