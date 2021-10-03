package model;

import java.awt.Color;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;

/**
 * This is the superclass for all shapes.
 * It owns a shape and a layer button
 * It has color, bound(startX, startY, endX, endY), the status suggesting if the shape newly created,
 * as well as historical Shapes & colors and undo histories of Shapes & Colors.
 * @author 200009734
 *
 */
public abstract class VectorShape extends Observable implements Cloneable{
	//Class Rectangle2D.Double, Line2D.Double, Ellipse2D.Double and Polygon all implements interface java.awt.Shape.
	protected Shape shape;
	protected Color color;
	protected JButton layerButton;
	protected boolean isNewlyCreated; 
	protected double startX, startY, endX, endY;
	public ArrayList<Shape> history, undoHistory;
	public ArrayList<Color> hisCol, undoHisCol;
	Observer observer;
	
	
	public VectorShape(Color color, double startX, double startY, double endX, double endY, Observer observer){
		
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.observer = observer;
	    this.color = color;
	    this.isNewlyCreated = true;
	    layerButton = new JButton();
	    isNewlyCreated = true;
	    this.addObserver(observer);
	    
	    history = new ArrayList<Shape>();
	    undoHistory = new ArrayList<Shape>();
	    
	    hisCol = new ArrayList<Color>();
	    undoHisCol = new ArrayList<Color>();
	    
	}
	
	
	public abstract void changeLocation(double xDif, double yDif);
	
	public abstract void changeSize(double dif);
	
	public abstract void changeColor(Color color);
	
	protected abstract Shape copyShapeObject();
	
	public Shape getShapeObject() {
		// TODO Auto-generated method stub
		return shape;
	}
	
	public void setShapeObject(Shape shape) {
		this.shape = shape;
		update();
	}
	/**
	 * This method is different from changeColor() method.
	 * It is only used in undo and redo operations.
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	public Color getColor() {
		return color;
	}
	
	public boolean getIsNewlyCreated() {
		return isNewlyCreated;
	}
	public JButton getLayerButton() {
		return layerButton;
	}
	/**
	 * call this method to notify observers
	 */
	public void update() {
		this.setChanged();
        this.notifyObservers();
	}
	/**
	 * The clone() method is override so that this object can be cloned. 
	 * @Override
	 */
	public Object clone() {
		VectorShape vShape = null;
		try {
			return (VectorShape)super.clone();
		}catch(CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return vShape;
	}
		
}
