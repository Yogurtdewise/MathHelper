/**
 * Name:         Math Helper
 * Version:      0.11.2
 * Version Date: 04/20/2015
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
import project.tools.QuestionPanelSelect;
import project.tools.TextFileMaker;

/**
 * This class is used to test PreK-K students on number US Coin recognition skills. It displays two
 *  selectable panels, each with a US coin. A student is asked to select the panel that matches
 *  the attributes specified in a question, then click the "Submit" button. While a student
 *  may be asked to match the same coin multiple times, the combination of images and question types
 *  (name or vcent value) will never be used twice.
 * @author Kenneth Chin
 */
public class PreKTestCoins implements TestableObserver{
	
	//The ModuleSelectButtonInterface that describes this test.
	private static final ModuleSelectButtonInterface TEST_BUTTON = PreKModuleSelectTestButtons.Button.COINS;

	private static final int NUM_QUESTION_TYPES = 2;   //The number of question types (name or value).
	private static final int NAME_TYPE = 0;  //The constant that indicates a question that identifies a coin's name.
	
	//Difficulty settings. Note: MUST be less than the maximum number of question permutations.
	private static final int EASY_MAX_QUESTIONS = 8;    //The maximum number of questions for the "Easy" difficulty.
	private static final int NORM_MAX_QUESTIONS = 10;   //The maximum number of questions for the "Normal" difficulty.
	private static final int HARD_MAX_QUESTIONS = 12;   //The maximum number of questions for the "Hard" difficulty.
	private int maxNumberOfQuestions = EASY_MAX_QUESTIONS; //The actual maximum number of questions for this test.
	
	private Random rng = new Random(System.currentTimeMillis()); //A random number generator.

	private boolean isPractice = false;   //Used to indicate that this test is a practice test.
	private DifficultyLevel difficulty;   //The current DifficultyLevel of this test.
	
	private int currentQuestionNum = 1;   //The current question number.

	private boolean[][][] isNumberSetUsed = new boolean[Coin.values().length][Coin.values().length][NUM_QUESTION_TYPES];
	private String correctAnswer;   //The name or value of the correct answer's coin.
	private String wrongAnswer;     //The name or value of the wrong answer's coin.
	private int correctAnswerIndex; //The Coin.values() index of the correct answer's coin.
	private int wrongAnswerIndex;   //The Coin.values() index of the wrong answer's coin.
	private int questionType;       //Used to indicate a name (0) or value (1) question type.
	private String answerString;    //The correct answer's panel String ("left" or "right").
	private int numCorrect = 0;     //The number of correct answers obtained from the user.
	private ArrayList<String> wrongAnswers = new ArrayList<String>(); //Used to track incorrect answers.
	
	private QuestionPanelSelect testPanel; //The QuestionPanelSelect that will display questions.
	private Clip clip; //The audio clip used to play the tutorial sounds.
	
	private GUIManager manager;    //The GUIManager that manages mainWindow & all GUI screens.
	private MainWindow mainWindow; //The MainWindow that will have questions displayed on.
	
	/**
	 * The PreKTestCoins constructor. Creates and displays a Coins test for PreK-K students.
	 * @param manager The GUIManager that manages the primary MainWindow & all GUI screens.
	 * @param isPractice A boolean indicating true if this test is a practice test, false otherwise.
	 * @param difficulty The DifficultyLevel of this test.
	 * @throws IOException Thrown if any image file is missing.
	 */
	public PreKTestCoins(GUIManager manager, boolean isPractice, DifficultyLevel difficulty) throws IOException{
		this.manager    = manager;
		this.mainWindow = manager.getMainWindow();
		this.isPractice = isPractice;
		this.difficulty = difficulty;
		
		setDifficulty();
		testPanel = new QuestionPanelSelect(mainWindow, maxNumberOfQuestions);
		testPanel.registerObserver(this);
		
		initArrays();
		playTutorial();
		askQuestion();
	}
	
	/**
	 * Used to play an audio tutorial, describing how to use this test.
	 */
	public void playTutorial(){
	    try{
	    	String filePath = "audio\\Test Tutorials\\Coins.wav";
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
	        clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    }catch(Exception e) {
	        manager.handleException(e);
	    }
	}
	
	/**
	 * Used to create and display the next question.
	 */
	private void askQuestion(){
		getValues();
		makeQuestion();
		currentQuestionNum++;
	}
	
