package GroupProject;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
/**
 * boundary class; 
 * this class forms the frame that take in user's information for registry
 *
 */
public class RegisterFrame extends JFrame implements ActionListener 
{

	private JLabel lbQmnumber = new JLabel("Please input your QM number:");
	private JTextField tfQmnumber = new JTextField(10);

	private JLabel lbEmail = new JLabel("Please input your email address:");
	private JTextField pfEmail = new JTextField(10);

	private JLabel lbName = new JLabel("Please input your fullname:");
	private JTextField tfName = new JTextField(10);

	private JButton btRegister = new JButton("Register");

	private JButton btExit = new JButton("Cancel");

	public RegisterFrame() 
	{
		super("Campus Scooter Sharing System");
		this.setLayout(new FlowLayout());
		this.add(lbQmnumber);
		this.add(tfQmnumber);
		this.add(lbEmail);
		this.add(pfEmail);
		this.add(lbName);
		this.add(tfName);
		this.add(btRegister);
		this.add(btExit);
		this.setSize(350, 200);
		GUIUtil.toCenter(this);
		this.setResizable(false);
		this.setVisible(true);


		btRegister.addActionListener(this);
		btExit.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) 
	{
		WriteText w = new WriteText();
		if (e.getSource() == btRegister) 
		{

			if (tfQmnumber.getText().equals("") || tfName.getText().equals("") || pfEmail.getText().equals("")) 
			{
				new SimpleWindow("Please complete the text field.");
				return;
			}
			if(!RegInfo.IDVerify(tfQmnumber.getText())) {
				new SimpleWindow("QM number should only consist of 9 digits. ");
				return;
			}
			if(!RegInfo.DuplicateCheck(tfQmnumber.getText())) {
				new SimpleWindow("You have already registered. ");
				return;
			}
			if(!RegInfo.NameVerify(tfName.getText())) {
				new SimpleWindow("Fullname should have more than two segment and all consist of letters. ");
				return;
			}
			if(!RegInfo.EmailVerify(pfEmail.getText())) {
				new SimpleWindow("Email address should include an '@', but not start with '@'");
				return;
			}
				w.Regist(
						tfQmnumber.getText() + "; " + tfName.getText() + "; " + pfEmail.getText());
				this.setVisible(false);
				new BackToMainF("Registered successfully!");
				return;

	}
	else
	{
		this.setVisible(false);
		new MainF();

	}

}

}