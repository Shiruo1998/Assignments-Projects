package GroupProject;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;  
import java.io.BufferedReader;  
import java.io.BufferedWriter;  
import java.io.FileWriter;  

/**
 * control class; 
 * this class contains methods that read information from txt files
 */
public class ReadText {
	
	User u;
	Slot s;
	/**
	 * to read slots' information in a station
	 * @param pathname i.e. Aseat.txt, Bseat.txt, Cseat.txt
	 * @return arrayList of Scooter objects which contain slot No.s and corresponding emptiness flag
	 */
	public ArrayList Read_Slot(String pathname){
		ArrayList<Slot> Station_List= new ArrayList();
		
        try {	
		File filename = new File(pathname);
		InputStreamReader reader = new InputStreamReader(  
		         new FileInputStream(filename));
		BufferedReader b = new BufferedReader(reader); 
        String line ;
        String[] lineArray;

        while ((line= b.readLine()) != null) {
			     lineArray=line.split(" ");
			     Slot s=new Slot(lineArray[0],lineArray[1]);
			     Station_List.add(s);			     
			}
        b.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  
        
        return Station_List;
			
	}
	
	/**
	 * this method read a user's information from a file 
	 * @return arrayList of User object 
	 */
	public ArrayList ReadUser(){
		String pathname = "Users.txt";
		ArrayList<User> User_List=new ArrayList();
		 try {	
				File filename = new File(pathname);
				InputStreamReader reader = new InputStreamReader(  
				         new FileInputStream(filename));
				BufferedReader b = new BufferedReader(reader); 
		        String line ;
		        String[] lineArray;

		        while ((line= b.readLine()) != null) {
					     lineArray=line.split("; ");
					     u=new User(lineArray[0],lineArray[1],lineArray[2],lineArray[3],lineArray[4],lineArray[5],lineArray[6]);
					     User_List.add(u);			
					}
		        b.close();
				} catch (IOException e) {
					e.printStackTrace();
				}  
		return User_List;
	}
	/**
	 * this is a method that read admin password form Admin.txt
	 * @return admin password
	 */
	public String Read_password(){
		String[] lineArray = read_admin().split("; ");
		return lineArray[0];
	}
	/**
	 * this is a method that read the time of the lately day-start   
	 * @return the time of last daily time reset
	 */
	public String Read_dayStart() {
		String[] lineArray = read_admin().split("; ");
		return lineArray[1];
	}
	/**
	 * this is a method that read the time of the lately week-start   
	 * @return the time of last weekly time reset
	 */
	public String Read_WeekStart() {
		String[] lineArray = read_admin().split("; ");
		return lineArray[2];
	}
	/**
	 * this is a method that read one line from Admin.txt
	 * @return String content in the file.
	 */
	private String read_admin() {
		String admin="";
		 try {	
				File filename = new File("Admin.txt");
				InputStreamReader reader = new InputStreamReader(  
				         new FileInputStream(filename));
				BufferedReader b = new BufferedReader(reader); 
		        admin = b.readLine();
		        b.close();
			} catch (IOException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		 return admin;
	}

}
