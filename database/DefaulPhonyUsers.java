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
package project.database;
/*
 * This class auto populates database with the fake users
 * @author Elena Eroshkina
 */
import java.util.HashMap;

import project.buttons.Grade1ModuleSelectTestButtons;
import project.buttons.Grade3ModuleSelectTestButtons;
import project.interfaces.ModuleSelectButtonInterface;


public class DefaulPhonyUsers {
	private UserDatabase database;
//Constructor for user database 
	DefaulPhonyUsers(UserDatabase database){
	this.database = database;
	}
	//this class adds all users information into the database
	void populatePhonyUsers(){
		createPhonyJohn();
		createPhonyJane();
		createPhonyIssac();
	}
	
	//creating fake user John
	private void createPhonyJohn(){
		String userName   = "Johnny";
		String password   = "appleseed";
		String firstName  = "John";
		String lastName   = "Smith";
		int    gradeLevel = 0;
		int    lastTest   = 9;
		
		//fake users password set to "password" for testing purposes
		database.addUser(userName, password, firstName, lastName, gradeLevel);
		database.setLastActiveTest(userName, lastTest);
	}
	
	//creating fake user Jane
	private void createPhonyJane(){
		String userName   = "Janie";
		String password   = "gottagun";
		String firstName  = "Jane";
		String lastName   = "Doe";
		int    gradeLevel = 1;
		int    lastTest   = 1;
		
		//this will create a fake report card for user Jane
		HashMap<ModuleSelectButtonInterface, ModuleReportSummary> reportMap = 
				new HashMap<ModuleSelectButtonInterface, ModuleReportSummary>();
		reportMap.put(Grade1ModuleSelectTestButtons.Button.EXPANSION,  new ModuleReportSummary(10, 10,  9, 10, -1, 10));
		reportMap.put(Grade1ModuleSelectTestButtons.Button.MEASURE,    new ModuleReportSummary(-1, 10, -1, 10, -1, 10));
		reportMap.put(Grade1ModuleSelectTestButtons.Button.FRACTIONS,  new ModuleReportSummary(-1, 10, -1, 10, -1, 10));
		reportMap.put(Grade1ModuleSelectTestButtons.Button.COMPARISON, new ModuleReportSummary(-1, 10, -1, 10, -1, 10));
		reportMap.put(Grade1ModuleSelectTestButtons.Button.ODD_EVEN,   new ModuleReportSummary(-1, 10, -1, 10, -1, 10));
		reportMap.put(Grade1ModuleSelectTestButtons.Button.MONEY,      new ModuleReportSummary(-1, 10, -1, 10, -1, 10));
		reportMap.put(Grade1ModuleSelectTestButtons.Button.WORD_PROB,  new ModuleReportSummary(-1, 10, -1, 10, -1, 10));
		reportMap.put(Grade1ModuleSelectTestButtons.Button.ARITHMETIC, new ModuleReportSummary(-1, 10, -1, 10, -1, 10));
		reportMap.put(Grade1ModuleSelectTestButtons.Button.ESTIMATION, new ModuleReportSummary(-1, 10, -1, 10, -1, 10));
		reportMap.put(Grade1ModuleSelectTestButtons.Button.FINAL,      new ModuleReportSummary(-1, 10, -1, 10, -1, 10));
		

		database.addUser(userName, password, firstName, lastName, gradeLevel, reportMap);
	}
	
	// creating fake user Isaac
	private void createPhonyIssac(){
		String userName   = "TheApple";
		String password   = "hitme";
		String firstName  = "Issac";
		String lastName   = "Newton";
		int    gradeLevel = 2;
		int    lastTest   = 1;
		
		//fake report card for user Issaac 
		HashMap<ModuleSelectButtonInterface, ModuleReportSummary> reportMap = 
				new HashMap<ModuleSelectButtonInterface, ModuleReportSummary>();
		reportMap.put(Grade3ModuleSelectTestButtons.Button.COMPARISON, new ModuleReportSummary(10, 10, 10, 10, 10, 10));
		reportMap.put(Grade3ModuleSelectTestButtons.Button.EXPANSION,  new ModuleReportSummary(-1, 10, -1, 10, -1, 10));
		reportMap.put(Grade3ModuleSelectTestButtons.Button.FRACTIONS,  new ModuleReportSummary(-1, 10, -1, 10, -1, 10));
		reportMap.put(Grade3ModuleSelectTestButtons.Button.DECIMALS,   new ModuleReportSummary(-1, 10, -1, 10, -1, 10));
		reportMap.put(Grade3ModuleSelectTestButtons.Button.NUM_CLASS,  new ModuleReportSummary(-1, 10, -1, 10, -1, 10));
		reportMap.put(Grade3ModuleSelectTestButtons.Button.WORD_PROB,  new ModuleReportSummary(-1, 10, -1, 10, -1, 10));
		reportMap.put(Grade3ModuleSelectTestButtons.Button.PROPERTIES, new ModuleReportSummary(-1, 10, -1, 10, -1, 10));
		reportMap.put(Grade3ModuleSelectTestButtons.Button.RELATIONS,  new ModuleReportSummary(-1, 10, -1, 10, -1, 10));
		reportMap.put(Grade3ModuleSelectTestButtons.Button.ARITHMETIC, new ModuleReportSummary(-1, 10, -1, 10, -1, 10));
		reportMap.put(Grade3ModuleSelectTestButtons.Button.ESTIMATE,   new ModuleReportSummary(-1, 10, -1, 10, -1, 10));
		reportMap.put(Grade3ModuleSelectTestButtons.Button.FINAL,      new ModuleReportSummary(-1, 10, -1, 10, -1, 10));
		
		database.addUser(userName, password, firstName, lastName, gradeLevel, reportMap);

	}
}
