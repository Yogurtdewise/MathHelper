/**
 * Name:         Math Helper
 * Version:      1.0.0
 * Version Date: 04/30/2015
 * Team:         "Cool Math" - Consists of Kenneth Chin, Chris Moraal, Elena Eroshkina, and Austin Clark
 * Purpose:      The "Math Helper" software is used to aid parents and teachers with the teaching and testing
 *                 of students, grades PreK through Grade 4, in the subject of Mathematics. The lessons and
 *                 tests provided cover a subset of skills as specified by the Massachusetts Department of
 *                 Education's (DOE) website, found at:
 *                              http://www.doe.mass.edu/frameworks/math/2000/toc.html
 *                 The DOE category, “Number Sense and Operations” for Grades Pre-K through Grade 4,
 *                 is the subset that the "Math Helper" software covers.
 *                 
 *               Features and services of the "Math Helper" software include, Login/Logout mechanics,
 *                 practice and formal testing, and tutorials of the above-specified skills. Additional
 *                 features include test completion results, test completion summaries, and test
 *                 completion rewards.
 */
package project.tools;

//Utilities used to add a background image
import java.awt.Graphics;
import java.awt.image.BufferedImage;

//Utilities used for creating and adding to the JPanel.
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;



//Utilities used for mouse listener.
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.CopyOnWriteArrayList;

//Utilities used to create borders.
import java.awt.Color;

import javax.swing.border.BevelBorder;
import javax.swing.BorderFactory;

//Utilities used for border flashing events.
import javax.swing.Timer;

import project.interfaces.Clickable;
import project.interfaces.ClickableObserver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * This class is a convenience class, used to simplify the construction of Swing containers for this project.
 *  Constructs a JPanel with optional mouse listener events and borders. The JPanel must have a background
 *  image. The JPanel's dimensions are equal to the dimensions of the background image.
 * NOTE: This JPanel does not implement a Layout Manager. This means that, if the user chooses to
 *  forego the addComponent() method of this class, they must explicitly set properties such as
 *  component.setBounds() before using add(component). This class intentionally does not override
 *  any unnecessary methods of the JPanel class. This class is meant to add and simplify JPanel 
 *  functionality; not to change its intended use.
 * @author Kenneth Chin
 *
 */
public class ContentPane extends JPanel implements Clickable{

	private BufferedImage background;     //The current background image.
	private boolean hasBorders;           //Used to flag if the ContentPane should have borders applied.
	private boolean isSelectable = false; //Used to flag if the ContentPane should be selectable. 
	private boolean isSelected   = false; //Used to determine if this ContentPane is selected.
	
	//NOTE: It has been observed that ClickableObservers often call removeObserver()
	// before notifyObservers() has finished iterating. Since notifyObservers() only
	// cares about the iterator version that was obtained when it was called,
	// CopyOnWriteArrayList is preferable over synchronized versions of the method.
	// This implementation prevents any ConcurrentModificationExceptions that occur
	// as a result of the above described behavior.
	private CopyOnWriteArrayList<ClickableObserver> observers; //A list of observers.
	
	
	//Timing events that allow a border to flash.
	private boolean isBorderFlashing = false; //Prevents flashBorder() from being called if true.
	private boolean borderToggled    = true;  //Used to track border color.
	private int     flashCounter     = 0;     //Counts the number of times the border changed color.
	private Timer   timer;                    //The Timer object used to flash the border.
	private ActionListener flashBorder;       //The ActionListener that listens for Timer events.
	
	
/*****************************
 * ContentPane Constructors	 *
 *****************************/
	
