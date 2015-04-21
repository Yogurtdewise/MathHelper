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
package project.run;

/**
 * Starts the Swing Event Dispatch Thread and adds the primary GUI controller to the queue.
 * @author Kenneth Chin
 *
 */
public class WindowTester {
	
	/**
	 * Run the program.
	 * @param args Should be null. No arguments are processed.
	 */
	public static void main(String[] args){
		GUIManager.runLaunch();

	}
}
