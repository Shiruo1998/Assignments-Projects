package GroupProject;
import java.awt.TextField;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
/**
 * control class; 
 * to see whether a user is registered or not
 */
public class SearchUser {
/**
 * this method is to search for a user by QM ID
 * @param id user's QM number
 * @return the index of the particular user; return -1 if not found
 */
	public static int searchUser(String id) {
		int index = -1;
		ReadText r=new ReadText();
		ArrayList<User> users=r.ReadUser();
		for (int i = 0; i < users.size(); i++) {	
			if(users.get(i).getQmnumber().equals(id)) {
				index = i;
			}
		}
		return index;
		
	}
}
