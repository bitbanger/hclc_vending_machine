import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.sql.SQLException;

/**
 * Unit test suite for <tt>RestockerMachinePickerScreen</tt>
 * @author Piper Chester <pwc1203@rit.edu>
 */
@RunWith(JUnit4.class)
public class RestockerMachinePickerScreenTest {
	
	/** The Database instance */
	private DatabaseLayer db = DatabaseLayer.getInstance();

	/** The VendingMachine instance */
	private VendingMachine vm;
	
	@Test
	public void testNegativeID() throws SQLException {
		boolean testFailed = false;

		try {
			 db.getVendingMachineById(-1); 
		} catch (BadStateException e){
			testFailed = true;
		} catch (BadArgumentException e){
			testFailed = true;
		} finally{
			Assert.assertFalse(testFailed);
		}
	}
}
