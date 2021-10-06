package GroupProject;
import java.awt.TextField;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * boundary class; 
 * this class forms a frame take in an String from user or admin
 */
public class EnterSth extends BasicMode{
	TextField ID;
	JButton ok,cancel;
	/**
	 * @param str the requirement (i.e. What to enter)
	 */
	public EnterSth(String str){
		super();
		JLabel label = new JLabel(str);
		ID = new TextField(10);
		ok = new JButton ("OK");
		ok.addActionListener(this);
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		panel1.add(label);
		panel2.add(ID);
		panel3.add(ok);
		panel3.add(cancel);
	}
	public void actionPerformed(ActionEvent e) {
		
	}
}