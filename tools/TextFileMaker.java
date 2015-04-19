/**
 * Name:         Math Helper
 * Version:      0.11.1
 * Version Date: 04/19/2015
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
package project.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

/**
 * This class is used to make a single text file. Each call to one of TextFileMaker's public methods
 *  creates a new text file. If the specified file already exists, "_#" is added to the specified
 *  "fileName", where "#" is an integer that is incremented until an unused file name is found.
 *  All newly created files will have the name suffix of ".txt".
 * @author Kenneth Chin
 */
public final class TextFileMaker{
	
	private static File     file;     //The File object that describes the file to be created.
	private static String   fileName; //The full file name of the file to be created.
	private static String   filePath; //The path from the program's directory, where the new file is to be created.
	private static String   text      = null; //Used if writeString() is called. See method details.
	private static String[] textArray = null; //Used if writeArray() is called. See method details.
	
	private static int versionNum = 1; //Used to increment fileName until an unused name is found.

	/**
	 * Private constructor prevents instantiation.
	 */
	private TextFileMaker(){}
	
	/**
	 * Creates a new text file who's contents are the specified String, "text". If the specified file
	 *  already exists, "_#" is added to the specified "fileName", where "#" is an integer that is
	 *  incremented until an unused file name is found. All newly created files will have the name suffix
	 *  of ".txt".
	 * @param filePath A String indicating the path to the directory where the new file will be created.
	 *  The path should begin from the program's root directory, and should not be preceded by any
	 *  directory separators.
	 * @param fileName A String indicating the new file's name. NOTE: This fileName will not be strictly
	 *  adhered to. See the method details for more information.
	 * @param text A String that is to be written to the specified text file.
	 * @throws IOException Thrown if the specified file can not be written to.
	 */
	public static void writeString(String filePath, String fileName, String text) throws IOException{
		TextFileMaker.fileName = fileName + ".txt";
		TextFileMaker.filePath = filePath;
		TextFileMaker.text     = text;
		makeFolder(filePath);
		file = new File(filePath, TextFileMaker.fileName);
		getUnusedFileName();
		writeToFile();
	}
	
	/**
	 * Creates a new text file who's contents are the elements of the specified String array, "textArray".
	 *  Each String element in the array will be written to the specified file in the order they exist
	 *  within the array. Each element will also have a carriage return added to the end of the String,
	 *  excluding the final element. If the specified file already exists, "_#" is added to the specified
	 *  "fileName", where "#" is an integer that is incremented until an unused file name is found. All
	 *  newly created files will have the name suffix of ".txt".
	 * @param filePath A String indicating the path to the directory where the new file will be created.
	 *  The path should begin from the program's root directory, and should not be preceded by any
	 *  directory separators.
	 * @param fileName A String indicating the new file's name. NOTE: This fileName will not be strictly
	 *  adhered to. See the method details for more information.
	 * @param textArray A String array that is to be written to the specified text file. Each String 
	 *  element in the array will be written to the specified file in the order they exist within the
	 *  array. Each element will also have a carriage return added to the end of the String, excluding
	 *  the final element.
	 * @throws IOException Thrown if the specified file can not be written to.
	 */
	public static void writeArray(String filePath, String fileName, String[] textArray) throws IOException{
		TextFileMaker.fileName  = fileName + ".txt";
		TextFileMaker.filePath  = filePath;
		TextFileMaker.textArray = textArray;
		makeFolder(filePath);
		file = new File(filePath, TextFileMaker.fileName);
		getUnusedFileName();
		writeToFile();
	}
	
	/**
	 * A convenience method used to obtain a String that describes the current time. The String is
	 *  expressed as "Year_Month_DayOfMonth_HourOfDay_Minute". This String can be used to name files.
	 * @return A String expressed as "Year_Month_DayOfMonth_HourOfDay_Minute", using System.currentTimeMillis().
	 */
	public static String getTimeStamp(){
		Calendar date = Calendar.getInstance();
		date.setTimeInMillis(System.currentTimeMillis());
		return date.get(Calendar.YEAR) + "_" + date.get(Calendar.MONTH) + "_" + date.get(Calendar.DAY_OF_MONTH)
				+ "_" + date.get(Calendar.HOUR_OF_DAY) + "_" + date.get(Calendar.MINUTE);
	}
	
	/**
	 * Used to make the specified folder(s), if the path does not already exist.
	 * @param pathFromRoot A String indicating the directory path, from the program's directory,
	 *  which is to be created.
	 * @return A boolean indicating true if the specified path structure was created or already exists;
	 *  false, otherwise.
	 */
	public static boolean makeFolder(String pathFromRoot){
		File directory = new File(pathFromRoot);
		if(directory.exists())
			return true;
		return directory.mkdirs();
	}
	
	/**
	 * A helper method used to obtain an unused file name. Adds "_#" to the specified fileName, where
	 *  "#" is an integer that is incremented until fileName is no longer a used name.
	 */
	private static void getUnusedFileName(){
		if(!file.exists()){
			return;
		}else{
			String newFileName = fileName + "_" + versionNum + ".txt";
			file = new File(filePath, newFileName);
			versionNum++;
			getUnusedFileName();
		}
	}
	
	/**
	 * Create, write, and finalize the file specified by class field "file". If there is nothing
	 *  to write or if both text and textArray are not null(the latter should never happen), the
	 *  newly created file is deleted.
	 * @throws IOException Thrown if there was a problem writing to the specified file (ex. another
	 *  program is using the specified file).
	 */
	private static void writeToFile() throws IOException{
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		if((textArray == null) && (text != null)){
			writer.write(text);
		}else if(text == null && textArray != null){
			for(int i = 0; i < textArray.length - 1; i++){
				writer.write(textArray[i] + "\r\n");
			}
			writer.write(textArray[textArray.length-1]);
		}else{
			if(file.exists())
				file.delete();
		}
		writer.close();
	}
}
