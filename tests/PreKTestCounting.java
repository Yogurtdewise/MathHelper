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
package project.tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import project.buttons.PreKModuleSelectTestButtons;
import project.constants.DifficultyLevel;
import project.interfaces.ModuleSelectButtonInterface;
import project.interfaces.TestableObserver;
import project.run.GUIManager;
import project.screens.RewardScreen;
import project.tools.MainWindow;
import project.tools.QuestionPanelText;
import project.tools.TextFileMaker;

/**
 * This class is used to test PreK-K students on Counting skills. It displays a sequence of
 *  integers between 0 and 20 (inclusive). One number of the sequence is missing. A student
 *  is asked to enter the missing number into a text box, then click a "Submit" button. No two
 *  questions are the same.
 * @author Kenneth Chin
 */
public class PreKTestCounting implements TestableObserver{
	
	//The ModuleSelectButtonInterface that describes this test.
	private static final ModuleSelectButtonInterface TEST_BUTTON = PreKModuleSelectTestButtons.Button.COUNTING;
	
	private static final int LOWEST_INT    = 0;  //Must be zero.
	private static final int HIGHEST_INT   = 20; //The highest integer value to be tested.
	
	//Difficulty settings. Note: Settings MUST be less than the maximum number of question permutations.
	private static final int EASY_SEQUENCE_SIZE = 5;  //The number of integers in a sequence for the "Easy" difficulty.
	private static final int EASY_MAX_QUESTIONS = 10; //The maximum number of questions for the "Easy" difficulty.
	private static final int NORM_SEQUENCE_SIZE = 4;  //The number of integers in a sequence for the "Normal" difficulty.
	private static final int NORM_MAX_QUESTIONS = 15; //The maximum number of questions for the "Normal" difficulty.
	private static final int HARD_SEQUENCE_SIZE = 3;  //The number of integers in a sequence for the "Hard" difficulty.
	private static final int HARD_MAX_QUESTIONS = 20; //The maximum number of questions for the "Hard" difficulty.
	
	private int sequenceSize         = EASY_MAX_QUESTIONS; //The actual sequence size for this test.
	private int maxNumberOfQuestions = EASY_MAX_QUESTIONS; //The actual maximum number of questions for this test.
	
	private Random rng = new Random(System.currentTimeMillis()); //A random number generator.

	private boolean isPractice = false;   //Used to indicate that this test is a practice test.
	private DifficultyLevel difficulty;   //The current DifficultyLevel of this test.
	
	private int currentQuestionNum      = 1; //The current question number.
	//The set of all possible questions that may be asked, associated with a boolean indicating
	// if the question has been asked. Must be set at instantiation after sequenceSize is set.
	private boolean[][] isNumberSetUsed;
	private int currentAnswer;  //The answer to the current question.
	private int numCorrect = 0; //The number of correct answers received from the user.
	private ArrayList<String> wrongAnswers = new ArrayList<String>(); //Used to track incorrect answers.
	private String currentQuestion; //The current question in String form.
	
	private QuestionPanelText testPanel; //The QuestionPanelText used to display questions and retrieve answers.
	private Clip clip; //The audio clip used to play the tutorial sounds.
	
	private GUIManager manager;    //The GUIManager that manages mainWindow & all GUI screens.
	private MainWindow mainWindow; //The MainWindow that is to have questions displayed on.
	
	/**
	 * The PreKTestCounting constructor. Creates and displays a Counting test for PreK-K students.
	 * @param manager The GUIManager that manages the primary MainWindow & all GUI screens.
	 * @param isPractice A boolean indicating true if this test is a practice test, false otherwise.
	 * @param difficulty The DifficultyLevel of this test.
	 * @throws IOException Thrown if any image file is missing.
	 */
	public PreKTestCounting(GUIManager manager, boolean isPractice, DifficultyLevel difficulty) throws IOException{
		this.manager    = manager;
		this.mainWindow = manager.getMainWindow();
		this.isPractice = isPractice;
		this.difficulty = difficulty;
		setDifficulty();
		
		isNumberSetUsed = new boolean[(HIGHEST_INT - sequenceSize) + 2][sequenceSize];
		testPanel = new QuestionPanelText(mainWindow, maxNumberOfQuestions);
		testPanel.registerObserver(this);
		playTutorial();
		askQuestion();
	}
	
	
	/**
	 * Used to play an audio tutorial, describing how to use this test.
	 */
	public void playTutorial(){
	    try{
	    	String filePath = "audio\\Test Tutorials\\Counting.wav";
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
	        clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    }catch(Exception e) {
	        manager.handleException(e);
	    }
	}
	
