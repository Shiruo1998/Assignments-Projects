package GroupProject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
/**
 * boundary class; 
 * this is the class where admin can choose to reset user's daily riding time, check station status, 
 * check user's riding information or send weekly report via email
 *
 */
public class MgnSysF extends BasicMode{
	JButton newDay,status,rideInfo,sendEmail,back;
	public MgnSysF() {
		super();
		this.setSize(800,150);
		newDay= new JButton("Start a new day");
		status = new JButton("Station status");
		rideInfo = new JButton("Users' Information of ride");
		sendEmail = new JButton("Send weekly report");
		back = new JButton("Exit");
		newDay.addActionListener(this);
		status.addActionListener(this);
		rideInfo.addActionListener(this);
		sendEmail.addActionListener(this);
		back.addActionListener(this);
		panel1.add(newDay);
		panel1.add(status);
		panel1.add(rideInfo);
		panel1.add(sendEmail);
		panel2.add(back);
	}
	public void actionPerformed(ActionEvent e) {
		switch(((JButton) e.getSource()).getText()) {
		case "Start a new day":
			if(StartNewDay.startNewDay())
				new SimpleWindow("Time reset successfully.");
			else new SimpleWindow("Time can only be reset once within 24 hours.");
			break;
		case "Station status":
			new StationStatus();
			break;
		case "Users' Information of ride":
			new InputID("Please enter student's QM number:");
			break;
		case "Send weekly report":
			new SendEmail();
			break;
		case "Exit":
			this.setVisible(false);
			new MainF();
			break;
		}
	}
}
