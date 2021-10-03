package GroupProject;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JButton;
/**
 * boundary class; 
 * this class forms the frame where admin search for a user's information.
 * 
 */
public class InputID extends EnterSth{
 public InputID(String str) {
	 super(str);
 }

 public void actionPerformed(ActionEvent e) {
	 if (((JButton)e.getSource()).equals(ok)) {
		 ArrayList<User> users = new ReadText().ReadUser();
		 int i = SearchUser.searchUser(ID.getText());
		 if(i!=-1) { //user is registered
			 this.setVisible(false);
			 new UserInfoWindow (users.get(i));
		 }
			//user is not registered
		 else	new SimpleWindow("QM number invalid or not registered.");
	 }
	 else this.setVisible(false);
 }
}