	/**
	 * Used to set maxNumberOfQuestions and sequenceSize to the number specified by difficulty.
	 *  See class difficulty setting constants.
	 */
	private void setDifficulty(){
		switch(difficulty){
			case EASY:   maxNumberOfQuestions = EASY_MAX_QUESTIONS;
						 sequenceSize         = EASY_SEQUENCE_SIZE;
			             break;
			case NORMAL: maxNumberOfQuestions = NORM_MAX_QUESTIONS;
			 			 sequenceSize         = NORM_SEQUENCE_SIZE;
			 			 break;
			case HARD:   maxNumberOfQuestions = HARD_MAX_QUESTIONS;
			 			 sequenceSize         = HARD_SEQUENCE_SIZE;
			 			 break;
			default:     maxNumberOfQuestions = EASY_MAX_QUESTIONS;
						 sequenceSize         = EASY_SEQUENCE_SIZE;
						 break;
		}
	}
	
	/**
	 * Used to create and display the next question.
	 */
	private void askQuestion(){
		String question = "What number is missing?<br><br>";
		currentQuestion = makeQuestion();
		question += currentQuestion;
		testPanel.showQuestion(question, (currentQuestionNum));
		currentQuestionNum++;
	}
	
	/**
	 * A helper method, used to obtain a random int between lowestValue and highestValue
	 *  (inclusive).
	 * @param lowestValue The lowest int value that the random number generator may return.
	 * @param highestValue The highest int value that the random number generator may return.
	 * @return A random int between lowestValue and highestValue (inclusive).
	 */
	private int getRandomInt(int lowestValue, int highestValue){
		//Swap the numbers if they are backwards.
		if(lowestValue > highestValue){
			int temp     = highestValue;
			highestValue = lowestValue;
			lowestValue  = temp;
		}
	    return rng.nextInt((highestValue - lowestValue) + 1) + lowestValue;
	}
	
	/**
	 * Used to create a sequence of numbers that is to be displayed to the user. One number is
	 *  missing. The missing number is replaced with an underscore, followed by a space.
	 * @param startInt An int. The first integer in the sequence.
	 * @param missingNum An int. The integer in the sequence that is missing.
	 * @return A String representing the integer sequence, with the missing number represented as "_ ".
	 */
	private String getSequenceString(int startInt, int missingNum){
		String sequenceString = "";
		for(int i=0; i<sequenceSize; i++){
			int numToAdd = startInt + i;
			String currentText = (numToAdd == missingNum) ? "_ " : (new Integer(numToAdd).toString());
			sequenceString += (i == (sequenceSize - 1))  ? currentText : (currentText + ", ");
		}
		return sequenceString;
	}
	
	/**
	 * A helper method used to determine if a particular integer sequence and missing number
	 *  combination has been used in a question.
	 * @param sequenceBegin An int representing the first integer in a sequence.
	 * @param missingNumIndex An int indicating the missing number in a sequence.
	 * @return A boolean. True if the question has been asked. False otherwise.
	 */
	private boolean isUsedQuestion(int sequenceBegin, int missingNumIndex){
		return isNumberSetUsed[sequenceBegin][missingNumIndex];
	}
	
	/**
	 * Used to generate a random sequence of sequential integers, with a random missing number.
	 * @return A String representing the sequence of integers generated, with the missing number
	 *  represented as "_ ".
	 */
	private String makeQuestion(){
		int startInt;
		int missingNumIndex;
		do{
			startInt        = getRandomInt(LOWEST_INT, (HIGHEST_INT - sequenceSize + 1));
			missingNumIndex = getRandomInt(0, (sequenceSize-1));
		}while(isUsedQuestion(startInt, missingNumIndex));
		int missingNum = startInt + missingNumIndex;
		currentAnswer  = missingNum;
		isNumberSetUsed[startInt][missingNumIndex] = true;
		return getSequenceString(startInt, missingNum);
	}
	
	/**
	 * Used to display a practice test's answer after a user has submitted an answer.
	 * @param answer A String indicating the user's answer to the current question.
	 */
	private void showAnswer(String answer){
		boolean isCorrect = checkAnswer(answer);
		String  message;
		if(isCorrect)
			message = "\"" +answer + "\"" + " is correct!";
		else
			message = "\"" + answer + "\""
					+ " is incorrect! The correct answer is: " + currentAnswer;
		try {
			testPanel.showAnswer(message, isCorrect);
		} catch (IOException e) {
			manager.handleException(e);
		}
	}
	
	/**
	 * Used to compare a user's answer to the question's correct answer. Increments
	 *  numCorrect if the user's answer was correct. Returns true if the user's answer
	 *  was correct; false otherwise.
	 * @param answer A String representing the user's answer.
	 * @return A boolean indicating true if the user's answer was correct; false otherwise.
	 */
	private boolean checkAnswer(String answer){
		if(answer.equals(new Integer(currentAnswer).toString())){
			numCorrect++;
			return true;
		}else{
			logWrongAnswer(answer);
			return false;
		}
	}
	
