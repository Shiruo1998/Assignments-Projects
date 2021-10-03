package model;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Observer;
/**
 * This is the Line class. Its Shape is an Line2D.Double object.
 * @author 200009734
 *
 */
public class Line extends VectorShape{
	private Line2D.Double line;

	public Line(Color color, double startX, double startY, double endX, double endY, Observer observer) {
		super(color, startX, startY, endX, endY, observer);
		shape = new Line2D.Double(startX, startY, endX, endY);
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
		line = (Line2D.Double)shape;
		
		// TODO Auto-generated method stub
		line.x1 += xDif;
		line.x2 += xDif;
		line.y1 += yDif;
		line.y2 += yDif;
		isNewlyCreated = false;
		history.add(copyShapeObject());
		hisCol.add(color);
		update();
	}
	/**
	 * Starting form on different vertice of an rectangle, the algorithm for scaling differs.
	 * Once changed size, it is no longer newly created.
	 * A copy of the changed shape is added to the history.
	 * Current color is added to history of colors.
	 */
	@Override
	public void changeSize(double dif) {
		line = (Line2D.Double)shape;
		
		//faster the scaling speed
		dif = dif * 5d;
		double xDif = dif * line.getBounds2D().getWidth()/line.getBounds2D().getHeight();
		if(!(line.x1 - xDif == 0) && !(line.x2 - xDif == 0) && !(line.y1 - xDif == 0) && !(line.y2 - xDif == 0) && !(line.x1 + xDif == 0) && !(line.x2 + xDif == 0) && !(line.y1 + xDif == 0) && !(line.x2 + xDif == 0)) {
			if(line.x1 <= line.x2 && line.y1 <= line.y2) {
				line.x1 -= xDif;
				line.y1 -= dif;
				line.x2 += xDif;
				line.y2 += dif;
			}else if(line.x1 > line.x2 && line.y1 <= line.y2) {
				line.x1 += xDif;
				line.y1 -= dif;
				line.x2 -= xDif;
				line.y2 += dif;
			}else if(line.x1 > line.x2 && line.y1 > line.y2) {
				line.x1 += xDif;
				line.y1 += dif;
				line.x2 -= xDif;
				line.y2 -= dif;
			}else {
				line.x1 -= xDif;
				line.y1 += dif;
				line.x2 += xDif;
				line.y2 -= dif;
			}
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
		line = (Line2D.Double)shape;
		return new Line2D.Double(line.getX1(), line.getY1(), line.getX2(), line.getY2());
	}

}
