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
	
	/**initialization of DB. It will be populated with the values stored in Hash table*/
	public UserDatabase(){
		database = new HashMap<String, UserEntry>();
	DefaulPhonyUsers defaultUser = new DefaulPhonyUsers(this);  
		defaultUser.populatePhonyUsers();
	}
<<<<<<< HEAD
	
	/**this method will add users to the database. It will pass all needed values 
=======
	/*this method will add users to the database. It will pass all needed values 
>>>>>>> f3bcedc1c5856096afe1ae91d045d3e83f4ceab5
	*to it such as user name, password, first name, last name and grade level. 
	*It will also pass the report of all taken tests
	 */
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
		
		/**at this point system will save all needed information into the database*/
		database.put(userName, entry);
	};
	/**method to add user*/
	public void addUser(String userName, 
			String password, 
			String firstName, 
			String lastName,
			int gradeLevel,
			HashMap<ModuleSelectButtonInterface, ModuleReportSummary> reportCard){
		/**class that stores information about user*/
		UserEntry entry = new UserEntry();
		entry.setFirstName(firstName);
		entry.setPassword(password);
		entry.setLastName(lastName);
		entry.setGradeLevel(gradeLevel);
		entry.setLastActiveTest(1);
		entry.setReportCard(reportCard);
		
		database.put(userName, entry);
	};
	
	
<<<<<<< HEAD
	/** method returns password value*/
=======
	/* method returns password value*/
>>>>>>> f3bcedc1c5856096afe1ae91d045d3e83f4ceab5
	public String getPassword(String userName){
		if(database.get(userName)==null)
			return null;
		return database.get(userName).getPassword();
	}
	
<<<<<<< HEAD
	/** method returns First name value */
=======
	/* method returns First name value */
>>>>>>> f3bcedc1c5856096afe1ae91d045d3e83f4ceab5
	public String getFirstName(String userName){
		if(database.get(userName)==null)
			return null;
		return database.get(userName).getFirstName();
	}
	
<<<<<<< HEAD
	/**method returns Last name value*/
=======
	/*method returns Last name value*/
>>>>>>> f3bcedc1c5856096afe1ae91d045d3e83f4ceab5
	public String getLastName(String userName){
		if(database.get(userName)==null)
			return null;
		return database.get(userName).getLastName();
	}
	
<<<<<<< HEAD
	/**method returns Grade level value*/
=======
	/*method returns Grade level value*/
>>>>>>> f3bcedc1c5856096afe1ae91d045d3e83f4ceab5
	public int getGradeLevel(String userName){
		if(database.get(userName)==null)
			return -1;
		return database.get(userName).getGradeLevel();
	}
	
<<<<<<< HEAD
	/**method returns test values*/
=======
	/*method returns test values*/
>>>>>>> f3bcedc1c5856096afe1ae91d045d3e83f4ceab5
	public int getLastActiveTest(String userName){
		if(database.get(userName)==null)
			return -1;
		return database.get(userName).getLastActiveTest();
	}
	
	public HashMap<ModuleSelectButtonInterface, ModuleReportSummary> getReportCard(String userName){
		if(database.get(userName)==null)
			return null;
		return database.get(userName).getReportCard();
	}
	
<<<<<<< HEAD
	/**method sets password value*/
=======
	/*method sets password value*/
>>>>>>> f3bcedc1c5856096afe1ae91d045d3e83f4ceab5
	public void setPassword(String userName, String password){
		database.get(userName).setPassword(password);
	}
	
<<<<<<< HEAD
	/** method sets First name value*/
=======
	/* method sets First name value*/
>>>>>>> f3bcedc1c5856096afe1ae91d045d3e83f4ceab5
	public void setFirstName(String userName, String firstName){
		database.get(userName).setFirstName(firstName);
	}
	
<<<<<<< HEAD
	/**method sets Last name value*/
=======
	/*method sets Last name value*/
>>>>>>> f3bcedc1c5856096afe1ae91d045d3e83f4ceab5
	public void setLastName(String userName, String lastName){
		database.get(userName).setLastName(lastName);
	}
	
<<<<<<< HEAD
	/**method sets Grade level value*/
=======
	/*method sets Grade level value*/
>>>>>>> f3bcedc1c5856096afe1ae91d045d3e83f4ceab5
	public void setGradeLevel(String userName, int gradeLevel){
		database.get(userName).setGradeLevel(gradeLevel);
	}
	
<<<<<<< HEAD
	/**method sets last taken test value*/
=======
	/*method sets last taken test value*/
>>>>>>> f3bcedc1c5856096afe1ae91d045d3e83f4ceab5
	public void setLastActiveTest(String userName, int lastActiveTest){
		database.get(userName).setLastActiveTest(lastActiveTest);
	}
	
<<<<<<< HEAD
	/**method sets report card values*/
=======
	/*method sets report card values*/
>>>>>>> f3bcedc1c5856096afe1ae91d045d3e83f4ceab5
	public void setReportCard(String userName, HashMap<ModuleSelectButtonInterface, ModuleReportSummary> reportCard){
		database.get(userName).setReportCard(reportCard);
	}
}
