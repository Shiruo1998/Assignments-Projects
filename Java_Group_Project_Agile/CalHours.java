package GroupProject;

import java.util.Date;
/**
 * control class;
 * this class contains method to calculated hours that have passed
 *
 */
public class CalHours {
	/**
	 * to this class contains method to calculated hours that have passed since a given start time 
	 * @param start the start time in millisecond
	 * @return the hours passed
	 */
	public static int calHours(long start) {
		Date now = new Date();
		int hour =(int) ((now.getTime()-start)/(1000*3600));	
		return hour;
	}
}
