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

/**This class implements Users Database
 * 
 * @author Elena Eroshkina
 */
public class UserDatabase implements Serializable{

	private HashMap<String, UserEntry> database;
	
	//initialization of DB. It will be populated with the values stored in Hash table
	public UserDatabase(){
		database = new HashMap<String, UserEntry>();
	DefaulPhonyUsers defaultUser = new DefaulPhonyUsers(this);  
		defaultUser.populatePhonyUsers();
	}
	//this method will add users to the database. It will pass all needed values 
	//to it such as user name, password, first name, last name and grade level. 
	//It will also pass the report of all taken tests
	public void addUser(String userName, 
			String password, 
			String firstName, 
			String lastName,
			int gradeLevel){
		UserEntry entry = new UserEntry();
		entry.setFirstName(firstName);
		entry.setPassword(password);
		entry.setLastName(lastName);
		entry.setGradeLevel(gradeLevel);
		entry.setLastActiveTest(1);
		entry.setReportCard(ReportCard.getHashMap(gradeLevel));
		
		//at this point system will save all needed information into the database
		database.put(userName, entry);
	};
	//method to add user
	public void addUser(String userName, 
			String password, 
			String firstName, 
			String lastName,
			int gradeLevel,
			HashMap<ModuleSelectButtonInterface, ModuleReportSummary> reportCard){
		//class that stores information about user
		UserEntry entry = new UserEntry();
		entry.setFirstName(firstName);
		entry.setPassword(password);
		entry.setLastName(lastName);
		entry.setGradeLevel(gradeLevel);
		entry.setLastActiveTest(1);
		entry.setReportCard(reportCard);
		
		database.put(userName, entry);
	};
	
	
	// method returns password value
	public String getPassword(String userName){
		return database.get(userName).getPassword();
	}
	
	// method returns First name value 
	public String getFirstName(String userName){
		return database.get(userName).getFirstName();
	}
	
	//method returns Last name value
	public String getLastName(String userName){
		return database.get(userName).getLastName();
	}
	
	//method returns Grade level value
	public int getGradeLevel(String userName){
		return database.get(userName).getGradeLevel();
	}
	
	//method returns test values
	public int getLastActiveTest(String userName){
		return database.get(userName).getLastActiveTest();
	}
	
	public HashMap<ModuleSelectButtonInterface, ModuleReportSummary> getReportCard(String userName){
		return database.get(userName).getReportCard();
	}
	
	//method sets password value
	public void setPassword(String userName, String password){
		database.get(userName).setPassword(password);
	}
	
	//method sets First name value
	public void setFirstName(String userName, String firstName){
		database.get(userName).setFirstName(firstName);
	}
	
	//method sets Last name value
	public void setLastName(String userName, String lastName){
		database.get(userName).setLastName(lastName);
	}
	
	//method sets Grade level value
	public void setGradeLevel(String userName, int gradeLevel){
		database.get(userName).setGradeLevel(gradeLevel);
	}
	
	//method sets last taken test value
	public void setLastActiveTest(String userName, int lastActiveTest){
		database.get(userName).setLastActiveTest(lastActiveTest);
	}
	
	//method sets report card values
	public void setReportCard(String userName, HashMap<ModuleSelectButtonInterface, ModuleReportSummary> reportCard){
		database.get(userName).setReportCard(reportCard);
	}
}
