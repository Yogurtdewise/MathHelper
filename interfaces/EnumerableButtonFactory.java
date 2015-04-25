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
package project.interfaces;

/**
 * This interface is used by classes that use an enum to iterate over their button objects.
 *  The purpose is to provide a facade that allow objects to iterate over all button lists.
 *  For constancy and modularity, any class that implements EnumerableButtonFactory must
 *  have its button enum implement ModuleSelectButtonInterface.
 * @author Kenneth Chin
 */
public interface EnumerableButtonFactory {
	/**
	 * An array of objects that implement ModuleSelectButtonInterface. Such objects
	 *  give access to JPanel objects and provide methods that describe how the JPanel
	 *  should be used.
	 * @return An array of ModuleSelectButtonInterface objects.
	 */
	public ModuleSelectButtonInterface[] getButtons();
	
	/**
	 * A convenience method that returns the number of ModuleSelectButtonInterface objects
	 *  described by a EnumerableButtonFactory.
	 * @return An int equal to getButtons().length.
	 */
	public int getNumberOfButtons();
	
	/**
	 * Used to obtain the String that is used as this EnumerableButtonFactory's title.
	 * @return The String that is used as this EnumerableButtonFactory's title.
	 */
	public String getTitleText();
}
