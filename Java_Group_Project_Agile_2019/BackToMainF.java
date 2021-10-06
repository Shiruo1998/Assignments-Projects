package GroupProject;

import java.awt.event.ActionEvent;
/**
 * boundary class; 
 * this class forms a frame that jumps back to the initial frame 
 * once user clicks a button on it
 */
public class BackToMainF extends SimpleWindow{
	/**
	 * @param str whatever message that is to shown on the message window
	 */
	public BackToMainF(String str) {
		super(str);
	}
	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);
		new MainF();
	}
}
