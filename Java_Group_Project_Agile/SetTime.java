package GroupProject;

import java.util.ArrayList;
/**
 * control class; 
 * this class contains methods that sets user's start-time of ride and renews daily riding time 
 *
 */
public class SetTime {
	/**
	 * this method sets user's start-time of ride and write it into User.txt
	 * @param users arrayList of User objects
	 * @param i index of the particular user
	 */
	public static void setStartTime(ArrayList<User> users, int i) {
		int starttime=(int)((System.currentTimeMillis()/1000));
		users.get(i).setStart_time(String.valueOf(System.currentTimeMillis())); //set user's start time of ride
		new WriteText().Change_User(users);
	}
	/**
	 * this method  renews daily riding time and write it into User.txt
	 * @param users arrayList of User objects
	 * @param i index of the particular user
	 */
	public static void setDailyTime(ArrayList<User> users, int i) {
		int minutes = getOneTermTime(users.get(i));
		users.get(i).setStart_time("0");		
		if(minutes>30) { // if this term lasts more than 30 minutes, set illegal
			users.get(i).setJudgelegal("0");
		}
		int dailyTotal = Integer.parseInt(users.get(i).getDaily_time())+minutes; //calculate daily riding time
		if(dailyTotal>120) { //if daily riding time exceeds 120 minutes, set illegal
			users.get(i).setJudgelegal("0");
		}
		
		users.get(i).setDaily_time(String.valueOf(dailyTotal));
		new WriteText().Change_User(users);
	}
	/**
	 * this method calculates period of ride of this term
	 * @param user User object
	 * @return period of single-term ride in minutes 
	 */
	public static int getOneTermTime(User user) {
		int minutes;
		long endtime=(System.currentTimeMillis());
		String starttime = user.getStart_time();
		minutes = (int)((endtime-Long.parseLong(starttime))/60000); 
		return minutes;
	}
}
