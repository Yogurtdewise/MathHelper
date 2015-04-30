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

import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * This class is used to notify the user that the button they clicked is not finished.
 *  This is done by displaying a JOptionPane that closes when the "ok" button is clicked.
 * @author Kenneth Chin
 */
public final class UnfinishedButtonWarning {
	
	//The message shown in the JOptionPanel.
	private static String message = "<p>We're sorry, this button isn't finished.</p></br>"
									+ "<p>The operation you selected isn't complete.</p></br>"
									+ "<p>Please choose another option, or log in at "
									+ " a different grade level.</p>";
	
	/**
	 * Private constructor prevents instantiation.
	 */
	private UnfinishedButtonWarning(){}

	
	/**
	 * Used to notify the user that the button they clicked is not finished.
	 *  This is done by displaying a JOptionPane that closes when the "ok" button is clicked.
	 */
	public static void showWarning(){
		JLabel label = new JLabel("<HTML><div>" + message + "</div></HTML>", JLabel.CENTER);
		JOptionPane.showMessageDialog(null, label, "Under Construction...", JOptionPane.INFORMATION_MESSAGE);
	}
}
