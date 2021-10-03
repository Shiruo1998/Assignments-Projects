package GroupProject;

import java.util.ArrayList;
import java.util.Date;
/**
 * control class;
 * this class simulate the situation that a new day begins
 *
 */
public class StartNewDay {
	/**
	 * this method firstly check the time passed since last time reset, 
	 * if longer than 24 hours, sets the daily riding time of all users' to 0
	 * and adds previous daily riding time to weekly riding time.
	 * @return true if daily time get reset, false if time passed shorter than 24 hours 
	 */
	public static boolean startNewDay() {
		WriteText w = new WriteText();
		ReadText r = new ReadText();
		if(CalHours.calHours(Long.parseLong(r.Read_dayStart()))<23){
			return false;
		}
		ArrayList<User> user;
		user=r.ReadUser();
		for (int i = 0; i < user.size(); i++) {
			int dailyTime = Integer.parseInt(user.get(i).getDaily_time());
			int weeklyTime=Integer.parseInt(user.get(i).getWeek_time());
			weeklyTime =weeklyTime + dailyTime;
			user.get(i).setDaily_time("0");
			user.get(i).setWeek_time(String.valueOf(weeklyTime));
		}
		w.Change_User( user);
		w.WriteDayStart(new Date().getTime());
		return true;
	}
}
