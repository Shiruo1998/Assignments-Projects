package GroupProject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
/**
 * boundary class; 
 * this class forms a frame that take in user's ID when he or she is to pay fine
 *
 */
public class PayFineLogIn extends EnterSth{
	/**
	 * initialize the frame
	 */
	public PayFineLogIn(){
		super("Please enter your ID:");
	}
	/**
	 * check user's legality. 
	 * operate only on illegal user
	 * instantiate different frame according to user's input
	 */
	public void actionPerformed(ActionEvent e) {
		int timeDif=0;
		if((JButton)e.getSource() == ok) {
			if(ID.getText().equals("")){
				SimpleWindow empStr = new SimpleWindow("You have to enter your ID");
			}
			else {
				this.setVisible(false);
				int i = PayFine.payFine(ID.getText());
				switch (i){
				case 1:
					new BackToMainF("You do not have to pay.");
					break;
				case 2:
					new BackToMainF("Please return your scooter before you pay the fine");
					break;
				case 3:
					new BackToMainF("Fine paied successfully");
					break;
				case 4:
					new BackToMainF("You are not registered");		
					break;
				}
			}
		}
		else {	
			this.setVisible(false);
		}
	
	}
}

