package GroupProject;

import java.util.ArrayList;
/**
 * control class; 
 * this class functions to pay the fine 
 *
 */
public class PayFine {
	/**
	 * this methods allow only registered illegal users that are not on ride to pay fine
	 * @param id user's QM number
	 * @return 1 if user is legal; 2 if user is currently on ride; 3 if user is illegal; 4 if user is not registered
	 */
	public static int payFine(String id) {
		ReadText r=new ReadText();
		WriteText w=new WriteText();
		ArrayList<User> users=r.ReadUser();
		int i = SearchUser.searchUser(id);
		if(i!=-1) { //user is registered
			if (users.get(i).getJudgelegal().equals("1")) {    // user is legal
    			return 1;
			}
			else if (!users.get(i).getStart_time().equals("0")) { //user is currently on ride
				return 2;
			}
			else { //user is illegal
				users.get(i).setDaily_time("0");
				users.get(i).setJudgelegal("1"); //flip legality flag
				w.Change_User(users);
				return 3;
			}
		}
		else { //user is not registered
			return 4;
		}
	}
}
