package delegate;

import model.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Shape;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This is the Delegate part of MD, 
 * it is a Frame that observes the change of VectorShape objects.
 * @author 200009734
 *
 */

public class Frame extends JFrame implements Observer{
	private static int layerNo = 0;
	private final int DEFAULT_WIDTH = 600;
	private final int DEFAULT_HEIGHT = 600;
	private final int PANEL_NO = 6;
	
	private JButton undo, redo, rect, ellipse, star, line, color, clear, delete, changeColorButton;
	private JPanel actionBar, canvas, colorContainer, undoRedo, undoRedoClear, LMContainer,layerBar, manipulationBar,panel1;
	private JLabel layerLabel, layerLabelM, instruction;
	
	//Each VectorShape object owns a button that represents the layer it is on,
	//which can be retrieved by a getter in VectorShape.
	//Here I use a HashMap to retrieve the VectorShape by button,
	//So that retrieving can be bidirectional.
	private HashMap<JButton, VectorShape> map;
	private ArrayList<JButton> layers;
	
	//To redo layer shape drawing, I need an ArrayList containing undo layers
	private ArrayList<JButton> undoLayers;
	
	private MouseAdapter mouseAdapter;
	private ResizeHandler resizeHandler;
	
	private JButton currentLayer;
	private JButton buttonPressed;
	
	//the location information of mouse press and release
	private int startX, startY, endX, endY;
	
	public static void main(String[] args) {
		new Frame();
	}
	