	/**
	 * Used to obtain an unused combination of panel images and question type.
	 */
	private void getValues(){
		do{
			questionType       = getRandomInt(0, NUM_QUESTION_TYPES -1);
			correctAnswerIndex = getRandomInt(0, Coin.values().length - 1);
			wrongAnswerIndex   = getRandomInt(0, Coin.values().length - 1);
		}while(correctAnswerIndex == wrongAnswerIndex
				|| isNumberSetUsed[correctAnswerIndex][wrongAnswerIndex][questionType]);
		if(questionType == NAME_TYPE){
			correctAnswer = Coin.values()[correctAnswerIndex].getName().toLowerCase();
			wrongAnswer   = Coin.values()[wrongAnswerIndex].getName().toLowerCase();
		}else{
			correctAnswer = Coin.values()[correctAnswerIndex].getCentValue();
			wrongAnswer   = Coin.values()[wrongAnswerIndex].getCentValue();
		}
		isNumberSetUsed[correctAnswerIndex][wrongAnswerIndex][questionType] = true;
	}
	
	/**
	 * Used to generate question text, and randomize the panel order (left or right).
	 * Displays the question and two panels.
	 */
	private void makeQuestion(){
		String question;
		if(questionType == NAME_TYPE){
			question = "Which picture shows a <b><u>" + correctAnswer + "</u></b>?";
		}else{
			question = "Which picture shows a <b><u>" + correctAnswer + "&cent;</u></b>?";
		}
		String leftImagePath;
		String rightImagePath;
		int answerLoc = getRandomInt(0, 1);
		if(answerLoc == 0){
			answerString   = QuestionPanelSelect.Answer.LEFT.getStringValue();
			leftImagePath  = Coin.values()[correctAnswerIndex].getFilePath();
			rightImagePath = Coin.values()[wrongAnswerIndex].getFilePath();
		}
		else{
			answerString   = QuestionPanelSelect.Answer.RIGHT.getStringValue();
			leftImagePath  = Coin.values()[wrongAnswerIndex].getFilePath();
			rightImagePath = Coin.values()[correctAnswerIndex].getFilePath();
		}
		try {
			testPanel.showQuestion(question, currentQuestionNum, leftImagePath, rightImagePath);
		} catch (IOException e) {
			manager.handleException(e);
		}
	}
	
	/**
	 * Used to display a practice test's answer after a user has submitted an answer.
	 * @param answer A String, formated by the specification of QuestionPanelSelect.Answer's getStringValue()
	 *  method, that indicates the user's answer for the current question.
	 */
	private void showAnswer(String answer){
		boolean isCorrect = checkAnswer(answer);
		String  message = (isCorrect) ? "Correct!" : "Incorrect! The correct answer does not have an X through it.";
		if(answerString.equals(QuestionPanelSelect.Answer.LEFT.getStringValue())){
			try {
				testPanel.showAnswer(message, QuestionPanelSelect.Answer.LEFT, isCorrect);
			} catch (IOException e) {
				manager.handleException(e);
			}
		}else if(answerString.equals(QuestionPanelSelect.Answer.RIGHT.getStringValue())){
			try {
				testPanel.showAnswer(message, QuestionPanelSelect.Answer.RIGHT, isCorrect);
			} catch (IOException e) {
				manager.handleException(e);
			}
		}else{
			try {
				testPanel.showAnswer(message, QuestionPanelSelect.Answer.NONE, isCorrect);
			} catch (IOException e) {
				manager.handleException(e);
			}
		}
	}
	
	/**
	 * Used to initialize the  isNumberSetUsed array to prevent the same images being shown
	 *  in a single question (asked to choose dime, but both panels would show dimes).
	 */
	private void initArrays(){
		for(int i = 0; i < Coin.values().length; i++){
			isNumberSetUsed[i][i][0] = true;
			isNumberSetUsed[i][i][1] = true;
		}
	}
	
	/**
	 * Used to set maxNumberOfQuestions to the number specified by difficulty.
	 *  See class difficulty setting constants.
	 */
	private void setDifficulty(){
		switch(difficulty){
			case EASY:   maxNumberOfQuestions = EASY_MAX_QUESTIONS; break;
			case NORMAL: maxNumberOfQuestions = NORM_MAX_QUESTIONS; break;
			case HARD:   maxNumberOfQuestions = HARD_MAX_QUESTIONS; break;
			default:     maxNumberOfQuestions = EASY_MAX_QUESTIONS; break;
		}
	}
	
