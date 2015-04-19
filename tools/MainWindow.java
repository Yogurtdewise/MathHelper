/**
 * Name:         Math Helper
 * Version:      0.11.1
 * Version Date: 04/19/2015
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

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.UIManager;

import project.run.GUIManager;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the program's main window; the primary container for all GUI elements.
 *  The main window is a JFrame, who's content pane is a JLayeredPane. The main window's Layout
 *  is managed by a BorderLayout. A BufferedImage is set as the background image of the main
 *  window. This background image is contained on the "1" layer of the JLayeredPane. Additional
 *  convenience methods have been added to simplify the addition of components to this JFrame.
 * @author Kenneth Chin
 *
 */
public final class MainWindow extends JFrame{
	
	//Paths from the program's directory to the JFrame icons.
	private static final String SMALL_ICON_PATH = "\\images\\global\\icon1.png";
	private static final String BIG_ICON_PATH   = "\\images\\global\\icon2.png";
	
	private static final String TITLE_TEXT = "Math Helper"; //The text to display on the title bar.

	private Dimension windowSize;        //The display area plus left and top border size.
	private JLayeredPane contentPane;    //The content pane of this JFrame.
	private ContentPane backgroundPanel; //A JPanel used to display the background image.
	private BufferedImage defaultBackgroundImage; //A default image, set at instantiation.
	private BufferedImage currentBackgroundImage; //The current background image.
	
	private final Integer BG_DEPTH = new Integer(1); //The layer depth of the backgroundPanel.
	
	private static MainWindow mainWindow = null; //The singleton instance of MainWindow.
	
	private GUIManager manager; //The GUIManager that is managing MainWindow.
	
	/**
	 * A private constructor to prevent instantiation. Since MainWindow creates a JFrame to
	 *  be used as the primary window for the program, there will only be one of these.
	 * Creates a JFrame that uses a JLayeredPane as its content pane and a BorderLayout
	 *  as its LayoutManager. The window's dimensions are larger than the defined display
	 *  area, by a pixel size equal to its left(width) and top(height) borders. The defined
	 *  background image is displayed on layer "1". All other components should be added to
	 *  layers greater than "1".
	 * @param width An int representing the number of pixels in the display area's width.
	 * @param height An int representing the number of pixels in the display area's height.
	 * @param defaultBackgroundImage A BufferedImage used as this JFrame's background image.
	 * @param manager The GUIManager object that will manage this MainWindow.
	 */
	private MainWindow(GUIManager manager, int width, int height, BufferedImage defaultBackgroundImage){
		this.manager = manager;
		
		int windowWidth  = width + getInsets().left;
		int windowHeight = height + getInsets().top;
		
		//Cross-platform GUI management.
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			manager.handleException(e);;
		}
		
		//Build the main window and components.
		windowSize       = new Dimension(windowWidth, windowHeight);
		
		setPreferredSize(windowSize);
		setMinimumSize(windowSize);
		
		setIcons();
		setTitle(TITLE_TEXT);
		
		this.defaultBackgroundImage = defaultBackgroundImage;
		currentBackgroundImage      = defaultBackgroundImage;
		backgroundPanel = new ContentPane(defaultBackgroundImage, false, false);
		
		setLayout(new BorderLayout());
		
		backgroundPanel.setBounds(getInsets().left, getInsets().top, windowSize.width, windowSize.height);
		createLayeredPane();
		contentPane.add(backgroundPanel, BG_DEPTH, 0);
		add(contentPane);

