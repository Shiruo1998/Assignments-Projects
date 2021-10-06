package GroupProject;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextField;
/**
 * boundary class; 
 * this class forms a frame that represents email sent.
 */
public class EmailWindow extends JFrame{
	/**
	 * initialize the frame
	 * @param text whatever text that is to be sent
	 * @param email the email address to send to 
	 */
	public EmailWindow(String text,String email) {

			JTextField textField = new JTextField(text);
			textField.setFont(new Font("",Font.BOLD,27));
			this.setTitle("To: " + email);
			this.add(textField);
			this.setSize(800, 200);
			this.setVisible(true);
			this.setLocation(10, 20);

	}
}
