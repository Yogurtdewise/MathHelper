/**
 * Name:         Math Helper
 * Version:      0.11.4
 * Version Date: 04/24/2015
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
package project.buttons;


import java.awt.image.BufferedImage;
import java.io.IOException;

import project.tools.ContentPane;
import project.tools.ImageLoader;

/**
 * This class is a convenience class, used to define and create a "Home" button ContentPane.
 * @author Kenneth Chin
 */
public final class HomeButtonMaker{

	private static final String name     = "Home";
	private static final String filePath = "\\images\\global\\HomeBtn.png";
	private static final int    xOrigin  = 20;
	private static final int    yOrigin  = 640;
	
	/**
	 * Private constructor prevents instantiation.
	 */
	private HomeButtonMaker(){}
	
	/**
	 * Used to obtain a new "Home" button ContentPane object, who's .setName() is "Home", and
	 *  whom's "clickable" value is true.
	 * @return A ContentPane that has the "clickable" value set to true, and has the "Home"
	 *  button background image.
	 * @throws IOException Thrown if the button's image file can not be read.
	 */
	public static ContentPane getContentPane() throws IOException{
		BufferedImage image = ImageLoader.getBufferedImage(filePath);
		ContentPane   panel = new ContentPane(image, true, false); 
		panel.setName(name);
		return panel;
	}
	
	/**
	 * Used to obtain the x-origin for the "Home" button, as used by most screens in the project.
	 * @return An int indicating the x-origin used by most screens in the project.
	 */
	public static int getXDefault(){
		return xOrigin;
	}
	
	/**
	 * Used to obtain the y-origin for the "Home" button, as used by most screens in the project.
	 * @return An int indicating the y-origin used by most screens in the project.
	 */
	public static int getYDefault(){
		return yOrigin;
	}
}