	/**
	 * Constructs a JPanel container that uses a BufferedImage background. The panel's dimensions
	 *  are equal to the dimensions of the background image. Mouse click events and borders are optional.
	 * @param backgroundImage The image that is to be used as this panel's background. Also used to define
	 *  the panel's dimensions.
	 * @param clickable A boolean. If true, this panel can track mouse events and will flash its borders
	 *  if it is clicked. Additionally, the panel's borders will change color when the mouse enters the
	 *  panel's borders. Otherwise, this panel will not respond to mouse events.
	 * @param borders A boolean. If true, this panel will have a raised-beveled boarder. Otherwise, this
	 *  panel will not have any borders.
	 */
	public ContentPane(BufferedImage backgroundImage, boolean clickable, boolean borders){
		background = backgroundImage;
		hasBorders = borders;
		
		setLayout(null);
		setSize(background.getWidth(), background.getHeight());
		setOpaque(false);
		
		observers = new CopyOnWriteArrayList<ClickableObserver>();
		
		if(clickable){
			setMouseListener();
			makeActionListener();
		}
		if(borders)
			addBorder();
	}
	
	/**
	 * Constructs an empty JPanel container with the specified dimensions. Mouse click events
	 *  and borders are optional.
	 * @param width An int indicating the JPanel's width, in pixels.
	 * @param height An int indicating the JPanel's height, in pixels.
	 * @param clickable A boolean. If true, this panel can track mouse events and will flash its borders
	 *  if it is clicked. Additionally, the panel's borders will change color when the mouse enters the
	 *  panel's borders. Otherwise, this panel will not respond to mouse events.
	 * @param borders A boolean. If true, this panel will have a raised-beveled boarder. Otherwise, this
	 *  panel will not have any borders.
	 */
	public ContentPane(int width, int height, boolean clickable, boolean borders){
		background = null;
		setLayout(null);
		setSize(width, height);
		setOpaque(false);
		
		observers = new CopyOnWriteArrayList<ClickableObserver>();
		
		if(clickable){
			setMouseListener();
			makeActionListener();
		}
		if(borders)
			addBorder();
	}
	
	/**
	 * Constructs a JPanel container that uses a BufferedImage background. The panel's dimensions
	 *  are equal to the dimensions of the background image. Mouse click events and borders are optional.
	 * @param backgroundImage The image that is to be used as this panel's background. Also used to define
	 *  the panel's dimensions.
	 * @param clickable A boolean. If true, this panel will track mouse events and will flash its borders
	 *  if it is clicked. Additionally, the panel's borders will change color when the mouse enters the
	 *  panel's borders. If false, this panel will not respond to mouse events, unless selectable == true.
	 * @param selectable A boolean. If true, this panel will track mouse events. The panel's borders will
	 *  change color when the mouse enters or leaves the panel. If the panel is clicked, the borders will
	 *  become green, and isSelected() will return true. If the panel is selected again, the borders will
	 *  be removed and resume the mouse enter/exit behavior. Additionally, isSelected() will be false.
	 *  Subsequent click events toggle the above behavior.
	 *  NOTE: If selectable == true, then the clickable behavior will be overriden, regardless of its
	 *   boolean value.
	 * @param borders A boolean. If true, this panel will have a raised-beveled boarder. Otherwise, this
	 *  panel will not have any borders.
	 */
	public ContentPane(BufferedImage backgroundImage, boolean clickable, boolean selectable, boolean borders){
		background   = backgroundImage;
		hasBorders   = borders;
		isSelectable = selectable;
		
		setLayout(null);
		setSize(background.getWidth(), background.getHeight());
		setOpaque(false);
		
		observers = new CopyOnWriteArrayList<ClickableObserver>();
		
		//If it's selectable, it's not clickable. Don't set the border flash timer.
		if(selectable){
			setMouseListener();
		//If it's not selectable, but is clickable treat like the (image, clickable, borders) constructor.
		}else if(clickable){
			setMouseListener();
			makeActionListener();
		}
		if(borders)
			addBorder();
	}
	
	
/*****************
 * Drawing Tools *
 *****************/
	
