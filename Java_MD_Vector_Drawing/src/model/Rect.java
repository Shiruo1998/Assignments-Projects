package model;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;

/**
 * This is the Rectangle class. Its Shape is an Rectangle2D.Double object.
 * @author 200009734
 *
 */
public class Rect extends VectorShape{
	private Rectangle2D.Double rect;

	public Rect(Color color, double startX, double startY, double endX, double endY, Observer observer){
		super(color, startX, startY, endX, endY, observer);
		shape = new Rectangle2D.Double(Math.min(startX, endX), Math.min(startY, endY), Math.abs(startX - endX), Math.abs(startY - endY));
		history.add(copyShapeObject());
		hisCol.add(color);
		update();
	}
	
	/**
	 * Once changed location, it is no longer newly created.
	 * A copy of the changed shape is added to the history.
	 * Current color is added to history of colors.
	 */
	@Override
	public void changeLocation(double xDif, double yDif) {
		rect = (Rectangle2D.Double)shape;
		rect.x = rect.x + xDif;
		rect.y = rect.y + yDif;
		isNewlyCreated = false;
		history.add(copyShapeObject());
		hisCol.add(color);
		update();
	}
	/**
	 * Once changed size, it is no longer newly created.
	 * A copy of the changed shape is added to the history.
	 * Current color is added to history of colors.
	 */
	@Override
	public void changeSize(double dif) {
		rect = (Rectangle2D.Double)shape;
		
		//faster the scaling speed
		dif = dif * 5d;
		
		double xDif = dif * rect.width/rect.height;
		if(!(rect.width + xDif == 0) && !(rect.height + dif == 0)) {
			rect.width = rect.width + dif * rect.width/rect.height;
			rect.height = rect.height + dif;
			isNewlyCreated = false;
			history.add(copyShapeObject());
			hisCol.add(color);
			update();
		}
	}
	
	/**
	 * Once changed size, it is no longer newly created.
	 * A copy of the changed shape is added to the history.
	 * changed color is added to history of colors.
	 */
	@Override
	public void changeColor(Color color) {
		// TODO Auto-generated method stub
		this.color = color;
		isNewlyCreated = false;
		history.add(copyShapeObject());
		hisCol.add(color);
		update();
	}
	
	@Override
	protected Shape copyShapeObject() {
		// TODO Auto-generated method stub
		rect = (Rectangle2D.Double)shape;
		return new Rectangle2D.Double(rect.x, rect.y, rect.width, rect.height);
	}
	
}