/**
 * Name:         Math Helper
 * Version:      0.11.3
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

import project.constants.DifficultyLevel;
import project.screens.ModuleSelectScreen;
import project.tools.ContentPane;

/**
 * This interface is used by enums that describe button objects. The purpose is
 *  to provide a facade that allows for button modularity between objects that
 *  require a set of pre-made buttons.
 * Enums that implement this interface are often part of a class that implements
 *  EnumerableButtonFactory. This allows greater access to the buttons described
 *  in a ModuleSelectButtonInterface enum.
 * @author Kenneth Chin
 */
public interface ModuleSelectButtonInterface {
	/**
	 * Used to retrieve the button's name.
	 * @return A String indicating a button's name.
	 */
	public String getName();
	
	/**
	 * Used to obtain the button's image file name.
	 * @return A String indicating the button's image file name.
	 */
	public String getFileName();
	
	/**
	 * Used to obtain a default x-origin(relative to MainWindow) for this button object.
	 * @return An int object describing an x-origin(relative to MainWindow) for this button object.
	 */
	public int getX();
	
	/**
	 * Used to obtain a default y-origin(relative to MainWindow) for this button object.
	 * @return An int object describing a y-origin(relative to MainWindow) for this button object.
	 */
	public int getY();
	
	/**
	 * Used to obtain the ContentPane(JPanel) object for this button.
	 * @return The ContentPane(JPanel) object for this button.
	 */
	public ContentPane getButton();
	
	/**
	 * Used to obtain this ModuleSelectButtonInterface's enum ordinal value.
	 * @return An int representing this ModuleSelectButtonInterface's enum ordinal value.
	 */
	public int getOrdinal();
	
	/**
	 * Used to invoke whatever action that is associated with this button.
	 * This method is often called by a ClickableObserver's clicked(JComponent)
	 *  method.
	 */
	public abstract void doAction(ModuleSelectScreen screen);
	
	/**
	 * Used by DifficultySelectScreen to notify this ModuleSelectButtonInterface that a DifficultyLevel
	 *  has been chosen by the user. In short, this is a listener that is called by DifficultySelectScreen.
	 *  ModuleSelectButtonInterfaces that do not use a DifficultySelectScreen should implement this as a
	 *  "do nothing" method.
	 * @param level The DifficultyLevel that was selected by the user.
	 */
	public abstract void difficultySelected(DifficultyLevel level);
}
