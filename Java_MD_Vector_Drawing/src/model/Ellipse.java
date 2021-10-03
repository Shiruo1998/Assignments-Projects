package model;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D.Double;
import java.util.Observer;
/**
 * This is the Ellipse class. Its Shape is an Ellipse2D.Double object.
 * @author 200009734
 *
 */
public class Ellipse extends VectorShape{
	private Ellipse2D.Double ellipse;
	public Ellipse(Color color, double startX, double startY, double endX, double endY, Observer observer) {
		super(color, startX, startY, endX, endY, observer);
		shape = new Ellipse2D.Double(Math.min(startX, endX), Math.min(startY, endY), Math.abs(startX - endX), Math.abs(startY - endY));
		ellipse = (Ellipse2D.Double)shape;
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
		ellipse.x = ellipse.x + xDif;
		ellipse.y = ellipse.y + yDif;
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
		//faster the size changing speed.
		dif = dif * 5d; 	
		
		//scale the ellipse
		double xDif = dif * ellipse.width / ellipse.height;
		if(!(ellipse.width + xDif == 0) && !(ellipse.height + dif == 0)) {
			ellipse.width = ellipse.width + xDif;
			ellipse.height = ellipse.height + dif;
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
		
		return new Ellipse2D.Double(ellipse.getX(), ellipse.getY(), ellipse.getWidth(), ellipse.getHeight());
	}
	

}
