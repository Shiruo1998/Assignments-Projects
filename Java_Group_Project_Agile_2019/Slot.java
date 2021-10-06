package GroupProject;

/**
 * entity class; 
 * this is an entity class that contains information of a slot 
 * i.e. slot No. and slot's emptiness 
 */
public class Slot {
	private String slotNo; 
	private String slotAvai; 
	/**
	 * @param slotNo slot number
	 * @param slotAvai emptiness of a slot
	 */
	public Slot(String slotNo, String slotAvai) {
		this.slotNo=slotNo;
		this.slotAvai=slotAvai;
	}

	public String getSlotNo() {
		return slotNo;
	}
	public void setSlotNo(String slotNo) {
		this.slotNo= slotNo;
	}
	public String getSlotAvai() {
		return slotAvai;
	}
	public void setSlotAvai(String slotAvai) {
		this.slotAvai = slotAvai;
	}
	public String toString() {
		return "[slotNo: "+slotNo+",slotAvai: "+slotAvai+" ]";
	}
}

