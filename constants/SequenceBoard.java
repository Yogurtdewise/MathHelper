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
package project.constants;

/**
 * This enum is used to define Sequence Board images. Such images are used as a visual representation
 *  of sequences first through fifth. The enum provides file paths from the program's root directory,
 *  and integer String names for each image.
 * @author Kenneth Chin
 */
public enum SequenceBoard {
	FIRST  ("\\images\\test\\sequences\\first.png" , "First"),
	SECOND ("\\images\\test\\sequences\\second.png", "Second"),
	THIRD  ("\\images\\test\\sequences\\third.png" , "Third"),
	FOURTH ("\\images\\test\\sequences\\fourth.png", "Fourth"),
	FIFTH  ("\\images\\test\\sequences\\fifth.png" , "Fifth");
	
	private String filePath;
	private String name;
	
	/**
	 * The private constructor for the SequenceBoard enum.
	 * @param filePath This AppleBoard's file path from the program's root directory.
	 * @param name The String name of this SequenceBoard.
	 */
	private SequenceBoard(String filePath, String name){
		this.filePath = filePath;
		this.name = name;
	}
	
	/**
	 * Used to obtain this SequenceBoard's file path from the program's root directory.
	 * @return A String describing this SequenceBoard's file path from the program's root directory.
	 */
	public String getPath(){
		return filePath;
	}
	
	/**
	 * Used to obtain a String describing what this SequenceBoard is representing.
	 * @return A String describing what this SequenceBoard is representing.
	 */
	public String getName(){
		return name;
	}
}
