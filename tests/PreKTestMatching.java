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
import project.constants.AppleBoard;
import project.constants.DifficultyLevel;
import project.interfaces.ModuleSelectButtonInterface;
import project.interfaces.Questionable;
import project.interfaces.QuestionableObserver;
import project.interfaces.TestableObserver;
import project.run.GUIManager;
import project.screens.RewardScreen;
import project.tools.MainWindow;
import project.tools.QuestionPanelSelect;
import project.tools.TextFileMaker;

/**
 * This class is used to test PreK-K students on number Matching skills. It displays two selectable
 *  panels, each with a quantity of some object. A student is asked to select the panel that has the
 *  same number of objects as specified in a question, then click the "Submit" button. While a student
 *  may be asked to match the same number multiple times, the combination of panel quantities will
 *  never be used twice.
 * @author Kenneth Chin
 */
public class PreKTestMatching implements TestableObserver, Questionable{
	
	//The ModuleSelectButtonInterface that describes this test.
	private static final ModuleSelectButtonInterface TEST_BUTTON = PreKModuleSelectTestButtons.Button.MATCHING;

	private static final int LOWEST_INT    = 0;         //MUST be zero.
	private static final int HIGHEST_INT   = 10;        //The highest value that may be asked in a question.
	
	//Difficulty settings. Note: MUST be less than the maximum number of question permutations.
	private static final int EASY_MAX_QUESTIONS = 10;   //The maximum number of questions for the "Easy" difficulty.
	private static final int NORM_MAX_QUESTIONS = 15;   //The maximum number of questions for the "Normal" difficulty.
	private static final int HARD_MAX_QUESTIONS = 20;   //The maximum number of questions for the "Hard" difficulty.
	private int maxNumberOfQuestions = EASY_MAX_QUESTIONS; //The actual maximum number of questions for this test.
	
	private Random rng = new Random(System.currentTimeMillis()); //A random number generator.

	private boolean isPractice = false;   //Used to indicate that this test is a practice test.
	private DifficultyLevel difficulty;   //The current DifficultyLevel of this test.
	
	private int currentQuestionNum = 1;   //The current question number.
	private ArrayList<String> imagePaths; //The file paths of all AppleBoard images, in the order of the enum.
	//The set (correctAnswer, wrongAnswer). Used to determine if a correct answer's panel was used
	//  with a wrongAnswer's panel.
	private boolean[][] isNumberSetUsed = new boolean[(HIGHEST_INT + 2)][(HIGHEST_INT + 2)];
	private int correctAnswer;   //The value and index of the panel that is correct.
	private int wrongAnswer;     //The value and index of the panel that is incorrect.
	private String answerString; //The correct answer's panel String ("left" or "right").
	private int numCorrect = 0;  //The number of correct answers obtained from the user.
	private ArrayList<String> wrongAnswers = new ArrayList<String>(); //Used to track incorrect answers.
	
	private QuestionableObserver observer; //The QuestionableObserver that want's to be notified of a user's answer.
	private boolean isFinalTest = false;   //Used to determine if this is a cumulative(final) test or a standalone.
	private String userAnswer = "";        //Used to store the user's answer, so it may be passed to observer.
	private String wrongAnswerLogEntry = null; //The entry that a final test will write to the wrongAnswerLog.
	
	private QuestionPanelSelect testPanel; //The QuestionPanelSelect that will display questions.
	private Clip clip; //The audio clip used to play the tutorial sounds.
	
	private GUIManager manager;    //The GUIManager that manages mainWindow & all GUI screens.
	private MainWindow mainWindow; //The MainWindow that will have questions displayed on.
	
