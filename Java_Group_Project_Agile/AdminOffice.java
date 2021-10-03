package GroupProject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * 	boundary class; 
 *  this class form a frame that simulates the administration office, where user or administrator chooses 
 *  to pay fine, registry or enter the management system.
 *
 */
public class AdminOffice extends BasicMode{
	JButton payF;
	JButton reg;
	JButton mgnSys;
	JButton back;
	/**
	 * initialize the frame
	 */
	public AdminOffice() {
		super();
		this.setSize(500,150);
		payF= new JButton("Pay Fine");
		payF.addActionListener(this);
		reg= new JButton ("Registry");
		reg.addActionListener(this);
		mgnSys = new JButton("Management System");
		mgnSys.addActionListener(this);
		back = new JButton("back");
		back.addActionListener(this);
		panel1.add(payF);
		panel1.add(reg);
		panel1.add(mgnSys);
		panel2.add(back);
		}
	/**
	 * different frame are instantiated according to user's input
	 */
	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);
		switch(((JButton)e.getSource()).getText()) {
			case "Pay Fine":
				new PayFineLogIn();
				break;
			case "Registry":
				new RegisterFrame();
				break;
			case "Management System":
				new InputPW("Please Input admin password:");
				break;
			case "back":		
				new MainF();
				break;
			
		}
			
	}
}
