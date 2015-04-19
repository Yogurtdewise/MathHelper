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
package project.interfaces;

/**
 * This interface is used by classes that wish to be notified when a Testable class receives
 *  an answer from a user, or the "Home" button has been clicked.. It guarantees methods that
 *  allow the TestableObserver to take action on the specified event.
 * @author Kenneth Chin
 */
public interface TestableObserver {

	/**
	 * An action that is performed when a Testable object has an answer to report.
	 * @param answer A String indicating a user's answer.
	 */
	public void answered(String answer);
	
	/**
	 * An action that is performed when a Testable object's "Next" button is clicked. Used by
	 *  practice tests when displaying an answer. Formal (graded) tests must implement this
	 *  method, but should do nothing.
	 */
	public void nextClicked();
	
	/**
	 * An action that is performed when an observed Testable's "Home" button has been clicked.
	 */
	public void homeClicked();
}
