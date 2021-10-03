package GroupProject;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

/**
 * boundary class; 
 * this class form a frame where user can choose to take out (put in) a scooter or not.
 */
public class AvSlot extends JFrame implements Runnable, ActionListener{
	JButton button1, cancel;
	JLabel time;
	JLabel[] labels;
	SlotOp slotOp;
	ArrayList<User> users;
	int minutes;
	int dailyTotal;
	int i;
	String s;
	/**
	 * initialize the frame
	 * make judgment on whether the user is to borrow or return a scooter
	 * and instantiate a frame accordingly
	 * @param s The slot or scooter that is available
	 * @param minutes The period of time (in minutes) that the user has been riding.
	 * @param seat The Seat object that contains information of available scooter or slot
	 * @param i the index of this particular user
	 * @param users user arrayList
	 */
	public AvSlot(String s, int minutes, SlotOp seat,int i, ArrayList<User> users) {
		this.slotOp =seat;
		this.users=users;
		this.i=i;
		this.minutes = minutes;
		this.s =s;
		//to return a scooter
		JPanel panel4 = new JPanel(new GridLayout(1,8));
		labels = new JLabel[8];
		for(int j =0 ; j<8; j++) {
			labels[j] = new JLabel("  @ ");
			panel4.add(labels[j]);
		}
		
		JLabel label = new JLabel(" " + s + " slot is available.\n This term, you have riden for " + minutes + " minutes.");
		JLabel please = new JLabel(" "+"Please put in the scooter within 60 seconds.");
		button1 = new JButton("Put in");
		cancel = new JButton("Cancel");
		if(seat.getFlag()==1) { //to pick up a scooter 
			label = new JLabel(" "+ s + " Scooter is available.\n Today, you have riden for " + minutes + " minutes.");
			please = new JLabel(" " + "Please take out the scooter within 60 seconds.");
			button1 = new JButton("Take Out");
		}
		button1.addActionListener(this);
		cancel.addActionListener(this);
		getContentPane().setLayout(new GridLayout(5,1));
		JPanel panel= new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		time=new JLabel(" 60");
		panel.setLayout(new GridLayout(1,2));
		getContentPane().add(panel4);
		getContentPane().add(label);
		getContentPane().add(please);
		getContentPane().add(time);
		panel.add(button1);
		panel.add(cancel);
		getContentPane().add(panel);
		this.setSize(500, 200);
		GUIUtil.toCenter(this);
		this.setVisible(true);
		
	}
	/**
	 * to count 60s.
	 * after 60s, the frame disappear and user is not able to operate
	 */
	public void run() {
		int k = s.charAt(1)-'1';
		try {
			for(int seconds = 59; seconds>0; seconds--) {
				Thread.sleep(1000);
				if(seconds%2==0)
					labels[k].setText("@ "+s);
				else labels[k].setText("    ");
				time.setText(" "+String.valueOf(seconds));
			}
			this.setVisible(false);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	} 
	/**
	 * perform correspondingly to user's input,
	 * alter user's information of ride
	 * and reconsider user's legality.
	 */
	public void actionPerformed(ActionEvent e){
		this.setVisible(false);
		JButton b = (JButton)e.getSource();
		if(b.equals(button1)) {
			this.setVisible(false);
			this.slotOp.flipFlag(); 
			if(b.getText().equals("Take Out")) { //to take out the scooter
				SetTime.setStartTime(users, i);
				new SimpleWindow("Enjoy your ride!");
			}
			else { //to put the scooter in
				SetTime.setDailyTime(users, i);
				new SimpleWindow("See you next time");
			}
		}
		if(b.equals(cancel)){
			this.setVisible(false);
			new MainF();
		}
			
	}

}