/**
 * Name:         Math Helper
 * Version:      0.11.2
 * Version Date: 04/20/2015
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

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * An uninstantiable class, used to obtain a BufferedImage based on the program's root directory
 *  and specified path.
 * Throws an IOException if the specified file can not be read.
 * @author Kenneth Chin
 *
 */
public final class ImageLoader {
	
	private static BufferedImage image; //The image that is to be created and returned.

	/**
	 * Private constructor prevents instantiation.
	 */
	private ImageLoader(){}
	
	/**
	 * Creates a BufferedImage, generated from the file found at the specified path.
	 * @param path A String describing the path of an image file, from the program's root directory.
	 * @throws IOException Thrown if the specified file can not be read.
	 */
	private static void createBufferedImage(String path) throws IOException{
        String parentDir = System.getProperty("user.dir");
    	String filePath  = parentDir + path;
    	image = ImageIO.read(new File(filePath));
	}
	
	/**
	 * Returns a BufferedImage, generated from the file found at the specified path.
	 * @param pathFromParent A String describing the path of an image file, from the program's root directory.
	 * @return A BufferedImage of the specified file.
	 * @throws IOException Thrown if the specified file can not be read.
	 */
	public static BufferedImage getBufferedImage(String pathFromParent) throws IOException{
		createBufferedImage(pathFromParent);
		return image;
	}
}
