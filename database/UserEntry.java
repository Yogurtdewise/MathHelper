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

import java.io.Serializable;
import java.util.HashMap;

import project.interfaces.ModuleSelectButtonInterface;

/**
 *
 * @author Elena Eroshkina
 */
public class UserEntry implements Serializable{
	/**
	 * The serialVersionUID for this Serializable class.
	 * @since v0.12.0
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * declaration of all needed fields for each user entry
	 */
	private String password = null;
	private String firstName = null;
	private String lastName = null;
	private int gradeLevel = 0;
	private int lastActiveTest = 1;
	private HashMap<ModuleSelectButtonInterface, ModuleReportSummary> reportCard = null;
	public UserEntry(){}
	/*
	 *retrieve the password value
	 */
	public String getPassword(){
		return password;
	}
	/*
	 *retrive the First name value
	 */
	public String getFirstName(){
		return firstName;
	}
	/*
	 *retrieve the Last name value
	 */
	public String getLastName(){
		return lastName;
	}
	/*
	 *retrieve the Grade value
	 */
	public int getGradeLevel(){
		return gradeLevel;
	}
	/*
	 *retrieve the Last taken test value
	 */
	public int getLastActiveTest(){
		return lastActiveTest;
	}
	/*
	 *takes Hash Map value from UserEntry()
	 * initially it is equals to null
	 * after user take a set of tests it will return a Hash Map that stores students grades
	 */
	public HashMap<ModuleSelectButtonInterface, ModuleReportSummary> getReportCard(){
		return reportCard;
	}
	/*
	 *sets the password value
	 */
	public void setPassword(String password){
		this.password = password;
	}
	/*
	 *sets the First name value
	 */
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
	/*
	 *sets the Last name value
	 */
	public void setLastName(String lastName){
		this.lastName = lastName;
	}
	/*
	 *sets the Grade level value
	 */
	public void setGradeLevel(int gradeLevel){
		this.gradeLevel = gradeLevel;
	}
	/*
	 *sets the Last taken test value
	 */
	public void setLastActiveTest(int lastActiveTest){
		this.lastActiveTest = lastActiveTest;
	}
	public void setReportCard(HashMap<ModuleSelectButtonInterface, ModuleReportSummary> reportCard){
		this.reportCard = reportCard;
	}
}