	/**
	 * The PreKTestMatching constructor. Creates and displays a Matching test for PreK-K students.
	 * @param manager The GUIManager that manages the primary MainWindow & all GUI screens.
	 * @param isPractice A boolean indicating true if this test is a practice test, false otherwise.
	 * @param difficulty The DifficultyLevel of this test.
	 * @throws IOException Thrown if any image file is missing.
	 */
	public PreKTestMatching(GUIManager manager, boolean isPractice, DifficultyLevel difficulty) throws IOException{
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
	 * Creates a PreKTestMatching object, without displaying anything. Used specifically for
	 *  final exams that implement QuestionableObserver. Use showQuestion(int) to display a
	 *  question.
	 * If isPractice == true, finalTest.answered(Questionable, String, String) will not be called
	 *  until after the user clicks the "Next" button (displayed while showing the answer).
	 * @param manager The GUIManager that manages the primary MainWindow.
	 * @param isPractice A boolean indicating true if this test is a practice test, false otherwise.
	 * @param difficulty The DifficultyLevel of this test.
	 * @param maxQuestions An int indicating the maximum number of questions that will be displayed in the
	 *  bottom-right corner of the screen. This should be the total number of questions on the final exam.
	 * @param finalTest The QuestionableObserver that wants to be notified when a user has entered an answer.
	 * @throws IOException Thrown if any image or audio file is missing.
	 */
	public PreKTestMatching(GUIManager manager, boolean isPractice, DifficultyLevel difficulty,
			int maxQuestions, QuestionableObserver finalTest) throws IOException{
		this.manager    = manager;
		this.mainWindow = manager.getMainWindow();
		this.isPractice = isPractice;
		this.difficulty = difficulty;
		
		setDifficulty();
		initArrays();
		
		maxNumberOfQuestions = maxQuestions;
		observer = finalTest;
		
		isFinalTest = true;
	}
	
	/**
	 * Used to play an audio tutorial, describing how to use this test.
	 */
	public void playTutorial(){
	    try{
	    	String filePath = "audio\\Test Tutorials\\Matching.wav";
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
	 * Used to obtain an unused combination of panel quantities.
	 */
	private void getValues(){
		do{
			correctAnswer = getRandomInt(LOWEST_INT, HIGHEST_INT);
			wrongAnswer   = getRandomInt(LOWEST_INT, HIGHEST_INT);
		}while(correctAnswer == wrongAnswer || isUsedQuestion(correctAnswer, wrongAnswer));
		isNumberSetUsed[correctAnswer][wrongAnswer] = true;
	}
	
	/**
	 * Used to generate question text, and randomize the panel order (left or right).
	 * Displays the question and two panels.
	 */
	private void makeQuestion(){
		String question    = "Which picture shows " + correctAnswer;
		question += (correctAnswer == 1) ? " apple?" : " apples?";
		AppleBoard[] board = AppleBoard.values();
		String leftImagePath;
		String rightImagePath;
		int answerLoc = getRandomInt(0, 1);
		if(answerLoc == 0){
			answerString   = QuestionPanelSelect.Answer.LEFT.getStringValue();
			leftImagePath  = board[correctAnswer].getPath();
			rightImagePath = board[wrongAnswer].getPath();
		}
		else{
			answerString   = QuestionPanelSelect.Answer.RIGHT.getStringValue();
			leftImagePath  = board[wrongAnswer].getPath();
			rightImagePath = board[correctAnswer].getPath();
		}
		try {
			testPanel.showQuestion(question, (currentQuestionNum), leftImagePath, rightImagePath);
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
	 * Used to determine if the combination of numberToMatch and aDifferentNumber
	 *  has been used in a previous question.
	 * @param numberToMatch An int. The number that the user is asked to match.
	 * @param aDifferentNumber An int. The number of objects on the panel that does not have the
	 *  quantity that the user is asked to match.
	 * @return A boolean. True if the combination of numberToMatch and aDifferentNumber
	 *  has been used in a previous question; false otherwise.
	 */
	private boolean isUsedQuestion(int numberToMatch, int aDifferentNumber){
		return isNumberSetUsed[numberToMatch][aDifferentNumber];
	}
	
	/**
	 * Used to initialize the imagePaths and isNumberSetUsed arrays. The imagePaths arrays are
	 *  set to the values specified in the AppleBoard enum. The isNumberSetUsed array is set
	 *  to prevent equal number combinations from being asked (asked to match 5, but both panels
	 *  would show 5 objects).
	 */
	private void initArrays(){
		imagePaths       = new ArrayList<String>();
		AppleBoard[] board = AppleBoard.values();
		for(int i = 0; i < board.length; i++){
			imagePaths.add(board[i].getPath());
			isNumberSetUsed[i][i] = true;
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
		String question = "Question " + (currentQuestionNum - 1) + ": Match(" + correctAnswer + ")";
		String entry    = question + " Student Answer: (" + wrongAnswer + ")"
								   + " Correct Answer: (" + correctAnswer + ").";
		if(isFinalTest)
			wrongAnswerLogEntry = entry;
		else
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
			String filePath = manager.getTestFolderPath() + "\\Matching\\";
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
		if(isPractice && !isFinalTest){
			showAnswer(answer);
		}
		//If the user didn't answer, do nothing, else check the answer.
		else if(!(answer.equals(QuestionPanelSelect.Answer.NONE.getStringValue()))){
			if(isFinalTest){
				if(clip.isActive())
					clip.stop();
				userAnswer = answer;
				if(isPractice)
					showAnswer(answer);
				else{
					testPanel.tearDown();
					observer.answered(this, checkAnswer(answer), wrongAnswerLogEntry);
				}
			}else{
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
	}
	
	@Override
	public void nextClicked() {
		if(isPractice){
			if(isFinalTest){
				testPanel.tearDown();
				observer.answered(this, checkAnswer(userAnswer), null);
			}else if(currentQuestionNum <= maxNumberOfQuestions){
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
	
	@Override
	public void showQuestion(int questionNum) throws IOException{
		testPanel = new QuestionPanelSelect(mainWindow, maxNumberOfQuestions);
		testPanel.registerObserver(this);
		playTutorial();
		currentQuestionNum = questionNum;
		userAnswer = "";
		wrongAnswerLogEntry = null;
		askQuestion();
	}
}