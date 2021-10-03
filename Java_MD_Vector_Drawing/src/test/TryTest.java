package test;

import model.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;

/**
 * This is the test class that tests the model
 * @author 200009734
 *
 */
public class TryTest{

		
	Color color = Color.BLACK;
	double startX = 20;
	double startY = 10;
	double endX = 100; 
	double endY = 300;
	double difX = 30;
	double difY = 5;
	double dif = 50;
	
	double widHigh;
	
	Observer observer;
	boolean isNewlyCreated = true;
	VectorShape star, ellipse, rect, line;
	Ellipse2D.Double ell;
	Rectangle2D.Double r;
	Polygon s;
	Line2D.Double l;
	ArrayList<Shape> history;
	ArrayList<Color> hisCol;
		
	@Before
	public void setup() {
		observer = new ObserverForTest();
		star = new Star(color, startX, startY, endX, endY, observer);
		ellipse = new Ellipse(color, startX, startY, endX, endY, observer);
		rect = new Rect(color, startX, startY, endX, endY, observer);
		line = new Line(color, startX, startY, endX, endY, observer);
		
		widHigh = Math.abs((startX-endX)/(startY-endY));
	}
	
	@Test
	public void test() {
		testInst();
		testIsNewlyCreated();
		testButton();
		testChangeLocation();
		testIsNewlyCreated2();
		testHistoriesIfLocationChange();
		testChangeSize();
		testHistoriesIfSizeChange();
		testChangeColor();
		testHistoriesIfColorChanged();
	}
	
	/**
	 * method for testing instanization.
	 */
	public void testInst(){
		
		assertNotNull(star);
		assertNotNull(ellipse);
		assertNotNull(rect);
		assertNotNull(line);
	}
	
	/**
	 * test if the isNewlyCreated flag is true when instanised.
	 */
	public void testIsNewlyCreated() {
		assertTrue(star.getIsNewlyCreated());
		assertTrue(ellipse.getIsNewlyCreated());
		assertTrue(rect.getIsNewlyCreated());
		assertTrue(line.getIsNewlyCreated());
	}
	/**
	 * test if a button belonging to the object is created once instanised.
	 */
	public void testButton() {
		assertNotNull(star.getLayerButton());
		assertNotNull(ellipse.getLayerButton());
		assertNotNull(rect.getLayerButton());
		assertNotNull(line.getLayerButton());
	}
	
	/**
	 * test if the coordinates of the Shape has changed correctly after location changing operation.
	 */
	public void testChangeLocation() {
		changeLocation();
		
		getShapes();
		
		assertEquals(50, ell.getX());
		assertEquals(15, ell.getY());
		assertEquals(130, ell.getX()+ell.getWidth());
		assertEquals(305, ell.getY()+ell.getHeight());

		assertEquals(50, r.getX());
		assertEquals(15, r.getY());
		assertEquals(130, r.getX()+ell.getWidth());
		assertEquals(305, r.getY()+ell.getHeight());
		
		assertEquals(50, l.getX1());
		assertEquals(15, l.getY1());
		assertEquals(130, l.getX2());
		assertEquals(305, l.getY2());
		
		assertTrue(starBoundContains(s, 50, 15, 130, 305));
		
		
	}
	/**
	 * test if the isNewlyCreated flag has changed to false when a change has been made on Shape.
	 */
	public void testIsNewlyCreated2() {
		assertFalse(star.getIsNewlyCreated());
		assertFalse(ellipse.getIsNewlyCreated());
		assertFalse(rect.getIsNewlyCreated());
		assertFalse(line.getIsNewlyCreated());
	}
	/**
	 * test if the histories have been correctly updated once location changed.
	 */
	public void testHistoriesIfLocationChange() {
		history  = ellipse.history;
		hisCol = ellipse.hisCol;
		assertEquals(ell, history.get(history.size()-1));
		assertEquals(Color.BLACK, hisCol.get(hisCol.size()-1));
		
		history  = rect.history;
		hisCol = rect.hisCol;
		assertEquals(r, history.get(history.size()-1));
		assertEquals(Color.BLACK, hisCol.get(hisCol.size()-1));
		
		history  = star.history;
		hisCol = star.hisCol;
		assertPolygonEqual(s,(Polygon)history.get(history.size()-1));
		assertEquals(Color.BLACK, hisCol.get(hisCol.size()-1));
		
		history  = line.history;
		hisCol = line.hisCol;
		assertLineEqual(l, (Line2D.Double)history.get(history.size()-1));
		assertEquals(Color.BLACK, hisCol.get(hisCol.size()-1));
		
	}
	/**
	 * test if the coordinates of the Shape has changed correctly after scaling operation.
	 */
	public void testChangeSize() {
		changeSize();
		
		getShapes();
		
		assertEquals(50, ell.getX());
		assertEquals(15, ell.getY());
		assertEquals((int)(widHigh * 5d * dif + Math.abs(startX - endX)), (int)ell.getWidth());
		assertEquals((int)(5d * dif + Math.abs(startY - endY)), (int)ell.getHeight());

		assertEquals(50, r.getX());
		assertEquals(15, r.getY());
		assertEquals((int)(widHigh * 5d * dif + Math.abs(startX - endX)), (int)r.getWidth());
		assertEquals((int)(5d * dif + Math.abs(startY - endY)),(int)r.getHeight());
		
		assertTrue(starBoundContains(s, 70, 60, 150, 350));
		
		assertEquals((int)(50 - 5d * dif * widHigh), (int)l.getX1());
		assertEquals((int)(15 - 5d * dif), (int)l.getY1());
		assertEquals((int)(130 + 5d * dif * widHigh), (int)l.getX2());
		assertEquals((int)(305 + 5d * dif), (int)l.getY2());
		
		/*
		 * Sine the scaling of lines differs on the coordinates of endpoints.
		 * Another set of numbers (coordinates) are tested on the scaling of lines. 
		 */
		Line tempL = new Line(Color.BLACK,500,80,30,3,observer);
		tempL.changeSize(dif);
		Line2D.Double temp = (Line2D.Double)tempL.getShapeObject();
		assertEquals((int)(500 + 5d * dif * (500-30)/(80-3)), (int)temp.getX1());
		assertEquals((int)(80 + 5d * dif), (int)temp.getY1());
		assertEquals((int)(30 - 5d * dif * (500-30)/(80-3)), (int)temp.getX2());
		assertEquals((int)(3 - 5d * dif), (int)temp.getY2());
	}
	
