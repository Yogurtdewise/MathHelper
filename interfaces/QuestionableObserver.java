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
package project.interfaces;

/**
 * This interface implemented by classes that want to know when a Questionable object has received
 *  an answer to its question. This is typically a "Final" or cumulative testing class that will use
 *  questions from Questionable test classes.
 * @author Kenneth Chin
 */
public interface QuestionableObserver {

	/**
	 * Used by classes that implement the Questionable interface to report that the user has answered
	 *  its question.
	 * @param object The Questionable object that is reporting that a user answered its question.
	 * @param correct A boolean indicating true if the user got the question correct; false otherwise.
	 * @param wrongAnswerLogEntry A String that is used as a log entry into the log of all incorrectly
	 *  answered questions. This entry should describe the question number, question asked, user's
	 *  answer, and expected correct answer. If the user got the answer correct, this String may be null.
	 */
	public void answered(Questionable object, boolean correct, String wrongAnswerLogEntry);
}
