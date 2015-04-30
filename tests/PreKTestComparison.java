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
 * This class is used to test PreK-K students on number Comparison skills. It displays two selectable
 *  panels, each with a quantity of some object. A student is asked to select the panel that answers
 *  a question, then click the "Submit" button. There are 8 question types described by the words
 *  they test on. The word for each question type is more, greater than, most, same, fewer, less than,
 *  least, or none. 
 *  
 *  
 *  While a student may be asked to match the same number multiple times, the combination of panel quantities will
 *  never be used twice.
 * @author Kenneth Chin
 */
public class PreKTestComparison implements TestableObserver, Questionable{
	
	//The ModuleSelectButtonInterface that describes this test.
	private static final ModuleSelectButtonInterface TEST_BUTTON = PreKModuleSelectTestButtons.Button.COMPARISON;

	private static final int LOWEST_INT    = 0;         //MUST be zero.
	private static final int HIGHEST_INT   = 10;        //The highest value that may be asked in a question.
	
	//Difficulty settings. Note: MUST be less than the maximum number of question permutations.
	private static final int EASY_MAX_QUESTIONS = 10;   //The maximum number of questions for the "Easy" difficulty.
	private static final int NORM_MAX_QUESTIONS = 15;   //The maximum number of questions for the "Normal" difficulty.
	private static final int HARD_MAX_QUESTIONS = 20;   //The maximum number of questions for the "Hard" difficulty.
	private int maxNumberOfQuestions = EASY_MAX_QUESTIONS; //The actual maximum number of questions for this test.
	
	private static Random rng = new Random(System.currentTimeMillis()); //A random number generator.

	private boolean isPractice = false;   //Used to indicate that this test is a practice test.
	private DifficultyLevel difficulty;   //The current DifficultyLevel of this test.
	
