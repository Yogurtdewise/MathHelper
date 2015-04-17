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

/**
 * This class is used as a data class that stores a student's grades for a particular Test Module.
 *  This class can be used in conjunction with a ModuleSelectButtonInterface to create a key/value
 *  mapping to a student's grades.
 *  NOTE: This class makes no warranty that a student's grade does not exceed the maximum
 *   achievable grade.
 * @author Kenneth Chin
 */
public class ModuleReportSummary implements Serializable{

	private int easyGrade     = -1;
	private int easyMax       = -1;
	private int normalGrade   = -1;
	private int normalMax     = -1;
	private int hardGrade     = -1;
	private int hardMax       = -1;
	
/****************
 * Constructors *
 ****************/
	
	/**
	 * A default constructor who's grades and max grades are all set to -1. This
	 *  ensures that isEasyTaken(), isNormalTaken(), and isHardTaken() all return false.
	 */
	public ModuleReportSummary(){}
	
	/**
	 * Used to create a ModuleReportSummary with specified values. Any "Grade" set to a
	 *  value less than 0, will ensure the difficulty level's isEasyTaken(), isNormalTaken(),
	 *  or isHardTaken() method returns false.
	 * @param easyGrade An int indicating a student's grade on the "Easy" difficulty level.
	 * @param easyMax An int indicating the maximum achievable grade on the "Easy" difficulty level.
	 * @param normalGrade An int indicating a student's grade on the "Normal" difficulty Level.
	 * @param normalMax An int indicating the maximum achievable grade on the "Normal" difficulty level.
	 * @param hardGrade An int indicating a student's grade on the "Hard" difficulty Level.
	 * @param hardMax An int indicating the maximum achievable grade on the "Hard" difficulty level.
	 */
	public ModuleReportSummary(int easyGrade, int easyMax, int normalGrade,
								int normalMax, int hardGrade, int hardMax){
		this.easyGrade = easyGrade;
		this.easyMax   = easyMax;
		this.normalGrade = normalGrade;
		this.normalMax   = normalMax;
		this.hardGrade   = hardGrade;
		this.hardMax     = hardMax;
	}
	
	
/*********************
 * isTaken() methods *	
 *********************/

	/**
	 * Used to determine if a student has taken the "Easy" difficulty level of this Test
	 *  Module.
	 * @return A boolean. False if the student's "Easy" grade is less than 0. True otherwise.
	 */
	public boolean isEasyTaken(){
		return (easyGrade >= 0);
	}
	
	/**
	 * Used to determine if a student has taken the "Normal" difficulty level of this Test
	 *  Module.
	 * @return A boolean. False if the student's "Normal" grade is less than 0. True otherwise.
	 */
	public boolean isNormalTaken(){
		return (normalGrade >= 0);
	}
	
	/**
	 * Used to determine if a student has taken the "Hard" difficulty level of this Test
	 *  Module.
	 * @return A boolean. False if the student's "Hard" grade is less than 0. True otherwise.
	 */
	public boolean isHardTaken(){
		return (hardGrade >= 0);
	}
	
	
/***************************
 * "Easy" getters & setters *
 ***************************/

	/**
	 * Used to obtain the int representing the student's "Easy" grade.
	 * @return The int representing the student's "Easy" grade.
	 */
	public int getEasyGrade(){
		return easyGrade;
	}
	
	/**
	 * Used to set the student's "Easy" grade.
	 * @param grade An int representing the student's "Easy" grade.
	 */
	public void setEasyGrade(int grade){
		easyGrade = grade;
	}
	
	/**
	 * Used to get the maximum achievable grade for the "Easy" difficulty level.
	 * @return An int indicating the maximum achievable grade for the "Easy" difficulty level.
	 */
	public int getEasyMax(){
		return easyMax;
	}
	
	/**
	 * Used to set the maximum achievable grade for the "Easy" difficulty level.
	 * @param max An int indicating the maximum achievable grade for the "Easy" difficulty level.
	 */
	public void setEasyMax(int max){
		easyMax = max;
	}
	
	
/******************************
 * "Normal" getters & setters *
 ******************************/	
	
	/**
	 * Used to obtain the int representing the student's "Normal" grade.
	 * @return The int representing the student's "Normal" grade.
	 */
	public int getNormalGrade(){
		return normalGrade;
	}
	
	/**
	 * Used to set the student's "Normal" grade.
	 * @param grade An int representing the student's "Normal" grade.
	 */
	public void setNormalGrade(int grade){
		normalGrade = grade;
	}
	
	/**
	 * Used to get the maximum achievable grade for the "Normal" difficulty level.
	 * @return An int indicating the maximum achievable grade for the "Normal" difficulty level.
	 */
	public int getNormalMax(){
		return normalMax;
	}
	
	/**
	 * Used to set the maximum achievable grade for the "Normal" difficulty level.
	 * @param max An int indicating the maximum achievable grade for the "Normal" difficulty level.
	 */
	public void setNormalMax(int max){
		normalMax = max;
	}
	
	
/***************************
* "Hard" getters & setters *
***************************/	
	
	/**
	 * Used to obtain the int representing the student's "Hard" grade.
	 * @return The int representing the student's "Hard" grade.
	 */
	public int getHardGrade(){
		return hardGrade;
	}
	
	/**
	 * Used to set the student's "Hard" grade.
	 * @param grade An int representing the student's "Hard" grade.
	 */
	public void setHardGrade(int grade){
		hardGrade = grade;
	}
	
	/**
	 * Used to get the maximum achievable grade for the "Hard" difficulty level.
	 * @return An int indicating the maximum achievable grade for the "Hard" difficulty level.
	 */
	public int getHardMax(){
		return hardMax;
	}
	
	/**
	 * Used to set the maximum achievable grade for the "Hard" difficulty level.
	 * @param max An int indicating the maximum achievable grade for the "Hard" difficulty level.
	 */
	public void setHardMax(int max){
		hardMax = max;
	}
}