	/**
	 * Once the programme runs, a Frame pop up.
	 */
	public Frame() {
		
		map = new HashMap<JButton, VectorShape>();
		layers = new ArrayList<JButton>();
		undoLayers = new ArrayList<JButton>();
		
		actionBar = new JPanel();
		canvas = new JPanel();
		undoRedo = new JPanel(new GridLayout(1,2));
		undoRedoClear = new JPanel(new BorderLayout());

		layerBar = new JPanel();
		BoxLayout bLayout = new BoxLayout(layerBar, BoxLayout.Y_AXIS);
		layerBar.setLayout(bLayout);
		
		manipulationBar = new JPanel();
		bLayout = new BoxLayout(manipulationBar, BoxLayout.Y_AXIS);
		manipulationBar.setLayout(bLayout);
		
		
		layerLabel = new JLabel("<html>-----------<br><p style=\"text-align:center;\">*    Layers    *</p>-----------</html>");

		undo = new JButton("undo");
		redo = new JButton("redo");
		rect = new JButton("rectangle");
		ellipse = new JButton("ellipse");
		star = new JButton("star");
		line = new JButton("line");
		clear = new JButton("clear");
		color = new JButton();
		
		//the manipulation buttons for a layer
		delete = new JButton("<html><font size=\"3\">delete<br>layer</font></html>");
		changeColorButton = new JButton("<html><font size=\"3\">change<br>color</font></html>");
		
		colorContainer = new JPanel();
		LMContainer = new JPanel();		
		layerLabelM = new JLabel(); 
		instruction = new JLabel();
		
		buttonPressed = new JButton();
		
		//adding actionListeners to each button... 
		rect.addActionListener(new ShapeCreateActionListener((Observer)this));
		ellipse.addActionListener(new ShapeCreateActionListener((Observer)this));
		star.addActionListener(new ShapeCreateActionListener((Observer)this));
		line.addActionListener(new ShapeCreateActionListener((Observer)this));
		color.addActionListener(new ColorActionListener(this, color.getBackground()));
		
		undo.addActionListener(new UndoActionListener(this));
		redo.addActionListener(new RedoActionListener(this));
		
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//The user has to confirm before deleting a layer (i.e. VectorShape object).
				int response = JOptionPane.showConfirmDialog(null, "<html><p style=\"text-align:center;\">Layers deleted cannot be restored.<br>Are you sure you want to delete this layer?</p></html>", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(response == JOptionPane.NO_OPTION) {
					return;
				}
				layers.remove(currentLayer);
				Graphics g = canvas.getGraphics();
                Graphics2D g2 = (Graphics2D) g;
				drawOthers(g2,currentLayer);
				
				//
				
				layerBar.remove(currentLayer);
				layerBar.paintAll(layerBar.getGraphics());
				toDefaultMBar();
				buttonPressed = (JButton) e.getSource();
			}
		});
		changeColorButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				map.get(currentLayer).changeColor(color.getBackground());
			}
		});
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//The user has to confirm before clearing the canvas.
				int response = JOptionPane.showConfirmDialog(null, "<html><p style=\"text-align:center;\">Are you sure you want to clear the canvas?<br>You can no longer access the drawing once cleared.</p></html>", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(response == JOptionPane.NO_OPTION) {
					return;
				}
				
				Graphics g = canvas.getGraphics();
                Graphics2D g2 = (Graphics2D) g;
                g2.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                
                layerNo = 0;
                layers.clear();
                layerBar.removeAll();
                layerBar.add(layerLabel);
                layerBar.revalidate();
                layerBar.repaint();
                toDefaultMBar();

			}
		
		});
		
		//The panel is just for a better look of color selection button on the frame.
		JPanel forGoodLook = new JPanel();
		forGoodLook.setLayout(new BorderLayout());
		
		forGoodLook.add(new JLabel("color", JLabel.CENTER), BorderLayout.NORTH);
		forGoodLook.add(new JPanel(), BorderLayout.WEST);
		forGoodLook.add(color, BorderLayout.CENTER);
		forGoodLook.add(new JPanel(), BorderLayout.EAST);
		forGoodLook.add(new JPanel(), BorderLayout.SOUTH);
		
		color.setOpaque(true);
		color.setBorderPainted(false);
		color.setBackground(Color.BLACK);
		
		colorContainer.setLayout(new BorderLayout());
		colorContainer.add(new JPanel(), BorderLayout.NORTH);
		colorContainer.add(new JPanel(), BorderLayout.WEST);
		colorContainer.add(forGoodLook, BorderLayout.CENTER);
		colorContainer.add(new JPanel(), BorderLayout.EAST);
		colorContainer.add(new JPanel(), BorderLayout.SOUTH);
		
		//Undo, Redo and Clear buttons are put together. 
		undoRedoClear.add(clear, BorderLayout.NORTH);
		undoRedo.add(undo);
		undoRedo.add(redo);
		undoRedoClear.add(undoRedo, BorderLayout.CENTER);
		
		//the actionBar contains the buttons to draw VectorShapes, choose color and UndoRedoClear panel. 
		actionBar.setLayout(new GridLayout(1,PANEL_NO));
		actionBar.add(undoRedoClear);
		actionBar.add(line);
		actionBar.add(rect);
		actionBar.add(ellipse);
		actionBar.add(star);
		actionBar.add(colorContainer);
		
		//the layerBar is to contains the buttons linking to each layer (i.e. VectorShape object)
		layerBar.add(layerLabel);
		
		panel1 = new JPanel();		
		bLayout = new BoxLayout(panel1, BoxLayout.Y_AXIS);
		
		//the manipulationBar contains instructions for the user and "delete layer" & "change color" button.
		manipulationBar.add(layerLabelM);		
		manipulationBar.add(instruction);		
		manipulationBar.add(changeColorButton);
		manipulationBar.add(delete);
		
		toDefaultMBar();
		
		LMContainer.setLayout(new GridLayout(1,2));
		LMContainer.add(layerBar);
		LMContainer.add(manipulationBar);
		
		//The frame has the layerBar and manipulationBar together on the right,
		//the actionBar on the top, the canvas on the center.
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(actionBar, BorderLayout.NORTH);
		this.getContentPane().add(canvas, BorderLayout.CENTER);
		this.getContentPane().add(LMContainer, BorderLayout.EAST);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		
		this.setVisible(true);
		this.setResizable(false);
	}
	
	/**
	 * This method create a thread whenever the the program observe a change in VectorShape object.
	 */
    public void update(Observable changedObject, Object arg1) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        Graphics g = canvas.getGraphics();
                        Graphics2D g2 = (Graphics2D) g;
                        VectorShape changedShape = (VectorShape)changedObject;
                        JButton layerButton =  changedShape.getLayerButton();
                        
                        //if the observed VectorShape object is newly created,
                        //draw it on the canvas, add its button to layerBar, 
                        //add the VectorShape object to the actionsDone arrayList.
                        if(changedShape.getIsNewlyCreated()) {
                        	g2.setColor(changedShape.getColor());
                        	g2.draw(changedShape.getShapeObject());
                            layerButton.addActionListener(new LayerActionListener());
                            map.put(layerButton, changedShape);
                 
                            layerNo ++;
                            layerButton.setText("L" + layerNo);
                            layers.add(layerButton);
                            layerBar.add(layerButton);
                            
                            layerBar.paintAll(layerBar.getGraphics());
                            currentLayer = null;
                        }else{
                        	//if a VectorShape object observed just changes
                        	//clear the canvas and redraw all
                        	g2.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            				drawAllLayers(g2, layers); 
                        }
                    }
                });
    }
    
    /**
     * This method draws all shapes apart from the shape that the given button links to. 
     * @param g2 the Graphics2D object of the canvas
     * @param layerButton the button that links to the shape which we do not want to draw.
     */
    public void drawOthers(Graphics2D g2, JButton layerButton) {
    	g2.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    	for(int i = 0; i <layers.size(); i++) {
    		if (layers.get(i) != null){
    			if(!(layers.get(i) == layerButton)) {
    				g2.setColor(map.get(layers.get(i)).getColor());
    				g2.draw(map.get(layers.get(i)).getShapeObject());
   
    			}
    		}
    	}
    }
    
    /**
     * This method draws a particular shape
     * and add it to actionsDone array (i.e. historical actions). 
     * @param g2 the Graphics2D object of the canvas
     * @param shape the shape we want to draw.
     */
    public void drawThisShape(Graphics2D g2, VectorShape shape) {
    	g2.setColor(shape.getColor());
    	g2.draw(shape.getShapeObject());
    }
    
    /**
     * This method draws all available layers (i.e. shapes).
     * @param g2 g2 the Graphics2D object of the canvas
     * @param layers the arrayList of current layer buttons available;
     */
    public void drawAllLayers(Graphics2D g2, ArrayList<JButton> layers) {
    	for(int j = 0 ; j < layers.size(); j++) {
			g2.setColor(map.get(layers.get(j)).getColor());
			g2.draw(map.get(layers.get(j)).getShapeObject());
		}
    }
    
    /**
     * This method remove all listeners on the canvas.
     * It is usually called when another button is pressed.
     */
    public void removeAllListenersOnCanvas() {
    	if(!canvas.getMouseListeners().equals(null)) {
			canvas.removeMouseListener(mouseAdapter);
		}
		if(!canvas.getMouseWheelListeners().equals(null)) {
			canvas.removeMouseWheelListener(resizeHandler);
		}
    }
        
    /**
     * This method gives the manipulationBar a default look.
     */
    public void toDefaultMBar() {
    	layerLabelM.setText("<html>-----------<br> <br>-----------</align></html>"); 
		instruction.setText("<html><font size=\"3\">&nbsp;Choose a<br>&nbsp;layer for<br>&nbsp;manipulation.<br> <br></font></html>");
		delete.setEnabled(false);
		changeColorButton.setEnabled(false);
    	manipulationBar.repaint();
    }
    
    /**
     * This is an actionListener for scaling the shape by mouse wheel scrolling
     *	@author 200009734
     */
    private class ResizeHandler implements MouseWheelListener {
    	VectorShape shape;
    	ResizeHandler(VectorShape shape){
    		this.shape = shape;
    	}
    	public void mouseWheelMoved(MouseWheelEvent e) {    	 
    		int x = e.getX();
    	 
    		int y = e.getY();
    	 
    		if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {    	    	
    	    	shape.changeSize(e.getWheelRotation());
    		}
    	}
    }
    
    /**
     * This is an actionListener for a shape's location change by mouse drag
     * @author 200009734
     *
     */
    private class LocationChangeHandler extends MouseAdapter{
    	private VectorShape shape;
    	
    	LocationChangeHandler(VectorShape shape) {
    		this.shape = shape;
    	}
    	
    	public void mousePressed(MouseEvent event) {
			startX = event.getX();
			startY = event.getY();
		}
		public void mouseReleased(MouseEvent event) {
			endX = event.getX();
			endY = event.getY();
    		shape.changeLocation(endX - startX, endY - startY);
		}
    }
    
    /**
     * This us an actionListener for the layer buttons.
     * @author 200009734
     *
     */
    private class LayerActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			Graphics g = canvas.getGraphics();
			Graphics2D g2 = (Graphics2D) g;
			JButton button = (JButton)e.getSource();
			VectorShape shape = map.get(button);
			
			//the shape linked by the clicked button flashes to suggests which shape the user is to manipulate.
			drawOthers(g2,button);			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			g2.setColor(shape.getColor());
			g2.draw(shape.getShapeObject());
			
			
			removeAllListenersOnCanvas();
			
			//the user can scale the selected shape by mouse wheel scrolling
			resizeHandler = new ResizeHandler(shape);
			canvas.addMouseWheelListener(resizeHandler);
			
			//the user can move the selected shape by mouse drags.
			mouseAdapter = new LocationChangeHandler(shape);
			canvas.addMouseListener(mouseAdapter);
			
			//instructions are given on the manipulationBar
			layerLabelM.setText("<html>-----------<br><p style=\"text-align:center;\">" + ((JButton)e.getSource()).getText() +  "</p>-----------</align></html>");
			instruction.setText("<html><font size=\"3\">&nbsp;Mouse wheel<br>&nbsp;scroll for<br>&nbsp;scaling.<br> <br>&nbsp;Mouse drag<br>&nbsp;to move the<br>&nbsp;shape around.<br> <br>&nbsp;Select a color<br>&nbsp;before change<br>&nbsp;color.<br> <br></font></html>");
			
			//the user can change the shape's color
			//the user can also choose to delete the layer.
			delete.setEnabled(true);
			changeColorButton.setEnabled(true);
			
			currentLayer = button;
			buttonPressed = button;
			
			
		}
    }
    
    /**
     * This is an actionListener that add another actionListner on the canvas
     * once a shape drawing button (i.e. "rectangular", "ellipse" ... ) is clicked.
     * @author 200009734
     *
     */
    private class ShapeCreateActionListener implements ActionListener{
    	Observer observer;
    	ShapeCreateActionListener(Observer observer){
    		this.observer = observer;
    	}
    	
    	public void actionPerformed(ActionEvent e) {
			if(!e.getSource().equals(buttonPressed)) {
				removeAllListenersOnCanvas();
				mouseAdapter = new SelfDefinedMA(observer, (JButton)e.getSource());
				canvas.addMouseListener(mouseAdapter);
			}
			buttonPressed = (JButton)e.getSource();
            toDefaultMBar();
		}
    	
    }
    
    /**
     * This is an actionListener that create a VectorShape object
     * corresponding to user's mouse drag on the canvas.
     * @author 200009734
     *
     */
    private class SelfDefinedMA extends MouseAdapter{
    	Observer observer;
    	JButton button;
    	SelfDefinedMA(Observer observer, JButton button){
    		this.observer = observer;
    		this.button = button;
    	}
    	
		public void mousePressed(MouseEvent event) {
			startX = event.getX();
			startY = event.getY();
		}
		
		public void mouseReleased(MouseEvent event) {
			endX = event.getX();
			endY = event.getY();
			Color choosedColor = color.getBackground();
			if(button.equals(rect)) {
				new Rect(choosedColor, startX, startY, endX, endY, observer);
			}
			if(button.equals(ellipse)) {
				new Ellipse(choosedColor, startX, startY, endX, endY, observer);
			}
			if(button.equals(star)) {
				new Star(choosedColor, startX, startY, endX, endY, observer);
			}
			if(button.equals(line)) {
				new Line(choosedColor, startX, startY, endX, endY, observer);
			}
		}

    }
    /**
     * This is an actionListner that perform undo operation.
     * The user can undo the scaling, location change as well as color change of selected shape (i.e. layer).
     * If no chosen layer, the user can undo recent layer creations.
     * 
     * @author 200009734
     *
     */
    private class UndoActionListener implements ActionListener{
    	JFrame parent;
    	
    	UndoActionListener(JFrame parent){
    		this.parent = parent;
    	}
    	public void actionPerformed(ActionEvent e) {
    		    		
			Graphics g = canvas.getGraphics();
            Graphics2D g2 = (Graphics2D) g;
            
            if(currentLayer == null) {
            	if(layers.size() == 0) {
            		JOptionPane.showMessageDialog(parent, "No layer made.");
                    return;
            	}
            	JButton lastLayer = layers.get(layers.size()-1);
            	undoLayers.add(lastLayer);
            	layers.remove(lastLayer);
            	layerBar.remove(lastLayer);
            	layerBar.revalidate();
            	layerBar.repaint();
            	layerNo --;
            	g2.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            	drawAllLayers(g2, layers);
            	return;
            }
            
            VectorShape currentShape = map.get(currentLayer);
            ArrayList<Shape> history = currentShape.history;
            ArrayList<Color> hisCol = currentShape.hisCol;
        
            if(history.size() <= 1) {
                JOptionPane.showMessageDialog(parent, "No previous actions on this Layer.");
                return;
            }
            Shape shape = history.get(history.size() - 2);
            Shape shapeToRemove = history.get(history.size()-1);
            Color colorToRemove = hisCol.get(hisCol.size()-1);
            history.remove(shapeToRemove);
            currentShape.undoHistory.add(shapeToRemove);
            currentShape.setColor(hisCol.get(hisCol.size()-2));
            hisCol.remove(colorToRemove);
            currentShape.undoHisCol.add(colorToRemove);
            currentShape.setShapeObject(shape);
							
			buttonPressed = (JButton) e.getSource();			
		}
    	
    }
    
    /**
     * This is the actionListener to perform redo operation.
     * The user can redo scaling, location change as well as color change of chosen shape (i.e. layer)
     * Recent undo-ed layer creation can be redo-ed, if no layer is selected.
     * 
     * @author 200009734
     *
     */
    private class RedoActionListener implements ActionListener{
    	JFrame parent;
    	
    	RedoActionListener(JFrame parent){
    		this.parent = parent;
    	}
    	
		@Override
		public void actionPerformed(ActionEvent e) {
			
			Graphics2D g2 = (Graphics2D)canvas.getGraphics();
			
			if(currentLayer == null) {
				if(undoLayers.size() == 0) {
					JOptionPane.showMessageDialog(parent, "No further layers to remake.");
					return;
				}
				JButton lastLayer = undoLayers.get(undoLayers.size()-1);
            	layers.add(lastLayer);
            	undoLayers.remove(lastLayer);
            	layerBar.add(lastLayer);
            	layerBar.revalidate();
            	layerBar.repaint();
            	layerNo ++;
            	drawAllLayers(g2, layers);
            	return;
			}
			
			VectorShape currentShape = map.get(currentLayer);
            ArrayList<Shape> undoHistory = currentShape.undoHistory;
            ArrayList<Color> undoHisCol = currentShape.undoHisCol;

			if(undoHistory.size() == 0) {
				JOptionPane.showMessageDialog(parent, "No further actions on this layer.");
				return;
			}
			
			Shape shape = undoHistory.get(undoHistory.size()-1);
			Color color = undoHisCol.get(undoHisCol.size()-1);
            undoHistory.remove(shape);
            currentShape.history.add(shape);
            currentShape.setShapeObject(shape);
            currentShape.setColor(color);
            undoHisCol.remove(color);
            currentShape.hisCol.add(color);
			
            g2.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            drawAllLayers(g2, layers);
			buttonPressed = (JButton) e.getSource();
		}
		
    }
	/**
	 * This class is an actionListener that allow the user to select a color on a ColorChooser.
	 * @author 20009734
	 *
	 */
    private class ColorActionListener implements ActionListener{
    	Component component;
    	Color initialColor;
    	ColorActionListener(Component component, Color color){
    		this.component = component;
    		initialColor = color;
    	}
		@Override
		public void actionPerformed(ActionEvent e) {
			Color choosedColor = JColorChooser.showDialog(component,"Color Chooser", initialColor);
			((JButton)e.getSource()).setBackground(choosedColor);
			buttonPressed = (JButton)e.getSource();
		}
    	
    }
    
}