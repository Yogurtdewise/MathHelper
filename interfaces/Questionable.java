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

import java.io.IOException;

/**
 * This interface is implemented by classes that can display a "Test" question to the user. Its methods
 *  are used QuestionableObserver objects to display a single question. Classes that want to know when
 *  an answer is received by this Questionable should implement QuestionableObserver.
 * @author Kenneth Chin
 */
public interface Questionable {
	
	/**
	 * Used to display a single "Test" question.
	 * @param questionNum An int indicating displayed question's number out of a set of questions.
	 * @throws IOException Thrown if any image or audio file of this Questionable can not be read.
	 */
	public void showQuestion(int questionNum) throws IOException;
	
	/**
	 * Used to obtain the HARD_MAX_QUESTIONS constant of a test.
	 * This number should not be exceeded, as it represents the maximum
	 *  permutations of the test. To do otherwise would cause an infinite
	 *  loop, as the test searches for an unused question.
	 * @return An integer representing the maximum permutations allowed for
	 *  a given test.
	 */
	public int getMaxQuestions();
	
}
