/**
 * Name:         Math Helper
 * Version:      0.10.0
 * Version Date: 04/16/2015
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
 * This enum is used to define Apple Board images. Such images are used as a visual representation
 *  of integers 0 through 10. The enum provides file paths from the program's root directory, and
 *  integer values for each image.
 * @author Kenneth Chin
 */
public enum AppleBoard {
	ZERO ("\\images\\test\\apples\\00AppleBoard.png",  0),
	ONE  ("\\images\\test\\apples\\01AppleBoard.png",  1),
	TWO  ("\\images\\test\\apples\\02AppleBoard.png",  2),
	THREE("\\images\\test\\apples\\03AppleBoard.png",  3),
	FOUR ("\\images\\test\\apples\\04AppleBoard.png",  4),
	FIVE ("\\images\\test\\apples\\05AppleBoard.png",  5),
	SIX  ("\\images\\test\\apples\\06AppleBoard.png",  6),
	SEVEN("\\images\\test\\apples\\07AppleBoard.png",  7),
	EIGHT("\\images\\test\\apples\\08AppleBoard.png",  8),
	NINE ("\\images\\test\\apples\\09AppleBoard.png",  9),
	TEN  ("\\images\\test\\apples\\10AppleBoard.png", 10);
	
	private String filePath;
	private int value;
	
	/**
	 * The private constructor for the AppleBoard enum.
	 * @param filePath This AppleBoard's file path from the program's root directory.
	 * @param value This AppleBoard's integer value.
	 */
	private AppleBoard(String filePath, int value){
		this.filePath = filePath;
		this.value = value;
	}
	
	/**
	 * Used to obtain this AppleBoard's file path from the program's root directory.
	 * @return A String describing this AppleBoard's file path from the program's root directory.
	 */
	public String getPath(){
		return filePath;
	}
	
	/**
	 * Used to obtain this AppleBoard's integer value.
	 * @return An int equal to this AppleBoard's integer value.
	 */
	public int getValue(){
		return value;
	}
}
