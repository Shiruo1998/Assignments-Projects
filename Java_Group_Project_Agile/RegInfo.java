package GroupProject;

import java.util.ArrayList;

import javax.swing.JOptionPane;
/**
 * control class; 
 * this class verifies user's input when registering
 */
public class RegInfo {
	/**
	 * to verify user's name input
	 * @param name user's name
	 * @return true only when user's input has more than two segment and all consist of letters 
	 */
	public static boolean NameVerify(String name) {
		String arrays[] = name.split(" ");
		if(arrays.length<2) {
			return false;
		}
		for(int i = 0; i<arrays.length; i++) {
			if(!arrays[i].matches("[a-zA-Z]+")) {
				return false;
			}
		}
		return true;
	}
	/**
	 * to verify user's email input 
	 * @param email user's email
	 * @return true only when user's input includes an '@', but not start with '@'
	 */
	public static boolean EmailVerify(String email) {
		if(email.indexOf("@") == -1 || email.startsWith("@")) {
			return false;
		}
		return true;
	}
	/**
	 * to verify user's QM number input
	 * @param id user's QM number
	 * @return true only when user's input consist of 9 digits
	 */
	public static boolean IDVerify(String id) {
		if (!isNumeric(id) )
		{
			return false;
		}
		return true;
	}
	/**
	 * to check whether the user has already registered
	 * @param id user's QM number
	 * @return true if user's QM number is not on the list, else return false
	 */
	public static boolean DuplicateCheck(String id) {
		ArrayList<User> user = new ReadText().ReadUser();
		for (int i = 0; i < user.size(); i++) 
		{
			if (user.get(i).getQmnumber().equals(id))
			{
				return false;
			}
		}
		return true;
	}
	private static boolean isNumeric(String qmnumber) {
		for (int i = 0; i < qmnumber.length(); i++) {
			if (qmnumber.length() != 9 || !Character.isDigit(qmnumber.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
