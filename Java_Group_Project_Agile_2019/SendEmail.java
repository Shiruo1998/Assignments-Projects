package GroupProject;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JTextField;
/**
 * control class; 
 * this class figures out the if to send week reports and what content to send
 * i.e. the total weekly periods of ride 
 * and instantiates email windows.
 */
public class SendEmail {
	public SendEmail() {
		if(isSunday()) {
			if(CalHours.calHours(Long.parseLong(new ReadText().Read_WeekStart()))<23)
				new SimpleWindow("Weekly report can only be sent once a week.");
			else{
				WriteText w=new WriteText();
				ReadText r=new ReadText();
				ArrayList<User> users=r.ReadUser();
				for (int i = 0; i < users.size(); i++) {
					String email = users.get(i).getEmail();
					String name = users.get(i).getFullname();
					String weekTime = users.get(i).getWeek_time();
					users.get(i).setWeek_time("0");
					w.Change_User(users);
					w.WriteWeekStart(new Date().getTime());
					String text = "Hi "+ name + ",\n "+"You have ridden "+ weekTime +" minutes for this week.";
					new EmailWindow(text,email);
				}
			}
		}
		else new SimpleWindow("Weekly report can only be sent on Sunday.");
			
	}
	/**
	 * this method makes judgment on the day of week
	 * @return true if today is Sunday, else return false
	 */
	private boolean isSunday() {
		Calendar calendar = Calendar.getInstance();
	    int day = calendar.get(Calendar.DAY_OF_WEEK);
	    if(day==Calendar.SUNDAY) {
	    	return true;
	    }
	    return false;
	}
}
