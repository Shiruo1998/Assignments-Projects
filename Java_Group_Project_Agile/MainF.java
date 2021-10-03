package GroupProject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
/**
 * boundary class; 
 * this class forms the initial frame where client chooses to go to station A, B, C or administration office
 *
 */
public class MainF extends BasicMode{
	JButton A, B, C, adminOffice;
	/**
	 * initialize the frame
	 */
	public MainF(){
		super();
		this.setSize(300,150);
		A = new JButton("A");
		A.addActionListener(this);
		B = new JButton("B");
		B.addActionListener(this);
		C = new JButton("C");
		C.addActionListener(this);
		adminOffice = new JButton("Administration Office");
		adminOffice.addActionListener(this);
		panel1.add(A);
		panel1.add(B);
		panel1.add(C);
		panel2.add(adminOffice);
	}
	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);
		if((JButton)e.getSource() == A) {
			new ScanGUI("Aseat.txt");
		}
		if((JButton)e.getSource() == B) {
			new ScanGUI("Bseat.txt");
		}
		if ((JButton)e.getSource() == C) {
			new ScanGUI("Cseat.txt");
		}
		if((JButton)e.getSource() == adminOffice) {
			new AdminOffice();
			
		}
	}
}
