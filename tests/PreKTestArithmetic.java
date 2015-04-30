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
import project.constants.DifficultyLevel;
import project.constants.Operator;
import project.interfaces.ModuleSelectButtonInterface;
import project.interfaces.Questionable;
import project.interfaces.QuestionableObserver;
import project.interfaces.TestableObserver;
import project.run.GUIManager;
import project.screens.RewardScreen;
import project.tools.MainWindow;
import project.tools.QuestionPanelText;
import project.tools.TextFileMaker;

/**
 * This class is used to test PreK-K students on Addition and Subtraction skills for integers
 *  ranging from 0 to 10. No question will result in a negative answer. No two questions will
 *  be the same. Students will answer a question via a text box and "Submit" button.
 * @author Kenneth Chin
 */
public class PreKTestArithmetic implements TestableObserver, Questionable{
	
	//The ModuleSelectButtonInterface that describes this test.
	private static final ModuleSelectButtonInterface TEST_BUTTON = PreKModuleSelectTestButtons.Button.ARITHMETIC;
	
	private static final int LOWEST_INT       = 0;      //MUST always be zero!
	private static final int HIGHEST_INT      = 10;     //The highest value int that may be tested.
	private static final int NUM_OF_OPERATORS = 2;      //The number of Operators to test. See TODO in constructor.
	
	//Difficulty settings. Note: MUST be less than the maximum number of question permutations.
	private static final int EASY_MAX_QUESTIONS = 10;   //The maximum number of questions for the "Easy" difficulty.
	private static final int NORM_MAX_QUESTIONS = 15;   //The maximum number of questions for the "Normal" difficulty.
	private static final int HARD_MAX_QUESTIONS = 20;   //The maximum number of questions for the "Hard" difficulty.
	private int maxNumberOfQuestions = EASY_MAX_QUESTIONS; //The actual maximum number of questions for this test.
	
	private Random rng = new Random(System.currentTimeMillis()); //A random number generator.

	private boolean isPractice = false;   //Used to indicate that this test is a practice test.
	private DifficultyLevel difficulty;   //The current DifficultyLevel of this test.
	
	private int currentQuestionNum = 1;   //The current number of questions that have been asked.
	
	//The set of all possible questions that may be asked, associated with a boolean indicating if
	// the question has been asked.
	private boolean[][][] isNumberSetUsed = new boolean[(HIGHEST_INT + 2)][(HIGHEST_INT + 2)][NUM_OF_OPERATORS];
	private int currentAnswer;  //The answer to the current question.
	private int numCorrect = 0; //The number of correctly answered questions.
	private ArrayList<String> wrongAnswers = new ArrayList<String>(); //Used to track incorrect answers.
	private String currentQuestion; //The current question in String form.
	
	private QuestionableObserver observer; //The QuestionableObserver that want's to be notified of a user's answer.
	private boolean isFinalTest = false;   //Used to determine if this is a cumulative(final) test or a standalone.
	private String userAnswer = "";        //Used to store the user's answer, so it may be passed to observer.
	private String wrongAnswerLogEntry = null; //The entry that a final test will write to the wrongAnswerLog.
	
	private QuestionPanelText testPanel; //The QuestionPanelText used to ask questions.
	private Clip clip; //The audio clip used to play the tutorial sounds.
	
	private GUIManager manager;    //The GUIManager that manages mainWindow & all GUI screens.
	private MainWindow mainWindow; //The MainWindow that is to have questions displayed on.
	
	/**
	 * The PreKTestAritmetic constructor. Creates and displays an Arithmetic test for PreK-K students.
	 * @param manager The GUIManager that manages the primary MainWindow & all GUI screens.
	 * @param isPractice A boolean indicating true if this test is a practice test, false otherwise.
	 * @param difficulty The DifficultyLevel of this test.
	 * @throws IOException Thrown if any image file is missing.
	 */
	public PreKTestArithmetic(GUIManager manager, boolean isPractice, DifficultyLevel difficulty) throws IOException{
		this.manager    = manager;
		this.mainWindow = manager.getMainWindow();
		this.isPractice = isPractice;
		this.difficulty = difficulty;
		setDifficulty();
		
		//TODO enable this if(NUM_OF_OPERATORS >= 4. May implement if Division is to be supported.
		//if(NUM_OF_OPERATORS >= 4)
		//	initSetUsed();
		testPanel = new QuestionPanelText(mainWindow, maxNumberOfQuestions);
		testPanel.registerObserver(this);
		playTutorial();
		makeAndShowQuestion();
	}
	
