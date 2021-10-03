package GroupProject;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
/**
 * boundary class; 
 * this class form a frame that present riding information of a particular user
 *
 */
public class UserInfoWindow extends JFrame implements ActionListener{
	JButton back;
	String info;
	public UserInfoWindow(User user){
		this.setSize(300, 300);
		JLabel a = new JLabel("QM number: " + user.getQmnumber());
		JLabel b = new JLabel("Name: " + user.getFullname());
		JLabel c = new JLabel("E-mail: " + user.getEmail());
		JLabel d = new JLabel("illegal");
		JLabel e = new JLabel("now on ride.");
		if(user.getJudgelegal().equals("1")) {
			d = new JLabel("legal");
		}
		
		if(user.getStart_time().equals("0")) {
			e = new JLabel("not currently on ride.");
		}
		
		JLabel f = new JLabel( "daily time of ride:" + user.getDaily_time());
		JLabel g = new JLabel("weekly time of ride:" + user.getWeek_time());
		getContentPane().setLayout(new GridLayout(8,1));
		getContentPane().add(a);
		getContentPane().add(b);
		getContentPane().add(c);
		getContentPane().add(d);
		getContentPane().add(e);
		getContentPane().add(f);
		getContentPane().add(g);
		this.setVisible(true);
		back=new JButton("Back");
		back.addActionListener(this);
		getContentPane().add(back);
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		this.setVisible(false);
	}
	
}
