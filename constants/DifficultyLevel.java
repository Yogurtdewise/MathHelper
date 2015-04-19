/**
 * Name:         Math Helper
 * Version:      0.11.0
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
package project.constants;

/**
 * An enum used to define DifficultyLevel constants & file names.
 * @author Kenneth Chin
 */
public enum DifficultyLevel{
	/**
	 * The "Easy" difficulty level.
	 */
	EASY("Easy", "Easy Button.png"),
	
	/**
	 * The "Normal" difficulty level.
	 */
	NORMAL("Normal", "Normal Button.png"),
	
	/**
	 * The "Hard" difficulty level.
	 */
	HARD("Hard", "Hard Button.png");
	
	private String name;
	private String filename;
	
	/**
	 * The private constructor for DifficultyLevel.
	 * @param name
	 * @param filename
	 */
	private DifficultyLevel(String name, String filename){
		this.name     = name;
		this.filename = filename;
	}
	
	/**
	 * Used to obtain this DifficultyLevel's name (description).
	 * @return A String describing this DifficultyLevel's name.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Used to obtain file name for this DifficultyLevel's button image.
	 * @return A String describing the file name for this DifficultyLevel's button image.
	 */
	public String getFilename(){
		return filename;
	}
}
