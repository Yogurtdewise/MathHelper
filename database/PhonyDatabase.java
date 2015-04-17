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

import java.io.Serializable;
import java.util.HashMap;

import project.buttons.Grade1ModuleSelectTestButtons;
import project.buttons.Grade3ModuleSelectTestButtons;
import project.interfaces.ModuleSelectButtonInterface;

/**
 * TODO Replace this class.
 * @author Kenneth Chin
 */
public class PhonyDatabase implements Serializable{

	private HashMap<String, PhonyEntry> database;
	
	public PhonyDatabase(){
		database = new HashMap<String, PhonyEntry>();
		populatePhonyUsers();
	}
	
	private void populatePhonyUsers(){
		createPhonyJohn();
		createPhonyJane();
		createPhonyIssac();
	}
	
	private void createPhonyJohn(){
		String userName   = "Johnny";
		String firstName  = "John";
		String lastName   = "Smith";
		int    gradeLevel = 0;
		int    lastTest   = 7;
		
		HashMap<ModuleSelectButtonInterface, ModuleReportSummary> reportMap = ReportCard.getHashMap(gradeLevel);
		
		PhonyEntry entry = new PhonyEntry();
		entry.setFirstName(firstName);
		entry.setLastName(lastName);
		entry.setGradeLevel(gradeLevel);
		entry.setLastActiveTest(lastTest);
		entry.setReportCard(reportMap);
		
		database.put(userName, entry);
	}
	
	private void createPhonyJane(){
		String userName   = "Janie";
		String firstName  = "Jane";
		String lastName   = "Doe";
		int    gradeLevel = 1;
		int    lastTest   = 1;
		
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
		
		PhonyEntry entry = new PhonyEntry();
		entry.setFirstName(firstName);
		entry.setLastName(lastName);
		entry.setGradeLevel(gradeLevel);
		entry.setLastActiveTest(lastTest);
		entry.setReportCard(reportMap);
		
		database.put(userName, entry);
	}
	
	private void createPhonyIssac(){
		String userName   = "TheApple";
		String firstName  = "Issac";
		String lastName   = "Newton";
		int    gradeLevel = 2;
		int    lastTest   = 1;
		
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
		
		PhonyEntry entry = new PhonyEntry();
		entry.setFirstName(firstName);
		entry.setLastName(lastName);
		entry.setGradeLevel(gradeLevel);
		entry.setLastActiveTest(lastTest);
		entry.setReportCard(reportMap);
		
		database.put(userName, entry);
	}
	
	public String getPassword(String userName){
		return database.get(userName).getPassword();
	}
	
	public String getFirstName(String userName){
		return database.get(userName).getFirstName();
	}
	
	public String getLastName(String userName){
		return database.get(userName).getLastName();
	}
	
	public int getGradeLevel(String userName){
		return database.get(userName).getGradeLevel();
	}
	
	public int getLastActiveTest(String userName){
		return database.get(userName).getLastActiveTest();
	}
	
	public HashMap<ModuleSelectButtonInterface, ModuleReportSummary> getReportCard(String userName){
		return database.get(userName).getReportCard();
	}
	
	public void setPassword(String userName, String password){
		database.get(userName).setPassword(password);
	}
	
	public void setFirstName(String userName, String firstName){
		database.get(userName).setFirstName(firstName);
	}
	
	public void setLastName(String userName, String lastName){
		database.get(userName).setLastName(lastName);
	}
	
	public void setGradeLevel(String userName, int gradeLevel){
		database.get(userName).setGradeLevel(gradeLevel);
	}
	
	public void setLastActiveTest(String userName, int lastActiveTest){
		database.get(userName).setLastActiveTest(lastActiveTest);
	}
	
	public void setReportCard(String userName, HashMap<ModuleSelectButtonInterface, ModuleReportSummary> reportCard){
		database.get(userName).setReportCard(reportCard);
	}
}
