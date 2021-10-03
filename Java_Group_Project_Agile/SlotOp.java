package GroupProject;
import java.io.*;
import java.util.*;
/**
 * control class; 
 * this class contains methods operating on slots or scooters in stations
 *
 */
public class SlotOp 
{	
	private String available= "";
	ArrayList<Slot> slots;
	private int flag;
	String path;
	/**
	 * to find available scooter (non-empty slot) or empty slot
	 * @param a emptiness flag. if 0, to find empty slot; if 1, to find non-empty slot
	 * @param path path of the txt file of station(i.e. Aseat.txt, Bseat.txt, Cseat.txt)
	 */
	public void findAvailable(int a,String path)
	{
		this.path =path;
		ReadText readText = new ReadText();
		slots = readText.Read_Slot(path);
		for(int i = 0; i<slots.size(); i++) {
			flag = Integer.parseInt(slots.get(i).getSlotAvai());
			
			if(flag==a) {
				available = slots.get(i).getSlotNo();
				break;
			}
		}
	}

	/**
	 * to flip the emptiness flag of slot
	 */
	public void flipFlag() {
		int k = available.charAt(1)-'1';
		int flipped = -flag+1;
		slots.get(k).setSlotAvai(flipped + "");
		WriteText writeText = new WriteText();
		writeText.Write_Slot(path, slots);
	}
	/**
	 * @return the emptiness flag
	 */
	public int getFlag() {
		return flag;
	}
	/**
	 * 
	 * @return No. of available slot or scooter
	 */
	public String getAvailable() {
		return available;
	}

}

