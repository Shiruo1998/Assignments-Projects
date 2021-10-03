package GroupProject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * boundary class; 
 * this class forms an simple message window
 *
 */
public class SimpleWindow extends BasicMode{
	JButton back;
	/**
	 * @param sentence whatever message to be shown on the window
	 */
	public SimpleWindow(String sentence){
		super();
		this.setSize(500,150);
		back = new JButton("OK");
		back.addActionListener(this);
		JLabel label = new JLabel(sentence);
		panel2.add(back);
		panel1.add(label);
	}
	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
}
