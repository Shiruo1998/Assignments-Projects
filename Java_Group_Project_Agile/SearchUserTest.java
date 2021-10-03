package GroupProject;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
/**
 * to test function of searching for a user from Users' list by QM number
 *	if function as expected, when user is found, SearchUser.searchUser(id) return user's index
 *	when not found, return -1
 */
class SearchUserTest {

	@Test
	void test() {
		String id = "111111111";
		//String id = "000000000"
		
		boolean flag = true;
		//boolean flag = false;
		int i = SearchUser.searchUser(id);
		
		boolean comp = i>-1;
		assertEquals(flag, comp);
	}

}
