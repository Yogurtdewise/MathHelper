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
package project.screens;

import java.awt.image.BufferedImage;
import java.io.IOException;

import project.run.GUIManager;
import project.tools.ImageLoader;
import project.tools.MainWindow;

/**
 * This abstract class is used as an Abstract Factory for any concrete module selection
 *  screens. It provides default settings and getters that all ModuleSelectScreens
 *  have in common. This implementation allows for global modification to all
 *  ModuleSelectScreens, while also providing a common facade that all module
 *  selection screens inherit.
 * @author Kenneth Chin
 */
public abstract class ModuleSelectScreen{
	
	/**
	 * The default index of the first button to be displayed.
	 */
	public static final int DEFAULT_FIRST_BUTTON_INDEX = 0;
	
	/**
	 * The maximum number of module buttons to show on a ModuleSelectScreen's page.
	 * NOTE: This is a quantity, NOT an index increment.
	 */
	public static final int MAX_BUTTONS_SHOWING        = 4;
	
	/**
	 * The default layer to which buttons are added to a MainWindow.
	 */
	public static final int BUTTON_LAYER               = 3;
	
	/**
	 * The default layer to which text is added to a MainWindow.
	 */
	public static final int TEXT_LAYER                 = 2;
	
	//The file path of the default background image from the program's root directory.
	private static final String backgroundImagePath = "\\images\\moduleSelect\\Background.png";
	
	private GUIManager manager;         //The GUIManager that manages mainWindow & all GUI screens.
	private MainWindow mainWindow;      //The MainWindow to which ModuleSelectScreen components are added to.
	
	/**
	 * Constructs an abstract ModuleSelectScreen, and changes the MainWindow's background image.
	 * @param manager The GUIManager that manages the primary MainWindow & all GUI screens.
	 * @throws IOException Thrown if the file described by backgroundImagePath can not be read.
	 */
	public ModuleSelectScreen(GUIManager manager) throws IOException{
		this.manager    = manager;
		this.mainWindow = manager.getMainWindow();
		if(mainWindow == null)
			throw new NullPointerException("ModuleSelectScreen recieved a null mainWindow.");
		setBackground();
	}
	
	/**
	 * Used to obtain the GUIManager that manages the MainWindow that ModuleSelectScreen components are added to.
	 * @return The GUIManager that manages the MainWindow that ModuleSelectScreen components are added to.
	 */
	public GUIManager getManager(){
		return manager;
	}
	
	/**
	 * Used to obtain the MainWindow to which ModuleSelectScreen components are added to.
	 * @return The MainWindow to which ModuleSelectScreen components are added to.
	 */
	public MainWindow getMainWindow(){
		return mainWindow;
	}

	/**
	 * Used to set MainWindow's background image to ModuleSelectScreen default background.
	 * @throws IOException Thrown if the file described by backgroundImagePath can not be read.
	 */
	private void setBackground() throws IOException{
		BufferedImage backgroundBufferedImage = ImageLoader.getBufferedImage(backgroundImagePath);
		mainWindow.setBackgroundImage(backgroundBufferedImage);
	}
	
	/**
	 * Used to remove all ModuleSelectScreen components from the MainWindow.
	 */
	public abstract void tearDown();
}