	/**
	 * Creates a PreKTestArithmetic object, without displaying anything. Used specifically for
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
	public PreKTestArithmetic(GUIManager manager, boolean isPractice, DifficultyLevel difficulty,
			int maxQuestions, QuestionableObserver finalTest) throws IOException{
		this.manager    = manager;
		this.mainWindow = manager.getMainWindow();
		this.isPractice = isPractice;
		this.difficulty = difficulty;
		setDifficulty();
		
		//TODO enable this if(NUM_OF_OPERATORS >= 4. May implement if Division is to be supported.
		//if(NUM_OF_OPERATORS >= 4)
		//	initSetUsed();
		
		maxNumberOfQuestions = maxQuestions;
		observer = finalTest;
		
		isFinalTest = true;
	}
	
	/**
	 * Used to play an audio tutorial, describing how to use this test.
	 */
	public void playTutorial(){
	    try{
	    	String filePath = "audio\\Test Tutorials\\Arithmetic.wav";
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
	        clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    }catch(Exception e) {
	        manager.handleException(e);
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
	 * A helper method used to determine if a number set has been used in a question.
	 * (Honestly, it's just to help the writer to remember what the indexes are supposed to represent.)
	 * @param operandIndex1 The index of the first operand in the question.
	 * @param operandIndex2 The index of the second operand in the question.
	 * @param operatorIndex The index of the operator, equal to its index in Operator.values().
	 * @return A boolean indicating true if this number set has been asked in a question. False otherwise.
	 */
	private boolean isUsedQuestion(int operandIndex1, int operandIndex2, int operatorIndex){
		return isNumberSetUsed[operandIndex1][operandIndex2][operatorIndex];
	}
	
	/**
	 * Used to display a question in the MainWindow. Generates an unused set of two operands and an
	 *  operator (Addition or Subtraction), then displays the question.
	 */
	private void makeAndShowQuestion(){
		int operand1;
		int operand2;
		int operatorIndex;
		Operator operator;
		int tempAnswer;
		do{
			operand1      = getRandomInt(LOWEST_INT, HIGHEST_INT);
			operand2      = getRandomInt(LOWEST_INT, HIGHEST_INT);
			operatorIndex = getRandomInt(LOWEST_INT, (NUM_OF_OPERATORS - 1));
			operator      = getOperator(operatorIndex);
			tempAnswer    = operator.doAction(operand1, operand2);
			//Prevent negative answers.
			if(tempAnswer < 0)
				isNumberSetUsed[operand1][operand2][operatorIndex] = true;
		}while(isUsedQuestion(operand1, operand2, operatorIndex));
		
		currentQuestion   = operand1 + " " + operator.getSymbol() + " " + operand2;
		currentAnswer     = operator.doAction(operand1, operand2);
		isNumberSetUsed[operand1][operand2][operatorIndex] = true;
		showQuestion(operand1, operand2, operator);
	}
	
	/**
	 * A helper method used to display a question in the MainWindow.
	 * @param operand1 An int. The first operand.
	 * @param operand2 An int. The second operand.
	 * @param operator An Operator, as defined by the Operator enum.
	 */
	private void showQuestion(int operand1, int operand2, Operator operator){
		String question = "Solve: <br><br>";
		testPanel.showEquation(question, (currentQuestionNum), operand1, operand2, operator);
		currentQuestionNum++;
	}
	
	/**
	 * A helper method used to associate a randomly generated int to an Operator type.
	 * @param operatorIndex The index of an Operator as returned by Operator.values().
	 * @return The Operator type specified by the given index.
	 */
	private Operator getOperator(int operatorIndex){
		switch(operatorIndex){
			case  0: return Operator.ADD;
			case  1: return Operator.SUBTRACT;
			//case  2: return Operator.MULTIPLY;
			//case  3: return Operator.DIVIDE;
			default: return getOperator((operatorIndex % NUM_OF_OPERATORS));
		}
	}
	
	/**
	 * Used to display a practice test's answer after a user has submitted an answer.
	 * @param answer A String indicating the user's answer to the current question.
	 */
	private void showAnswer(String answer){
		boolean isCorrect = checkAnswer(answer);
		String  message;
		if(isCorrect)
			message = "\"" + answer + "\"" + " is correct!";
		else
			message = "\"" + answer + "\""
					+ " is incorrect! The correct answer is shown above.";
		try {
			testPanel.showEquationAnswer("<font color=\"#00FF00\">" + currentAnswer + "</font>", message, isCorrect);
		} catch (IOException e) {
			manager.handleException(e);
		}
	}
	
	/**
	 * Used to compare a user's answer to the question's correct answer. Increments
	 *  numCorrect if the user's answer was correct. If the user's answer was incorrect,
	 *  the question, the question's answer, and correct answer are added to the
	 *  wrongAnswers array. Returns true if the user's answer  was correct; false otherwise.
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
			String filePath = manager.getTestFolderPath() + "\\Arithmetic\\";
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
		else if(isFinalTest){
			if(clip.isActive())
				clip.stop();
			userAnswer = answer;
			if(isPractice)
				showAnswer(answer);
			else{
				testPanel.tearDown();
				observer.answered(this, checkAnswer(answer), wrongAnswerLogEntry);
			}
		}
		else if(currentQuestionNum <= maxNumberOfQuestions){
			checkAnswer(answer);
			makeAndShowQuestion();
		}else{
			checkAnswer(answer);
			testPanel.tearDown();
			if(clip.isActive())
				clip.stop();
			try{
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
	
	@Override
	public void nextClicked() {
		if(isPractice){
			if(isFinalTest){
				testPanel.tearDown();
				observer.answered(this, checkAnswer(userAnswer), null);
			}else if(currentQuestionNum <= maxNumberOfQuestions){
				makeAndShowQuestion();
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
		testPanel = new QuestionPanelText(mainWindow, maxNumberOfQuestions);
		testPanel.registerObserver(this);
		playTutorial();
		currentQuestionNum = questionNum;
		userAnswer = "";
		wrongAnswerLogEntry = null;
		makeAndShowQuestion();
	}
}
