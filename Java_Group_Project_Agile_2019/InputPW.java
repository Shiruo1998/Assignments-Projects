package GroupProject;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
/**
 * boundary class; 
 * this class form the frame where the admin authenticates himself
 *
 */
public class InputPW extends EnterSth{

	public InputPW(String str) {
		super(str);
	}
	public void actionPerformed(ActionEvent e) {
		if (((JButton)e.getSource()).equals(ok)) {
			 String realPW = new ReadText().Read_password();
			 if(ID.getText().equals(realPW)) { //password is accords to that in Admin.txt
				 this.setVisible(false);
				 new MgnSysF();
			 }
			 else { //password not correct
				 new SimpleWindow("Password not correct!");
			 }
		 }
		 else {
			 this.setVisible(false);
			 new MainF();
		 }
	 }

}
