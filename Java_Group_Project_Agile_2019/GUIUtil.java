package GroupProject;

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
/**
 * control class; 
 * this class contains function that put the frame to the center of the screen.
 */
public class GUIUtil {
	/**
	 * this function put the frame to the center of the screen.
	 * @param comp the frame to be put to center
	 */
    public static void toCenter(Component comp) {
        GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle rec=ge.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
        comp.setLocation(((int)(rec.getWidth()-comp.getWidth())/2),
                ((int)(rec.getHeight()-comp.getHeight()))/2);

    }
}
