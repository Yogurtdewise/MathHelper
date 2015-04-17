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
package project.database;

import java.util.HashMap;

import project.buttons.Grade1ModuleSelectTestButtons;
import project.buttons.Grade3ModuleSelectTestButtons;
import project.buttons.PreKModuleSelectTestButtons;
import project.constants.DifficultyLevel;
import project.interfaces.ModuleSelectButtonInterface;

/**
 * This class is a convenience class used to obtain read, alter, or initialize a HashMap that
 *  may be used to track a user's grades. More specifically, it is used to "bridge the gap"
 *  between the program's database & its implementation.
 * The public methods of this class can be used to get an initialized HashMap for a specified
 *  grade level, or get/set a particular test's grades.
 * @author Kenneth Chin
 */
public final class ReportCard {

	/**
	 * Private constructor prevents instantiation.
	 */
	private ReportCard(){}
	
	/**
	 * Used to obtain a new HashMap<ModuleSelectButtonInterface, ModuleReportSummary> for the
	 *  selected gradeLevel. Each "test" available to the specified gradeLevel will ensure that
	 *  its ModuleReportSummary will indicate that no tests have been taken.
	 * Throws an IndexOutOfBoundsException if gradeLevel is not 0, 1, or 2.
	 *  
	 *Specifics on the state of the returned object:
	 * The HashMap will have a size equal to the number of tests available to the
	 *  specified gradeLevel. The number of tests available is specified by the number of enums
	 *  in the gradeLevel's EnumerableButtonFactory's ModuleSelectButtonInterface object. Each
	 *  key value is an element in the aforementioned ModuleSelectButtonInterface object. Each
	 *  ModuleReportSummary is initialized with its grade and maxGrade set to -1. This setting
	 *  ensures that the all ModuleReportSummary classes will indicate that no tests have been taken.
	 * @param gradeLevel An int indicating the grade level for this HashMap. This value must be
	 *  "0" for the PreK-K grades, "1" for the 1-2 grades, or "2" for the 3-4 grades. Any other
	 *  value will cause an IndexOutOfBoundsException.
	 * @return A newly initialized HashMap<ModuleSelectButtonInterface, ModuleReportSummary>
	 *  for the specified gradeLevel. See the method details for specific state of the returned
	 *  object.
	 */
	public static HashMap<ModuleSelectButtonInterface, ModuleReportSummary> getHashMap(int gradeLevel){
		HashMap<ModuleSelectButtonInterface, ModuleReportSummary> map
						= new HashMap<ModuleSelectButtonInterface, ModuleReportSummary>();
		if(gradeLevel > 2 || gradeLevel < 0)
			throw new IndexOutOfBoundsException("The gradeLevel \"" + gradeLevel + "\" does not exist.");
		ModuleSelectButtonInterface[] tests = getTests(gradeLevel);
		for(int i = 0; i < tests.length; i++){
			map.put(tests[i], new ModuleReportSummary());
		}
		return map;
	}
	
	/**
	 * A helper method used to determine which set of ModuleSelectButtonInterface objects
	 *  define the tests for the specified gradeLevel.
	 * @param gradeLevel An int indicating the grade level that will identify the set of
	 *  ModuleSelectButtonInterface objects to be used. This value must be "0" for the
	 *  PreK-K grades, "1" for the 1-2 grades, or "2" for the 3-4 grades.
	 * @return An array of ModuleSelectButtonInterface objects who's elements are defined
	 *  by the gradeLevel's EnumeratedButtonInterface's ModuleSelectButtonInterface enum.
	 */
	private static ModuleSelectButtonInterface[] getTests(int gradeLevel){
		switch(gradeLevel){
			case 0:  return PreKModuleSelectTestButtons.Button.values();
			case 1:  return Grade1ModuleSelectTestButtons.Button.values();
			case 2:  return Grade3ModuleSelectTestButtons.Button.values();
			default: return PreKModuleSelectTestButtons.Button.values();
		}
	}
	
	/**
	 * Used to set the grade and maximum achievable grade for the specified test and difficulty level.
	 * @param reportMap A HashMap<ModuleSelectButtonInterface, ModuleReportSummary> who's
	 *  ModuleSelectButtonInterface key values describe a test, and who's ModuleReportSummary value
	 *  properties describe the grades for each key.
	 * @param test A ModuleSelectButtonInterface that describes the test who's grades are to be changed.
	 * @param grade An int describing the grade to be assigned to this test.
	 * @param maxGrade An int describing the maximum achievable test grade for the specified test.
	 * @param difficulty A DifficultyLevel object that describes the test's difficulty level who's grade
	 *  is to be set.
	 * @return A HashMap<ModuleSelectButtonInterface, ModuleReportSummary> who's ModuleReportSummary for
	 *  the specified "test" & "difficulty" has been modified to reflect "grade" and "maxGrade".
	 */
	public static HashMap<ModuleSelectButtonInterface, ModuleReportSummary> setGrade
				(HashMap<ModuleSelectButtonInterface, ModuleReportSummary> reportMap,
				ModuleSelectButtonInterface test, int grade, int maxGrade, DifficultyLevel difficulty){
		ModuleReportSummary summary = reportMap.get(test);
		if(difficulty == DifficultyLevel.EASY){
			summary.setEasyGrade(grade);
			summary.setEasyMax(maxGrade);
		}else if(difficulty == DifficultyLevel.NORMAL){
			summary.setNormalGrade(grade);
			summary.setNormalMax(maxGrade);
		}else if(difficulty == DifficultyLevel.HARD){
			summary.setHardGrade(grade);
			summary.setHardMax(maxGrade);
		}else
			return reportMap;
		return reportMap;
	}
	
	/**
	 * Used to obtain the grade for the specified test & difficulty level.
	 * @param reportMap A HashMap<ModuleSelectButtonInterface, ModuleReportSummary> who's
	 *  ModuleSelectButtonInterface key values describe a test, and who's ModuleReportSummary value
	 *  properties describe the grades for each key.
	 * @param test A ModuleSelectButtonInterface that describes the test who's grades are to be retrieved.
	 * @param difficulty A DifficultyLevel object that describes the test's difficulty level who's grade
	 *  is to be retrieved.
	 * @return An int indicating the grade for the specified "test" & "difficulty".
	 */
	public static int getGrade(HashMap<ModuleSelectButtonInterface, ModuleReportSummary> reportMap,
			ModuleSelectButtonInterface test, DifficultyLevel difficulty){
		ModuleReportSummary summary = reportMap.get(test);
		if(difficulty == DifficultyLevel.EASY)
			return summary.getEasyGrade();
		else if(difficulty == DifficultyLevel.NORMAL)
			return summary.getNormalGrade();
		else if(difficulty == DifficultyLevel.HARD)
			return summary.getHardGrade();
		else
			return -1;
	}
}