		//Finish up and display the main window.
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}
	
	/**
	 * Returns the singleton instance of MainWindow. This is enforced to prevent more
	 *  than one JFrame from being created.
	 * Creates a JFrame that uses a JLayeredPane as its content pane and a BorderLayout
	 *  as its LayoutManager. The window's dimensions are larger than the defined display
	 *  area, by a pixel size equal to its left(width) and top(height) borders. The defined
	 *  background image is displayed on layer "1". All other components should be added to
	 *  layers greater than "1".
	 * @param width An int representing the number of pixels in the display area's width.
	 * @param height An int representing the number of pixels in the display area's height.
	 * @param defaultBackgroundImage A BufferedImage used as this JFrame's background image.
	 * @param manager The GUIManager object that will manage this MainWindow.
	 * @return The singleton instance of MainWindow.
	 */
	public static MainWindow getInstance(GUIManager manager, int width, int height, BufferedImage defaultBackgroundImage){
		if(mainWindow == null)
			mainWindow = new MainWindow(manager, width, height, defaultBackgroundImage);
		return mainWindow;
	}
	
	/**
	 * Used to obtain the GUIManager object that manages this MainWindow. This is used
	 *   to access settings or to access other classes that may alter the MainWindow.
	 * @return The GUIManager object that manages this MainWindow.
	 */
	public GUIManager getManager(){
		return manager;
	}
	
	/**
	 * A helper method, used to initialize and set MainWindow's icon images.
	 */
	private void setIcons(){
		//String filePath = System.getProperty("user.dir");
		final List<BufferedImage> icons = new ArrayList<BufferedImage>();
		try {
			icons.add(ImageLoader.getBufferedImage(SMALL_ICON_PATH));
			icons.add(ImageLoader.getBufferedImage(BIG_ICON_PATH));
		} catch (IOException e) {
			manager.handleException(e);
		}
		if(icons.size() > 0)
			setIconImages(icons);
	}
	
	/**
	 * A helper method used to create the JLayeredPane that manages layered components.
	 */
	private void createLayeredPane(){
		contentPane = new JLayeredPane();
		contentPane.setPreferredSize(windowSize);
		contentPane.setOpaque(true);
		contentPane.setVisible(true);
	}
	
	/**
	 * Used to change this JFrame's background image. It may be necessary to invoke .reDraw()
	 *  after changing the background image. This can be extremely time intensive, if there are
	 *  many components being displayed. If possible, do not use .reDraw(). Instead, wait for the
	 *  Event Dispatch Thread to update the JPanel automatically.
	 * NOTE: Changing the background image WILL NOT change the window size.
	 * @param backgroundImage
	 */
	public void setBackgroundImage(BufferedImage backgroundImage){
		currentBackgroundImage = backgroundImage;
		backgroundPanel.changeBackground(backgroundImage);
	}
	
	/**
	 * Used to obtain the BufferedImage used for this JFrame's background image.
	 * @return The BufferedImage used for this JFrame's background image.
	 */
	public BufferedImage getBackgroundImage(){
		return currentBackgroundImage;
	}
	
	/**
	 * Used to change the background back to the default background image.
	 */
	public void setBackgroundToDefault(){
		currentBackgroundImage = defaultBackgroundImage;
		backgroundPanel.changeBackground(defaultBackgroundImage);
	}
	
	/**
	 * Used to add a JComponent to the primary content pane. Specify the layer and origin relative
	 *  to MainWindow's origin. DO NOT change the contents of any layer less than or equal to 1.
	 * @param component A JComponent to add to the specified layer. It is recommended that the JComponent
	 *  is a JPanel, but may be any sub-class of JComponent.
	 * @param layer An int indicating the z-axis of the specified component. 0 is the bottom layer. DO NOT
	 *  change the contents of any layer less than or equal to 1, as they are reserved for this implementation.
	 * @param xOrigin An int indicating the origin of the specified container's x-axis, relative to MainWindow's origin.
	 * @param yOrigin An int indicating the origin of the specified container's y-axis, relative to MainWindow's origin.
	 */
	public void addLayer(JComponent component, int layer, int xOrigin, int yOrigin){
		Integer depth = new Integer(layer);
		if(depth.compareTo(BG_DEPTH) <= 0)
			depth = new Integer(BG_DEPTH + 1);
		component.setBounds(xOrigin, yOrigin, component.getPreferredSize().width, component.getPreferredSize().height);
		contentPane.add(component, depth);
		component.setOpaque(false);
		component.setVisible(true);
		contentPane.revalidate();
		contentPane.repaint();
	}
	
	/**
	 * This method is made available for convenience only. It is preferable to use MainWindow.addLayer().
	 *  Returns the JLayeredPane that is used as the primary content pane. This method does not override
	 *  the private method of JLayeredPane's parent, Window.
	 * NOTE: DO NOT change any layer less than or equal to 1.
	 * @return The JLayeredPane that is used for MainWindow's primary content pane.
	 */
	public JLayeredPane getContainer(){
		return contentPane;
	}
	
	/**
	 * Used to force the main window to re-paint its components. This is often required when changing or
	 *  removing components from the primary content pane. This method should not be used in rapid
	 *  succession, as the requests may be coalesced.
	 *  NOTE: To improve performance, the user should consider using the reDraw() method of the
	 *  ContentPane container, or the rePaint() method of the JComponent container, who's contents
	 *  contain the images they want to re-paint. This prevents MainWindow and all its children from
	 *  re-painting.
	 */
	protected void reDraw(){
		contentPane.revalidate();
		repaint();
	}
	
}
