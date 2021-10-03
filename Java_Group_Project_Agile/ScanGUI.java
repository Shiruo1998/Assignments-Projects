package GroupProject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
/**
 * boundary class; 
 * This class forms a frame that simulate the situation that user scans his card
 * via user entering his ID.
 */
public class ScanGUI extends EnterSth{
	String path;
	AvSlot window;
	public ScanGUI(String path){
		super("Please enter your ID:");
		this.path = path;

	}
/**
 * upon user entering his ID 
 * his registration, legality and whether to return or pick up a scooter is judged
 * and different frame is instantiated accordingly.
 */
	public void actionPerformed(ActionEvent e) {
		if((JButton)e.getSource() == ok) {
			if(ID.getText().equals("")){ 
				SimpleWindow empStr = new SimpleWindow("You have to enter your ID");
			}
			else {
				int i = SearchUser.searchUser(ID.getText()); 
				if(i==-1) { //not registered
		    		SimpleWindow notReg = new SimpleWindow("ID incorrect or not registered.");
				}
				else { //registered
					this.setVisible(false);
					ReadText r=new ReadText();
			    	ArrayList<User> users=r.ReadUser();
					if(users.get(i).getStart_time().equals("0")) {   // to pick up a scooter
		    			if (users.get(i).getJudgelegal().equals("1")) {    // legal
		    				SlotOp s = new SlotOp();
		    				s.findAvailable(1,path); // to find available scooter
		    				String str = s.getAvailable(); 
		    				if(str.equals("")) 
		    					new SimpleWindow("No available scooter");
		    				else {
		    					AvSlot myRunnable= new AvSlot(str, Integer.parseInt(users.get(i).getDaily_time()),s,i,users);
		    					//instantiate a frame where user can choose to take out the scooter from slot or not
		    					Thread th1=new Thread(myRunnable);
		    					th1.start(); //start timing
		    				}
		    					
		    			}else { //illegal
		    				BackToMainF alert = new BackToMainF("You are illegal. Please go to the administration offce to pay fine.");
		    			}
		    		}
		    		else { //to return a scooter
		    			SlotOp s = new SlotOp();
		    			s.findAvailable(0,path); //to find empty slot
		    			String str = s.getAvailable(); 
		    			if(str.equals("")) 
	    					new SimpleWindow("No empty slot");
	    				else {
	    					int minutes = SetTime.getOneTermTime(users.get(i)); 
	    					AvSlot myRunnable= new AvSlot(str,minutes,s,i,users);
	    					//instantiate a frame where user can choose to put scooter into the slot or not
	    					Thread th2=new Thread(myRunnable);
	    					th2.start(); //start timing
	   
	    				}
		    		}
		    	return;

 				}
			}
		} 
		else {
			this.setVisible(false);
			new MainF();
		}
	}
}

