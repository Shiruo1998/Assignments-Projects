package GroupProject;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
/**
 * to test the function of finding empty slot (flag = 0)
 * or available scooter (flag = 1)
 */
class SlotOpTest {

	@Test
	void test() {
		int flag = 1;
		//int flag = 0;
		String pathA = "Aseat.txt";
		SlotOp slotOp = new SlotOp();
		slotOp.findAvailable(flag, pathA);
		assertEquals(flag,slotOp.getFlag());
	}

}