	/**
	 * test if the histories have been correctly updated once size changed.
	 */
	public void testHistoriesIfSizeChange() {
		testHistoriesIfLocationChange();
	}
	
	/**
	 * test if the histories have been correctly updated once color changed.
	 */
	public void testChangeColor() {
		changeColor();
		
		assertEquals(Color.RED, rect.getColor());
		assertEquals(Color.RED, star.getColor());
		assertEquals(Color.RED, ellipse.getColor());
		assertEquals(Color.RED, line.getColor());
	}
	
	public void testHistoriesIfColorChanged() {
		history  = ellipse.history;
		hisCol = ellipse.hisCol;
		assertEquals(ell, history.get(history.size()-1));
		assertEquals(Color.RED, hisCol.get(hisCol.size()-1));
		
		history  = rect.history;
		hisCol = rect.hisCol;
		assertEquals(r, history.get(history.size()-1));
		assertEquals(Color.RED, hisCol.get(hisCol.size()-1));
		
		history  = star.history;
		hisCol = star.hisCol;
		assertPolygonEqual(s,(Polygon)history.get(history.size()-1));
		assertEquals(Color.RED, hisCol.get(hisCol.size()-1));
		
		history  = line.history;
		hisCol = line.hisCol;
		assertLineEqual(l, (Line2D.Double)history.get(history.size()-1));
		assertEquals(Color.RED, hisCol.get(hisCol.size()-1));
		
	}
	
	/**
	 * change the location
	 */
	public void changeLocation() {
		star.changeLocation(difX, difY);
		ellipse.changeLocation(difX, difY);
		rect.changeLocation(difX, difY);
		line.changeLocation(difX, difY);
	}
	/**
	 * change the size
	 */
	public void changeSize() {
		ellipse.changeSize(dif);
		rect.changeSize(dif);
		star.changeSize(dif);
		line.changeSize(dif);
	}
	
	/**
	 * change the color
	 */
	public void changeColor() {
		rect.changeColor(Color.RED);
		star.changeColor(Color.RED);
		ellipse.changeColor(Color.RED);
		line.changeColor(Color.RED);
	}
	
	/**
	 * It is hard to calculate the points of a 5-pointed star without accessing the private createStar() method in Star object.
	 * Here I use the bound of the star to test if it is of approximately correct size.
	 * @param s the Star object
	 * @param x1 the x coordinate of the upper left point of a given rectangle
	 * @param y1 the y coordinate of the upper left point of a given rectangle
	 * @param x2 the x coordinate of the lower right point of a given rectangle
	 * @param y2 the y coordinate of the low right point of a given rectangle
	 * @return true if the star is inside the rectangle area that we want it tolocate
	 */
	public boolean starBoundContains(Polygon s, double x1, double y1, double x2, double y2) {
		Rectangle2D bound = s.getBounds();
		double distanceY = Math.max(Math.abs(x1-x2),Math.abs(y1-y2));
		
		int edgeLength = (int) (0.5 * distanceY + 0.5 * distanceY * Math.cos(Math.PI/5));
		
		if(bound.getX() <= Math.min(x1, x2) && bound.getY() <= Math.min(y1, y2) && bound.getWidth() >= edgeLength && bound.getHeight() >= edgeLength ) {
			return true;
		}
		return false;
	}
	
	/**
	 * get the Shape object from the created VectorShape object
	 */
	public void getShapes() {
		ell= (Ellipse2D.Double)ellipse.getShapeObject();
		r = (Rectangle2D.Double)rect.getShapeObject();
		s = (Polygon)star.getShapeObject();
		l = (Line2D.Double)line.getShapeObject();
	}
	/**
	 * the assertEquals method compare the addresses or two Polygon object, which is not feasible
	 * Here I self-defined an assert method that compares the coordinates of each pair of points of two polygon.
	 * @param p1 the first polygon (a star)
	 * @param p2 the second polygon
	 */
	public void assertPolygonEqual(Polygon p1, Polygon p2){
		if(p1.npoints != p2.npoints) {
			assertTrue(false);
		}
		for(int i = 0; i < p1.xpoints.length ; i++) {
			if(p1.xpoints[i] != p2.xpoints[i]) {
				assertTrue(false);
			}
		}
		for(int i = 0; i < p1.ypoints.length ; i++) {
			if(p1.ypoints[i] != p2.ypoints[i]) {
				assertTrue(false);
			}
		}
	}
	/**
	 * the assertEquals method compare the addresses or two Line2D.Double object, which is not feasible
	 * Here I self-defined an assert method that compares the endpoints of two lines.
	 * @param l1 the first line
	 * @param l2 the second line
	 */
	public void assertLineEqual(Line2D.Double l1, Line2D.Double l2) {
		if(l1.getX1() != l2.getX1() || l1.getY1() != l2.getY1() || l1.getX2() != l2.getX2() ||l1.getY2() != l2.getY2()) {
			assertTrue(false);
		}
	}
}