	/**
	 * A helper method, used to obtain a random int between lowestValue and highestValue
	 *  (inclusive).
	 * @param lowestValue The lowest int value that the random number generator may return.
	 * @param highestValue The highest int value that the random number generator may return.
	 * @return A random int between lowestValue and highestValue (inclusive).
	 */
	private int getRandomInt(int lowestValue, int highestValue) {
		if(lowestValue > highestValue){
			int temp     = highestValue;
			highestValue = lowestValue;
			lowestValue  = temp;
		}
	    return rng.nextInt((highestValue - lowestValue) + 1) + lowestValue;
	}

	/**
	 * Used to compare a user's answer to the question's correct answer. Increments
	 *  numCorrect if the user's answer was correct. Returns true if the user's answer
	 *  was correct; false otherwise.
	 * @param answer A String representing the panel selected and submitted by the user.
	 * @return A boolean indicating true if the user's answer was correct; false otherwise.
	 */
	private boolean checkAnswer(String answer){
		if(answer.equals(answerString)){
			numCorrect++;
			return true;
		}else{
			logWrongAnswer();
			return false;
		}
	}
	
	/**
	 * A helper method, used to create a String that describes an incorrectly answered question.
	 *  Adds the created String to the wrongAnswers ArrayList.
	 */
	private void logWrongAnswer(){
		String question;
		String entry;
		if(questionType == NAME_TYPE){
			question = "Question " + (currentQuestionNum - 1) + ": Coin(" + correctAnswer + ")";
			entry    = question + " Student Answer: (" + wrongAnswer + ")"
									   + " Correct Answer: (" + correctAnswer + ").";
		}else{
			question = "Question " + (currentQuestionNum - 1) + ": Coin(" + correctAnswer + "\u00A2)";
			entry    = question + " Student Answer: (" + wrongAnswer + "\u00A2)"
									   + " Correct Answer: (" + correctAnswer + "\u00A2).";
		}
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
			String filePath = manager.getTestFolderPath() + "\\Coins\\";
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
	public void answered(String answer) {
		if(isPractice){
			showAnswer(answer);
		}
		//If the user didn't answer, do nothing, else check the answer.
		else if(!(answer.equals(QuestionPanelSelect.Answer.NONE.getStringValue()))){
			checkAnswer(answer);
			if(currentQuestionNum <= maxNumberOfQuestions){
				askQuestion();
			}else{
				testPanel.tearDown();
				if(clip.isActive())
					clip.stop();
				try {
					int grade = getGrade();
					boolean isBetter = isBetterGrade(numCorrect);
					String fileName = "Coins(" + difficulty.getName() + ")";
					if(isBetter)
						manager.setGrade(TEST_BUTTON, difficulty, numCorrect, maxNumberOfQuestions);
					makeTestDetailFile();
					new RewardScreen(manager, TEST_BUTTON, difficulty, grade, isBetter, manager.getRewardsFolderPath());
				}catch (IOException e) {
					manager.handleException(e);
				}
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
	
	/**
	 * This enum is used to define different US coins. Each coin is associated with a name, cent value,
	 *  and image file name.
	 * @author Kenneth Chin
	 */
	private enum Coin{
		PENNY  ("Penny"  ,"1" , "penny.png"),
		NICKEL ("Nickel" ,"5" , "nickel.png"),
		DIME   ("Dime"   ,"10", "dime.png"),
		QUARTER("Quarter","25", "quarter.png");
		
		private String filePath;
		private String centValue;
		private String name;
		
		private static final String imagePath = "\\images\\test\\coins\\";
		
		/**
		 * The Coin constructor.
		 * @param name A String describing this Coin's name.
		 * @param centValue A String describing this Coin's cent value.
		 * @param fileName A String describing this Coin's image file name.
		 */
		private Coin(String name, String centValue, String fileName){
			this.name      = name;
			this.centValue = centValue;
			this.filePath  = imagePath + fileName;
		}
		
		/**
		 * Used to obtain a String describing this Coin's name.
		 * @return A String describing this Coin's name.
		 */
		public String getName(){
			return name;
		}
		
		/**
		 * Used to obtain a String describing this Coin's cent value.
		 * @return A String describing this Coin's cent value.
		 */
		public String getCentValue(){
			return centValue;
		}
		
		/**
		 * Used to obtain a String describing this Coin's image file path from the program's root directory.
		 * @return A String describing this Coin's image file path from the program's root directory.
		 */
		public String getFilePath(){
			return filePath;
		}
	}
}