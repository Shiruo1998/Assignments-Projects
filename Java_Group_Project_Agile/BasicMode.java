package GroupProject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * This class is the basic frame that is extended by almost all frame classes.
 */
public abstract class BasicMode extends JFrame implements ActionListener{
	JPanel panel1;
	JPanel panel2;
	JPanel panel3;
	public BasicMode() {
		this.setTitle("Campus Scooter Sharing System");
		panel1 = new JPanel();
		panel2 = new JPanel();
		panel3 = new JPanel();		
		getContentPane().setLayout(new GridLayout(3,1));
		getContentPane().add(panel1);
		getContentPane().add(panel2);
		getContentPane().add(panel3);
		this.setSize(350, 200);
		GUIUtil.toCenter(this);
		this.setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		
	}
}