	/**
	 * Used to add a JComponent to this JPanel. The component's layout is specified by the passed parameters.
	 * @param component A JComponent to be added to this JPanel.
	 * @param xOrigin An int representing the x-origin of the JComponent to be added,
	 *  relative to this JPanel's origin.
	 * @param yOrigin An int representing the y-origin of the JComponent to be added,
	 *  relative to this JPanel's origin.
	 */
	public void addComponent(JComponent component, int xOrigin, int yOrigin){
		component.setBounds(xOrigin, yOrigin, component.getPreferredSize().width, component.getPreferredSize().height);
		add(component);
	}
	
	/**
	 * Used to change this JPanel's background image. This function forces this JPanel to repaint().
	 * WARNING: Changing the background's image will change this JPanel's dimensions to be
	 *  equal to the that of the new image's dimensions.
	 * @param backgroundImage The BufferedImage that is to be used as this ContentPane's new background image.
	 */
	public void changeBackground(BufferedImage backgroundImage){
		background = backgroundImage;
		repaint();
	}
	
	/**
	 * A convenience method used to obtain a BufferedImage. The BufferedImage can then be used
	 *  to change this JPanel's background, using changeBackground(BufferedImage).
	 * @param path A String describing the image file's path from the program's root directory.
	 * @return A BufferedImage of the specified image file.
	 * @throws IOException {@link project.tools.ImageLoader See the ImageLoader class for details.}
	 */
	public BufferedImage getImageFromPath(String path) throws IOException{
		return ImageLoader.getBufferedImage(path);
	}
	
	/**
	 * Used to force this JPanel to re-paint its components. This is often required when changing or
	 *  removing components from the JPanel. This method should not be used in rapid succession, as
	 *  the requests may be coalesced. Instead, make all necessary changes to the panel then call
	 *  reDraw(); as opposed to calling this method after every change.
	 */
	public void reDraw(){
		revalidate();
		repaint();
	}
	
	
/******************
 * Mouse Listener *
 ******************/
	
	/**
	 * Used to add a mouse listener to this JPanel and handle associated events. Notifies observers if
	 *  this JPanel is clicked. If "clickable" is set to false in the JPanel's constructor, this method
	 *  will never be invoked.
	 */
	private void setMouseListener(){
		addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent event) {
            	highlightBorder();
            }
            public void mouseExited(MouseEvent event) {
            	if(isSelected){
            		selectedBorder();
            	}else if(hasBorders)
            		addBorder();
            	else
            		removeBorder();
            }
            public void mousePressed(MouseEvent event) {
            	if(isSelectable)
            		toggleSelection();
            	else if(!isBorderFlashing)
            		flashBorder();
            	else
            		removeBorder();
            	notifiyObserver();
            }
        });
	}
	
	
/*******************
 * Selection Tools *	
 *******************/

	/**
	 * This method is used toggle this ContentPane's selection status. If the panel is selected,
	 *  the method wil make the panel un-selected, and vice-versa. It is not recommended to use
	 *  this method outside of this class, but is left available in case there are un-foreseen 
	 *  uses. NOTE: This method will have no effect if this ContentPane was not created using the 
	 *  ContentPane(backgroundImage, clickable, selectable, borders) constructor, or if
	 *  selectable == false.
	 */
	public void toggleSelection(){
		//This is not a selectable ContentPane. Do nothing.
		if(!isSelectable)
			return;
		//The panel is being de-selected. Return the beveled borders.
		if(isSelected && hasBorders){
			removeBorder();
			addBorder();
			isSelected = false;
		//The panel is being de-selected, but borders == false.
		}else if(isSelected){
			removeBorder();
			isSelected = false;
		//The panel is being selected. Make the borders green.
		}else{
			selectedBorder();
			isSelected = true;
		}
	}
	
	/**
	 * Used to determine if this ContentPane is selected. NOTE: This method will always return
	 *  false if this ContentPane was not created using the 
	 *  ContentPane(backgroundImage, clickable, selectable, borders) constructor, or if
	 *  selectable == false.
	 * @return A boolean indicating true if this ContentPane is selected; false otherwise.
	 */
	public boolean isSelected(){
		return isSelected;
	}
	
	