	/**
	 * A helper method, used to create a String that describes an incorrectly answered question.
	 *  Adds the created String to the wrongAnswers ArrayList.
	 * @param wrongAnswer A String representing the incorrect answer that a user gave to currentQuestion.
	 */
	private void logWrongAnswer(String wrongAnswer){
		String question = "Question " + (currentQuestionNum-1) + ": (" + currentQuestion + ")";
		String entry    = question + " Student Answer: (" + wrongAnswer + ")"
								   + " Correct Answer: (" + currentAnswer + ").";
		wrongAnswers.add(entry);
	}
	
	/**
	 * Used to create a text file containing all questions, user answers, and correct answers
	 *  for all questions that the user answered incorrectly. If the user did not answer any
	 *  question incorrectly, no file is created.
	 * @throws IOException Thrown if there is a problem writing the file.
	 */
	private void makeTestDetailFile() throws IOException{
		if(wrongAnswers.size() > 0){
			String filePath = manager.getTestFolderPath() + "\\Counting\\";
			String fileName = TextFileMaker.getTimeStamp() + "_(" + difficulty.getName() + ")";
			String[] textArray = wrongAnswers.toArray(new String[wrongAnswers.size()]);
			TextFileMaker.writeArray(filePath, fileName, textArray);
		}
	}
	
	/**
	 * A helper method, used to obtain an int indicating the user's grade out of 100%.
	 * @return An int indicating the user's grade out of 100%.
	 */
	private int getGrade(){
		return Math.round((((float)numCorrect / (float)maxNumberOfQuestions) * 100));
	}
	
	/**
	 * A helper method, used to determine if the user's grade is better than a previously
	 *  recorded grade.
	 * @param numCorrect An int describing the user's number of correct answers.
	 * @return A boolean that is true if the user's current grade is greater than or equal
	 *  to a previously recorded grade; false otherwise.
	 */
	private boolean isBetterGrade(int numCorrect){
		return (numCorrect >= manager.getPreviousGrade(TEST_BUTTON, difficulty));
	}
	
	@Override
	public void answered(String answer){
		if(isPractice){
			showAnswer(answer);
		}
		else if(currentQuestionNum <= maxNumberOfQuestions){
			checkAnswer(answer);
			askQuestion();
		}else{
			checkAnswer(answer);
			testPanel.tearDown();
			if(clip.isActive())
				clip.stop();
			try{
				int grade = getGrade();
				boolean isBetter = isBetterGrade(numCorrect);
				String fileName = "Counting(" + difficulty.getName() + ")";
				if(isBetter)
					manager.setGrade(TEST_BUTTON, difficulty, numCorrect, maxNumberOfQuestions);
				makeTestDetailFile();
				new RewardScreen(manager, TEST_BUTTON, difficulty, grade, isBetter, manager.getRewardsFolderPath());
			}catch(IOException e){
				manager.handleException(e);
			}
		}
	}
	
	@Override
	public void nextClicked() {
		if(isPractice){
			if(currentQuestionNum <= maxNumberOfQuestions){
				askQuestion();
			}else{
				if(clip.isActive())
					clip.stop();
				String message = "All practice questions answered!\n\n"
							   + "Your grade is: " + getGrade() + "%!\n\n"
							   + "Click OK to return to the Welcome Screen!";
		    	JOptionPane.showMessageDialog(null, message, "Practice Complete!", JOptionPane.INFORMATION_MESSAGE);
		    	testPanel.tearDown();
		    	try{
					manager.buildWelcomeScreen();
		    	}catch (IOException e){
					manager.handleException(e);
				}
			}
		}
	}
	
	@Override
	public void homeClicked(){
		//If this is not a practice test, confirm the user wants to quit.
		int answer = JOptionPane.NO_OPTION;
		if(!isPractice){
			String[] options = {"Yes", "No"};
			String warning = "<p>Warning:</p><br>"
					+ "        <p>You haven't finished the test!<br>"
					+ "        If you quit now, your progress<br>"
					+ "        won't be saved!</p>"
					+ "        <p><CENTER>Are you sure you want to quit?</CENTER></p>";
			JLabel warningLabel = new JLabel("<HTML><div>" + warning + "</div></HTML>");
			answer = JOptionPane.showOptionDialog(mainWindow, warningLabel, "Test not finished!",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
		}
		//If this is a practice test or the user confirmed they want to quit, do so. Else, do nothing.
		if(isPractice || answer == JOptionPane.YES_OPTION){
			if(clip.isActive())
				clip.stop();
			testPanel.tearDown();
			try {
				manager.buildWelcomeScreen();
			} catch (IOException e) {
				manager.handleException(e);
			}
		}
	}
}
