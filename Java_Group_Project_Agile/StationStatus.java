package GroupProject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.*;
/**
 * boundary class; 
 * this class forms the frame that present the status of each station
 *
 */
public class StationStatus extends JFrame{
	JPanel panelA = new JPanel();
	JPanel panelB = new JPanel();
	JPanel panelC = new JPanel();
	JButton button;
	public StationStatus() {
		this.setSize(500,300);
		panelA.setBorder(BorderFactory.createTitledBorder("Station A: "));
		panelB.setBorder(BorderFactory.createTitledBorder("Station B: "));
		panelC.setBorder(BorderFactory.createTitledBorder("Station C: "));
		panelA.setLayout(new GridLayout(10,1,5,5));
		panelB.setLayout(new GridLayout(10,1,5,5));
		panelC.setLayout(new GridLayout(10,1,5,5));
		ReadText r = new ReadText();
		ArrayList<Slot> aScooter = r.Read_Slot("Aseat.txt");
		for(int i = 0; i<aScooter.size(); i++) {			
			panelA.add(new JLabel(" "+aScooter.get(i).getSlotNo()+ "   " + checkStatus(aScooter.get(i))+" "));
		}
		ArrayList<Slot> bScooter = r.Read_Slot("Bseat.txt");
		for(int i = 0; i<bScooter.size(); i++) {

			panelB.add(new JLabel(" "+bScooter.get(i).getSlotNo()+ "   " + checkStatus(bScooter.get(i))+" "));
		}
		ArrayList<Slot> cScooter = r.Read_Slot("Cseat.txt");
		for(int i = 0; i<cScooter.size(); i++) {

			panelC.add(new JLabel(" "+cScooter.get(i).getSlotNo()+ "   " + checkStatus(cScooter.get(i))+" "));
		}
		JPanel panel = new JPanel();
		panel.add(panelA);
		panel.add(panelB);
		panel.add(panelC);
		this.add(panel);
		this.setVisible(true);	
	}

	private String checkStatus(Slot slot) {
		if(slot.getSlotAvai().equals("0")) {
			return "0 (empty)";
		}
		else return "1 (taken)";
	}
}