/****************
 * Border Tools *
 ****************/
	
	/**
	 * Used to add/changeTo a raised-beveled border around this JPanel, regardless of constructor's "borders" value.
	 */
	public void addBorder(){
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.GRAY, Color.DARK_GRAY));
	}
	
	/**
	 * Used to remove all borders around this JPanel, regardless of constructor's "borders" value.
	 */
	public void removeBorder(){
		setBorder(BorderFactory.createEmptyBorder());
	}
	
	/**
	 * A helper method for "mouseEntered" and "mouseClicked" events.
	 * Used to add/changeTo a yellow border around this JPanel, regardless of constructor's "borders" value.
	 */
	private void highlightBorder(){
		setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
	}
	
	/**
	 * A helper method used to change this ContentPane's borders to a green color. Such coloring is
	 *  used to indicate that the ContentPane is selected, and isSelected == true (but is not enforced
	 *  here). This method overrides any existing border value.
	 */
	private void selectedBorder(){
		setBorder(BorderFactory.createLineBorder(Color.GREEN, 7));
	}
	
	/**
	 * Used in conjunction with "mouseClicked" events.
	 * Used to flash this JPanel's outer border twice (from yellow to white to yellow x2).
	 */
	private void flashBorder(){
		isBorderFlashing = true;
		setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
		timer = new Timer(100, flashBorder);
		timer.setRepeats(true);
		timer.start();
	}
	
	/**
	 * A helper method for the flashBorder() method. Creates a ActionListener that is triggered
	 *  when Timer events call actionPerformed(ActionEvent).
	 */
	private void makeActionListener(){
		flashBorder      = new ActionListener() {

		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	//The border is white, change it to yellow.
		        if(borderToggled){
		        	highlightBorder();
		        	borderToggled = false;
		        	flashCounter++;
		        }
		        //The border is yellow, change it to white.
		        else{
		        	setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
		        	borderToggled = true;
		        }
		        //Stop flashing after two flashes.
		        if(flashCounter >= 2){
		        	timer.stop();
		        	isBorderFlashing = false;
		        	borderToggled    = true;
		        	flashCounter     = 0;
		        	removeBorder();
		        }
		    }
		};
	}
	
	
/*************
 * Overrides *
 *************/
	
	/**
	 * Used to set and get the dimensions of this JPanel. Overrides JComponent's getPreferedSize method.
	 *  This method is required for this JPanel to be drawn.
	 */
	@Override
    public Dimension getPreferredSize() {
        return background == null ? super.getPreferredSize() : new Dimension(background.getWidth(), background.getHeight());
    }
	
	/**
	 * Used to draw this JPanel's background image. Do not call this method directly. Instead use reDraw().
	 *  This method is required by JComponent for this JPanel to be drawn.
	 */
	@Override
    public void paintComponent(Graphics g) {
		super.paintComponent(g);

        if(background != null) {
            int x = (getWidth()  - background.getWidth()) / 2;
            int y = (getHeight() - background.getHeight()) / 2;
            g.drawImage(background, x, y, this);
        }
    }

	/**
	 * Registers ClickableObservers that wish to be notified when this JPanel is clicked.
	 * NOTE: This JPanel must have been constructed using "clickable = true".
	 */
	@Override
	public void registerObserver(ClickableObserver obs) {
		observers.add(obs);
	}
	
	/**
	 * Removes a registered ClickableObserver that was added using registerObserver().
	 * NOTE: This JPanel must have been constructed using "clickable = true".
	 */
	@Override
	public void removeObserver(ClickableObserver obs) {
		int index = observers.indexOf(obs);
		if(index >= 0)
			observers.remove(index);
	}

	/**
	 * Notifies ClickableObservers that this JPanel has been clicked.
	 * NOTE: This JPanel must have been constructed using "clickable = true".
	 */
	@Override
	public void notifiyObserver() {
		for(ClickableObserver obs:observers)
			obs.clicked(this);
	}
}