	private int currentQuestionNum = 1;   //The current question number.
	private ArrayList<String> imagePaths; //The file paths of all AppleBoard images, in the order of the enum.
	//The set (comparisonPoint, correctAnswer, wrongAnswer, questionType). Used to determine if a correct answer's panel was used
	//  with a wrongAnswer's panel for the specified questionType & comparisonPoint.
	private static boolean[][][][] isNumberSetUsed = new boolean[HIGHEST_INT + 2][(HIGHEST_INT + 2)][(HIGHEST_INT + 2)][Comparison.values().length];
	private static int comparisonPoint; //The value that correctAnswer and wrongAnswer are compared to. Used to develop a question.
	private static int correctAnswer;   //The value and index of the panel that is correct.
	private static int wrongAnswer;     //The value and index of the panel that is incorrect.
	private int questionType;    //The index of Comparison.values() that describes this question's type.
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
	 * The PreKTestComparison constructor. Creates and displays a Compare test for PreK-K students.
	 * @param manager The GUIManager that manages the primary MainWindow & all GUI screens.
	 * @param isPractice A boolean indicating true if this test is a practice test, false otherwise.
	 * @param difficulty The DifficultyLevel of this test.
	 * @throws IOException Thrown if any image file is missing.
	 */
	public PreKTestComparison(GUIManager manager, boolean isPractice, DifficultyLevel difficulty) throws IOException{
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
	 * Creates a PreKTestEstimate object, without displaying anything. Used specifically for
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
	public PreKTestComparison(GUIManager manager, boolean isPractice, DifficultyLevel difficulty,
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
	    	String filePath = "audio\\Test Tutorials\\Comparison.wav";
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
	 * Used to obtain an unused combination of panel quantities & question values.
	 */
	private void getValues(){
		boolean isValuesSet = false;
		Comparison[] types = Comparison.values();
		do{
			questionType = getRandomInt(0, types.length - 1);
			isValuesSet = types[questionType].getValues();
		}while(!isValuesSet);
	}
	
	/**
	 * Used to generate question text, and randomize the panel order (left or right).
	 * Displays the question and two panels.
	 */
	private void makeQuestion(){
		Comparison[] types = Comparison.values();
		AppleBoard[] board = AppleBoard.values();
		String question;
		String leftImagePath;
		String rightImagePath;
		//Change the displayed number based on question type.
		if(questionType == Comparison.NONE.ordinal())
			question = types[questionType].getQuestionString(wrongAnswer);
		else
			question = types[questionType].getQuestionString(comparisonPoint);
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
	 * Used to initialize the imagePaths and isNumberSetUsed arrays. The imagePaths arrays are
	 *  set to the values specified in the AppleBoard enum. The isNumberSetUsed array is initialized
	 *  by each individual Comparison enum.
	 */
	private void initArrays(){
		imagePaths       = new ArrayList<String>();
		AppleBoard[] board = AppleBoard.values();
		for(int i = 0; i < board.length; i++){
			imagePaths.add(board[i].getPath());
		}
		for(Comparison type: Comparison.values()){
			type.initUsedArray();
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
	private static int getRandomInt(int lowestValue, int highestValue) {
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
		String entry = "Question " + (currentQuestionNum - 1) + ": Compare"
							+ Comparison.values()[questionType].getLogEntry();
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
			String filePath = manager.getTestFolderPath() + "\\Compare\\";
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
	
	/**
	 * This enum is used as the implementation of the state pattern for PreKTestComparison.
	 * This enum is used to differentiate question types & their functions. Each question type
	 *  has its own question format, set of valid values for comparisonPoint, correctAnswer, & wrongAnswer,
	 *  and entry format for the "wrong answer file".
	 * The fourth value in isNumberSetUsed[][][][fourth value] is directly related to each Comparison.ordinal.
	 * @author Kenneth Chin
	 */
	private enum Comparison{
		MORE() {
			@Override
			public String getQuestionString(int number) {
				return "Which picture has <b><u>more</u></b> apples?";
			}

			@Override
			protected void initUsedArray() {
				for(int comparisonPoint = 0; comparisonPoint < HIGHEST_INT + 2; comparisonPoint++){
					for(int answer = 0; answer < HIGHEST_INT + 2; answer++){
						for(int wrong = 0; wrong < 12; wrong++){
							//All answer > comparisonPoint && wrong <= answer should be false; all others true.
							if(answer <= comparisonPoint || (answer > comparisonPoint && wrong > comparisonPoint)){
								isNumberSetUsed[comparisonPoint][answer][wrong][this.ordinal()] = true;
							}
						}
					}
				}
			}

			//Since there are 286 possible values, it is assumed that a set of values can be obtained.
			@Override
			protected boolean getValues() {
				do{
					comparisonPoint = getRandomInt(LOWEST_INT, HIGHEST_INT - 1);
					correctAnswer   = getRandomInt(comparisonPoint + 1, HIGHEST_INT);
					wrongAnswer     = getRandomInt(LOWEST_INT, comparisonPoint);
				}while(correctAnswer == wrongAnswer
						|| isNumberSetUsed[comparisonPoint][correctAnswer][wrongAnswer][this.ordinal()]);
				isNumberSetUsed[comparisonPoint][correctAnswer][wrongAnswer][this.ordinal()] = true;
				return true;
			}

			@Override
			protected String getLogEntry() {
				return "(Which is MORE?) Student Answer: (" + wrongAnswer + ")"
						   + " Correct Answer: (" + correctAnswer + ").";
			}
		},
		GREATER_THAN() {
			@Override
			public String getQuestionString(int number) {
				return "Which picture has a number of apples <b><u>greater than</u></b> " + number + "?";
			}
			
			@Override
			protected void initUsedArray() {
				for(int comparisonPoint = 0; comparisonPoint < HIGHEST_INT + 2; comparisonPoint++){
					for(int answer = 0; answer < HIGHEST_INT + 2; answer++){
						for(int wrong = 0; wrong < 12; wrong++){
							//All answer > comparisonPoint && wrong <= answer should be false; all others true.
							if(answer <= comparisonPoint || (answer > comparisonPoint && wrong > comparisonPoint)){
								isNumberSetUsed[comparisonPoint][answer][wrong][this.ordinal()] = true;
							}
						}
					}
				}
			}
			
			//Since there are 286 possible values, it is assumed that a set of values can be obtained.
			@Override
			protected boolean getValues() {
				do{
					comparisonPoint = getRandomInt(LOWEST_INT, HIGHEST_INT - 1);
					correctAnswer   = getRandomInt(comparisonPoint + 1, HIGHEST_INT);
					wrongAnswer     = getRandomInt(LOWEST_INT, comparisonPoint);
				}while(correctAnswer == wrongAnswer
						|| isNumberSetUsed[comparisonPoint][correctAnswer][wrongAnswer][this.ordinal()]);
				isNumberSetUsed[comparisonPoint][correctAnswer][wrongAnswer][this.ordinal()] = true;
				return true;
			}
			
			@Override
			protected String getLogEntry() {
				return "(Greater than " + comparisonPoint + ") Student Answer: (" + wrongAnswer + ")"
						   + " Correct Answer: (" + correctAnswer + ").";
			}
		},
		MOST() {
			@Override
			public String getQuestionString(int number) {
				return "Which picture has the <b><u>most</u></b> apples?";
			}
			@Override
			protected void initUsedArray() {
				for(int comparisonPoint = 0; comparisonPoint < HIGHEST_INT + 2; comparisonPoint++){
					for(int answer = 0; answer < HIGHEST_INT + 2; answer++){
						for(int wrong = 0; wrong < 12; wrong++){
							//All answer > comparisonPoint && wrong <= answer should be false; all others true.
							if(answer <= comparisonPoint || (answer > comparisonPoint && wrong > comparisonPoint)){
								isNumberSetUsed[comparisonPoint][answer][wrong][this.ordinal()] = true;
							}
						}
					}
				}
			}
			
			//Since there are 286 possible values, it is assumed that a set of values can be obtained.
			@Override
			protected boolean getValues() {
				do{
					comparisonPoint = getRandomInt(LOWEST_INT, HIGHEST_INT - 1);
					correctAnswer   = getRandomInt(comparisonPoint + 1, HIGHEST_INT);
					wrongAnswer     = getRandomInt(LOWEST_INT, comparisonPoint);
				}while(correctAnswer == wrongAnswer
						|| isNumberSetUsed[comparisonPoint][correctAnswer][wrongAnswer][this.ordinal()]);
				isNumberSetUsed[comparisonPoint][correctAnswer][wrongAnswer][this.ordinal()] = true;
				return true;
			}
			
			@Override
			protected String getLogEntry() {
				return "(Which is MOST?) Student Answer: (" + wrongAnswer + ")"
						   + " Correct Answer: (" + correctAnswer + ").";
			}
		},
		SAME() {
			@Override
			public String getQuestionString(int number) {
				String appleTense = (number == 1) ? "apple" : "apples";
				return "Which picture is the <b><u>same</u></b> as " + number + " " + appleTense + "?";
			}
			
			@Override
			protected void initUsedArray() {
				for(int comparisonPoint = 0; comparisonPoint < HIGHEST_INT + 2; comparisonPoint++){
					for(int answer = 0; answer < HIGHEST_INT + 2; answer++){
						for(int wrong = 0; wrong < 12; wrong++){
							//All answer == comparisonPoint && wrong != answer should be false; all others true.
							if(answer != comparisonPoint || (answer == comparisonPoint && wrong == answer)){
								isNumberSetUsed[comparisonPoint][answer][wrong][this.ordinal()] = true;
							}
						}
					}
				}
			}
			
			//Since there are 132 possible values, it is assumed that a set of values can be obtained.
			@Override
			protected boolean getValues() {
				do{
					comparisonPoint = getRandomInt(LOWEST_INT, HIGHEST_INT);
					correctAnswer   = comparisonPoint;
					wrongAnswer     = getRandomInt(LOWEST_INT, HIGHEST_INT);
				}while(correctAnswer == wrongAnswer
						|| isNumberSetUsed[comparisonPoint][correctAnswer][wrongAnswer][this.ordinal()]);
				isNumberSetUsed[comparisonPoint][correctAnswer][wrongAnswer][this.ordinal()] = true;
				return true;
			}
			
			@Override
			protected String getLogEntry() {
				return "(Which is the SAME as " + correctAnswer + "?) Student Answer: (" + wrongAnswer + ")"
						   + " Correct Answer: (" + correctAnswer + ").";
			}
		},
		FEWER {
			@Override
			public String getQuestionString(int number) {
				return "Which picture has <b><u>fewer</u></b> apples? ";
			}
			
			@Override
			protected void initUsedArray() {
				for(int comparisonPoint = 0; comparisonPoint < HIGHEST_INT + 2; comparisonPoint++){
					for(int answer = 0; answer < HIGHEST_INT + 2; answer++){
						for(int wrong = 0; wrong < 12; wrong++){
							//All answer < comparisonPoint && wrong >= comparisonPoint should be false; all others true.
							if(answer >= comparisonPoint || wrong < comparisonPoint ||
									(answer < comparisonPoint && wrong < comparisonPoint && wrong >= answer)){
								isNumberSetUsed[comparisonPoint][answer][wrong][this.ordinal()] = true;
							}
						}
					}
				}
			}
			
			//Since there are 286 possible values, it is assumed that a set of values can be obtained.
			@Override
			protected boolean getValues() {
				do{
					comparisonPoint = getRandomInt(LOWEST_INT + 1, HIGHEST_INT);
					correctAnswer   = getRandomInt(LOWEST_INT, comparisonPoint - 1);
					wrongAnswer     = getRandomInt(LOWEST_INT, comparisonPoint);
				}while(correctAnswer == wrongAnswer
						|| isNumberSetUsed[comparisonPoint][correctAnswer][wrongAnswer][this.ordinal()]);
				isNumberSetUsed[comparisonPoint][correctAnswer][wrongAnswer][this.ordinal()] = true;
				return true;
			}
			
			@Override
			protected String getLogEntry() {
				return "(Which is FEWER?) Student Answer: (" + wrongAnswer + ")"
						   + " Correct Answer: (" + correctAnswer + ").";
			}
		},
		LESS_THAN {
			@Override
			public String getQuestionString(int number) {
				return "Which picture has a number of apples <b><u>less than</u></b> " + number + "?";
			}
			
			@Override
			protected void initUsedArray() {
				for(int comparisonPoint = 0; comparisonPoint < HIGHEST_INT + 2; comparisonPoint++){
					for(int answer = 0; answer < HIGHEST_INT + 2; answer++){
						for(int wrong = 0; wrong < 12; wrong++){
							//All answer < comparisonPoint && wrong >= comparisonPoint should be false; all others true.
							if(answer >= comparisonPoint || wrong < comparisonPoint ||
									(answer < comparisonPoint && wrong < comparisonPoint && wrong >= answer)){
								isNumberSetUsed[comparisonPoint][answer][wrong][this.ordinal()] = true;
							}
						}
					}
				}
			}
			
			//Since there are 286 possible values, it is assumed that a set of values can be obtained.
			@Override
			protected boolean getValues() {
				do{
					comparisonPoint = getRandomInt(LOWEST_INT + 1, HIGHEST_INT);
					correctAnswer   = getRandomInt(LOWEST_INT, comparisonPoint - 1);
					wrongAnswer     = getRandomInt(LOWEST_INT, comparisonPoint);
				}while(correctAnswer == wrongAnswer
						|| isNumberSetUsed[comparisonPoint][correctAnswer][wrongAnswer][this.ordinal()]);
				isNumberSetUsed[comparisonPoint][correctAnswer][wrongAnswer][this.ordinal()] = true;
				return true;
			}
			
			@Override
			protected String getLogEntry() {
				return "(Less than " + comparisonPoint + ") Student Answer: (" + wrongAnswer + ")"
						   + " Correct Answer: (" + correctAnswer + ").";
			}
		},
		LEAST {
			@Override
			public String getQuestionString(int number) {
				return "Which picture has the <b><u>least</u></b> apples?";
			}
			
			@Override
			protected void initUsedArray() {
				for(int comparisonPoint = 0; comparisonPoint < HIGHEST_INT + 2; comparisonPoint++){
					for(int answer = 0; answer < HIGHEST_INT + 2; answer++){
						for(int wrong = 0; wrong < 12; wrong++){
							//All answer < comparisonPoint && wrong >= comparisonPoint should be false; all others true.
							if(answer >= comparisonPoint || wrong < comparisonPoint ||
									(answer < comparisonPoint && wrong < comparisonPoint && wrong >= answer)){
								isNumberSetUsed[comparisonPoint][answer][wrong][this.ordinal()] = true;
							}
						}
					}
				}
			}
			
			//Since there are 286 possible values, it is assumed that a set of values can be obtained.
			@Override
			protected boolean getValues() {
				do{
					comparisonPoint = getRandomInt(LOWEST_INT + 1, HIGHEST_INT);
					correctAnswer   = getRandomInt(LOWEST_INT, comparisonPoint - 1);
					wrongAnswer     = getRandomInt(LOWEST_INT, comparisonPoint);
				}while(correctAnswer == wrongAnswer
						|| isNumberSetUsed[comparisonPoint][correctAnswer][wrongAnswer][this.ordinal()]);
				isNumberSetUsed[comparisonPoint][correctAnswer][wrongAnswer][this.ordinal()] = true;
				return true;
			}
			
			@Override
			protected String getLogEntry() {
				return "(Which is LEAST?) Student Answer: (" + wrongAnswer + ")"
						   + " Correct Answer: (" + correctAnswer + ").";
			}
		},
		NONE {
			@Override
			public String getQuestionString(int number) {
				String appleTense = (number == 1) ? "apple" : "apples";
				String question = "One picture has " + number + " " + appleTense + ".<br>"
						+ "The other has none.<br>"
						+ "Choose the picture that has <b><u>none</u></b>.";
				return question;
			}
			
			@Override
			protected void initUsedArray() {
				for(int comparisonPoint = 0; comparisonPoint < HIGHEST_INT + 2; comparisonPoint++){
					for(int answer = 0; answer < HIGHEST_INT + 2; answer++){
						for(int wrong = 0; wrong < 12; wrong++){
							//All answer = 0 && wrong != 0 should be false; all others true.
							if(comparisonPoint != 0 || answer != 0 ||
									(comparisonPoint == 0 && answer == 0 && wrong == answer)){
								isNumberSetUsed[comparisonPoint][answer][wrong][this.ordinal()] = true;
							}
						}
					}
				}
			}
			
			@Override
			protected boolean getValues() {
				boolean isUsed = true;
				do{
					
					comparisonPoint = 0;
					correctAnswer   = 0;
					wrongAnswer     = getRandomInt(1, HIGHEST_INT);
					
					isUsed = isNumberSetUsed[comparisonPoint][correctAnswer][wrongAnswer][this.ordinal()];
					//Check if all questions have been asked.
					if(isUsed){
						for(int i = 1; i < HIGHEST_INT + 2; i++){
							boolean isThisUsed = isNumberSetUsed[0][0][i][this.ordinal()];
							if(!isThisUsed)
								break;
							else if(isThisUsed && i == HIGHEST_INT + 1)
								return false;
						}
					}
				}while(isUsed);
				isNumberSetUsed[comparisonPoint][correctAnswer][wrongAnswer][this.ordinal()] = true;
				return true;
			}
			
			@Override
			protected String getLogEntry() {
				return "(Which is NONE?) Student Answer: (" + wrongAnswer + ")"
						   + " Correct Answer: (" + correctAnswer + ").";
			}
		};
		
		/**
		 * Used to obtain a String that describes the question being asked by this Comparison.
		 * NOTE: This method should only be used after Comparison.getValues().
		 * @param number An int. A number that may be displayed. For Comparison.NONE this number is
		 *  the "wrong panel's" quantity. For all other Comparison enums, this number is the
		 *  number that the panel quantities are compared to.
		 * @return A String that describes the question being asked by this Comparison.
		 */
		public abstract String getQuestionString(int number);
		
		/**
		 * Used to initialize the "isNumberSetUsed" array for this Comparison. All
		 *  {comparisonPoint, correctAnswer, wrongAnswer, this.ordinal()} combinations that are invalid
		 *  are set to true.
		 */
		protected abstract void initUsedArray();
		
		/**
		 * Used to set comparisonPoint, correctAnswer, & wrongAnswer to an unused set of values that
		 *  may be used to ask a question.
		 * NOTE: Since all but Comparison.NONE have over 100 possible questions it is assumed
		 *  that there will always be an unused question available. However, in Comparison.NONE's case
		 *  there are only 11 possible questions. Therefore the availability of questions in
		 *  Comparison.NONE is not assumed.
		 * @return A boolean that is assumed true. If true is returned, then it is assumed that
		 *  comparisonPoint, correctAnswer, & wrongAnswer have been set. Otherwise, the values have
		 *  not been set (because all available questions have been used).
		 */
		protected abstract boolean getValues();
		
		/**
		 * Used to obtain a String that is formatted for logWrongAnswer()'s array entry.
		 * The format is "(Abbreviated question) Student Answer: (wrongAnswer) Correct Answer: (correctAnswer).".
		 * @return A String that is formatted for logWrongAnswer()'s array entry.
		 */
		protected abstract String getLogEntry();
	}
}