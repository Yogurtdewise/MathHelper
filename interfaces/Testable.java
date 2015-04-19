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
 * This interface is used by classes that can obtain an answer from the user and/or have a "Home"
 *  button clicked. TestableObservers that wish to be notified when a user enters an answer or
 *  clicks the "Home" button should register with the Testable, and implement the TestableObserver
 *  interface. The Testable interface guarantees methods that allow TestableObservers to
 *  register/un-register for notifications, and allows the Testable to notify all registered
 *  observers of a user answer or "Home" button events.
 * @author Kenneth Chin
 */
public interface Testable {
	/**
	 * Registers a TestableObserver to be notified if this Testable has an answer to report, or
	 *  its "Home" button has been clicked.
	 * @param obs The TestableObserver to be notified.
	 */
	public void registerObserver(TestableObserver obs);
	
	/**
	 * Removes a TestableObserver from the list of TestableObserver that are notified when this
	 *  Testable has an answer to report or its "Home" button has been clicked..
	 * @param obs The TestableObserver to be removed.
	 */
	public void removeObserver(TestableObserver obs);
	
	/**
	 * Calls the The TestableObserver.answered(String) method of all registered TestableObservers when
	 *  this Testable has an answer to report. Otherwise, calls the TestableObserver.homeClicked()
	 *  method of all registered TestableObservers when this Testable has had its "Home" button clicked.
	 * @param event A String used to differentiate between an "Answer" event or "Home" button event.
	 *  This parameter may be used for any event that a Testable might track, however the only guaranteed
	 *  actions are those specified in the method description.
	 */
	public void notifiyObserver(String event);
}
