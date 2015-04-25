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
package project.database;

import java.io.Serializable;
import java.util.HashMap;

import project.interfaces.ModuleSelectButtonInterface;

/**
 * TODO Replace this class.
 * @author Kenneth Chin
 */
public class UserEntry implements Serializable{
	
	private String password    = null;
	private String firstName   = null;
	private String lastName    = null;
	private int gradeLevel     = 0;
	private int lastActiveTest = 1;
	
	private HashMap<ModuleSelectButtonInterface, ModuleReportSummary> reportCard = null;
	
	public UserEntry(){}
	
	public String getPassword(){
		return password;
	}
	
	public String getFirstName(){
		return firstName;
	}
	
	public String getLastName(){
		return lastName;
	}
	
	public int getGradeLevel(){
		return gradeLevel;
	}
	
	public int getLastActiveTest(){
		return lastActiveTest;
	}
	
	public HashMap<ModuleSelectButtonInterface, ModuleReportSummary> getReportCard(){
		return reportCard;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName){
		this.lastName = lastName;
	}
	
	public void setGradeLevel(int gradeLevel){
		this.gradeLevel =  gradeLevel;
	}
	
	public void setLastActiveTest(int lastActiveTest){
		this.lastActiveTest = lastActiveTest;
	}
	
	public void setReportCard(HashMap<ModuleSelectButtonInterface, ModuleReportSummary> reportCard){
		this.reportCard = reportCard;
	}
	
 }
