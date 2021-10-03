package GroupProject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.InputStreamReader; 
import java.io.FileOutputStream; 
import java.io.FileInputStream;  
import java.io.FileNotFoundException;

/**
 * control class; 
 * this class contains methods that write information into txt files
 *	
 */
public class WriteText {
	public String noUser;
	public String noAdm;
	public String pathname;
	public String s;
	
	/**
	 * this method write slots information into txt file of station
	 * @param pathname i.e. Aseat.txt, Bseat.txt, Cseat.txt
	 * @param slot arrayList of slots to be written in
	 */
	public void Write_Slot(String pathname,ArrayList<Slot> slot) {
		try 
		{
	        File writeName = new File(pathname); 
	        	FileWriter fw = new FileWriter(writeName);
	        	for(int i=0;i<slot.size();i++) {
	        		fw.write(slot.get(i).getSlotNo()+" "); 
		        	fw.write(slot.get(i).getSlotAvai()+"\r\n"); 	        	
	        	}
	        	fw.flush(); 
	        	fw.close();	        	
	     } 
	     catch (IOException ex) 
	     {
	        	ex.printStackTrace();
	     }
	     
	}
	/**
	 * write an arrayList of users' information into a file
	 * @param user arrayList of Users to be written in
	 */
	public void Change_User(ArrayList<User> user) {
		try 
		{
	        File writeName = new File("Users.txt"); 
	        	FileWriter fw = new FileWriter(writeName);
	          
	        	for(int i=0;i<user.size();i++) {
	        		fw.write(user.get(i).getQmnumber()+"; "+user.get(i).getFullname()+"; "+user.get(i).getEmail()+"; "+user.get(i).getJudgelegal()+"; "); 
		        	fw.write(user.get(i).getStart_time()+"; "+user.get(i).getDaily_time()+ "; "+user.get(i).getWeek_time()+"; "+"\r\n");        	
	        	}
	        
	        	fw.flush(); 
	        	fw.close();	        	
	     } 
	     catch (IOException ex) 
	     {
	        	ex.printStackTrace();
	     }
	     
	}

	/**
	 * append new user's information when registered to Users.txt
	 * @param content i.e. "[QM number]; [Email address]; [Full name]" 
	 */
	public void Regist(String content) {
        try {
            FileWriter writer = new FileWriter("Users.txt", true);
            writer.write(content);
            writer.write("; "+"1"+"; "+"0"+"; "+"0"+"; "+"0"+"; "+"\r\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	/**
	 * to write the time of daily time reset in "Admin.txt"
	 * @param start the time of daily time resent in millisecond.
	 */
	public void WriteDayStart(long start) {
		ReadText r = new ReadText();
		String pw =r.Read_password();
		String weekStart =r.Read_WeekStart();
		try {
            FileWriter writer = new FileWriter("Admin.txt");
            writer.write(pw+"; "+start+"; "+weekStart+"; ");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	/**
	 * to write the time of sending weekly reports in "Admin.txt"
	 * @param start the time when weekly reports were sent
	 */
	public void WriteWeekStart(long start) {
		ReadText r = new ReadText();
		String pw =r.Read_password();
		String dayStart =r.Read_dayStart();
		try {
            FileWriter writer = new FileWriter("Admin.txt");
            writer.write(pw+"; "+dayStart+"; "+start+"; ");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
