package GroupProject;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
/**
 * to test function of verify user's QM number
 * which should consists only 9 digits
 *
 */
class RegInfoTest {

	@Test
	void test() {
		
		String id = "12345";
		//String id = "aasdfaasd";
		//String id = "000000000";
		
		boolean flag = false;
		//boolean flag= true;
		
		assertEquals(flag,RegInfo.IDVerify(id));
	}

}
