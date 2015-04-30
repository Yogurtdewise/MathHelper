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
 * This class is used to test PreK-K students on number Estimation skills. It displays two
 *  selectable panels, each with a number of apples. A student is asked to select the panel that
 *  closest in value to an image specified in a question, then click the "Submit" button. While a
 *  student may be asked to estimate the same value multiple times, the combination of answer images
 *  and question values will never be used twice.
 * @author Kenneth Chin
 */
public class PreKTestEstimate implements TestableObserver, Questionable{
	
	//The ModuleSelectButtonInterface that describes this test.
	private static final ModuleSelectButtonInterface TEST_BUTTON = PreKModuleSelectTestButtons.Button.ESTIMATE;
	
	//Difficulty settings. Note: MUST be less than the maximum number of question permutations.
	private static final int EASY_MAX_QUESTIONS = 10;   //The maximum number of questions for the "Easy" difficulty.
	private static final int NORM_MAX_QUESTIONS = 15;   //The maximum number of questions for the "Normal" difficulty.
	private static final int HARD_MAX_QUESTIONS = 20;   //The maximum number of questions for the "Hard" difficulty.
	private int maxNumberOfQuestions = EASY_MAX_QUESTIONS; //The actual maximum number of questions for this test.
	
	private Random rng = new Random(System.currentTimeMillis()); //A random number generator.

	private boolean isPractice = false;   //Used to indicate that this test is a practice test.
	private DifficultyLevel difficulty;   //The current DifficultyLevel of this test.
	
	private int currentQuestionNum = 1;   //The current question number.

	//The set of all questions; comparisonPoint, correctAnswer, wrongAnswer
	private boolean[][][] isNumberSetUsed = new boolean[Value.values().length][Value.values().length][Value.values().length];
	private int correctAnswer;      //The value of the correct answer.
	private int wrongAnswer;        //The value of the wrong answer.
	private int comparisonPoint;    //The value that whose estimate is to be compared to.
	private String answerString;    //The correct answer's panel String ("left" or "right").
	private int numCorrect = 0;     //The number of correct answers obtained from the user.
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
	 * The PreKTestEstimate constructor. Creates and displays an Estimate test for PreK-K students.
	 * @param manager The GUIManager that manages the primary MainWindow and all GUI screens.
	 * @param isPractice A boolean indicating true if this test is a practice test, false otherwise.
	 * @param difficulty The DifficultyLevel of this test.
	 * @throws IOException Thrown if any image file is missing.
	 */
	public PreKTestEstimate(GUIManager manager, boolean isPractice, DifficultyLevel difficulty) throws IOException{
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
	public PreKTestEstimate(GUIManager manager, boolean isPractice, DifficultyLevel difficulty,
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
	    	String filePath = "audio\\Test Tutorials\\Estimate.wav";
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
		int size = Value.values().length;
		int highLow; //Used to randomize correct/wrongAnswer to be above(1) or below(0) comparisonPoint.
		do{
			//Upper & Lower bound are excluded.
			comparisonPoint = getRandomInt(1, size - 2);
			highLow         = getRandomInt(0, 1);
			if(highLow == 0){
				correctAnswer = getRandomInt(0, comparisonPoint - 1);
				highLow       = getRandomInt(0, 1);
				wrongAnswer   = (highLow == 0) ? getRandomInt(0, correctAnswer - 1)
									: getRandomInt(comparisonPoint + 1, size - 1);
			}else{
				correctAnswer = getRandomInt(comparisonPoint + 1, size - 1);
				highLow       = getRandomInt(0, 1);
				wrongAnswer   = (highLow == 0) ? getRandomInt(0, comparisonPoint - 1)
									: getRandomInt(correctAnswer + 1, size - 1);
			}
		}while((correctAnswer >= (size - 1)) || correctAnswer <= 0
				|| (wrongAnswer >= (size - 1)) || wrongAnswer <= 0
				|| isNumberSetUsed[comparisonPoint][correctAnswer][wrongAnswer]);
		isNumberSetUsed[comparisonPoint][correctAnswer][wrongAnswer] = true;
	}
	
	/**
	 * Used to generate question text, and randomize the panel order (left or right).
	 * Displays the question and two panels.
	 */
	private void makeQuestion(){
		String question = "<CENTER>Which picture has the number of apples closest to that shown below?</CENTER></br><"
					+ "<img src = \"file:" + System.getProperty("user.dir")
					+ Value.values()[comparisonPoint].getFilePath() + "\" align=\"bottom\">";

		String leftImagePath;
		String rightImagePath;
		int answerLoc = getRandomInt(0, 1);
		if(answerLoc == 0){
			answerString   = QuestionPanelSelect.Answer.LEFT.getStringValue();
			leftImagePath  = Value.values()[correctAnswer].getFilePath();
			rightImagePath = Value.values()[wrongAnswer].getFilePath();
		}
		else{
			answerString   = QuestionPanelSelect.Answer.RIGHT.getStringValue();
			leftImagePath  = Value.values()[wrongAnswer].getFilePath();
			rightImagePath = Value.values()[correctAnswer].getFilePath();
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
	 * Used to initialize the isNumberSetUsed array. Prevents equidistant values from being asked.
	 *  IE. "Estimate 2". Prevents (4,0), (3,1), (2,2), (1,3), (0,4) as potential correct/wrong
	 *   answer combinations.
	 *  Also prevents correctAnswer/wrongAnswer combinations where wrongAnswer is closer value
	 *   comparisonPoint than correctAnswer.
	 *   IE. "Estimate 10". correctAnswer = 3, wrongAnswer = 7 Should not be allowed.
	 */
	private void initArrays(){
		int size = Value.values().length;
		for(int i = 0; i < size; i++){
			for(int rightAnswer = 0; rightAnswer < size; rightAnswer++){
				for(int incorrectAnswer = 0; incorrectAnswer < size; incorrectAnswer++){
					int rightABS = ((i - rightAnswer) > 0) ? (i - rightAnswer) : ((i - rightAnswer) * -1);
					int wrongABS = ((i - incorrectAnswer) > 0) ? (i - incorrectAnswer) : ((i - incorrectAnswer) * -1);
					if(rightABS >= wrongABS)
						isNumberSetUsed[i][rightAnswer][incorrectAnswer] = true;
					if(rightAnswer == i || incorrectAnswer == i)
						isNumberSetUsed[i][rightAnswer][incorrectAnswer] = true;
				}
			}
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
		question = "Question " + (currentQuestionNum - 1) + ": Estimate(Closest to " + comparisonPoint + ")";
		entry    = question + " Student Answer: (" + wrongAnswer + ")"
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
			String filePath = manager.getTestFolderPath() + "\\Estimate\\";
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
	 * This enum is used to define different values that may be used for comparison. Each value is 
	 *  associated with a name, int value, and image file name.
	 * Note: Number of valid questions = 3890.
	 * @author Kenneth Chin
	 */
	private enum Value{
		ZERO     ("Zero"     , 0 , "zero.png"),
		ONE      ("One"      , 1 , "one.png"),
		TWO      ("Two"      , 2 , "two.png"),
		THREE    ("Three"    , 3 , "three.png"),
		FOUR     ("Four"     , 4 , "four.png"),
		FIVE     ("Five"     , 5 , "five.png"),
		SIX      ("Six"      , 6 , "six.png"),
		SEVEN    ("Seven"    , 7 , "seven.png"),
		EIGHT    ("Eight"    , 8 , "eight.png"),
		NINE     ("Nine"     , 9 , "nine.png"),
		TEN      ("Ten"      , 10, "ten.png"),
		ELEVEN   ("Eleven"   , 11, "eleven.png"),
		TWELVE   ("Twelve"   , 12, "twelve.png"),
		THIRTEEN ("Thirteen" , 13, "thirteen.png"),
		FOURTEEN ("Fourteen" , 14, "fourteen.png"),
		FIFTEEN  ("Fifteen"  , 15, "fifteen.png"),
		SIXTEEN  ("Sixteen"  , 16, "sixteen.png"),
		SEVENTEEN("Seventeen", 17, "seventeen.png"),
		EIGHTEEN ("Eighteen" , 18, "eighteen.png"),
		NINETEEN ("Nineteen" , 19, "nineteen.png"),
		TWENTY   ("Twenty"   , 20, "twenty.png");
		
		private String filePath;
		private int value;
		private String name;
		
		private static final String imagePath = "\\images\\test\\estimate\\";
		
		/**
		 * The Value constructor.
		 * @param name A String describing this Value's name.
		 * @param value An int describing this value's int value.
		 * @param fileName A String describing this Value's image file name.
		 */
		private Value(String name, int value, String fileName){
			this.name      = name;
			this.value = value;
			this.filePath  = imagePath + fileName;
		}
		
		/**
		 * Used to obtain a String describing this Value's name.
		 * @return A String describing this Value's name.
		 */
		public String getName(){
			return name;
		}
		
		/**
		 * Used to obtain a String describing this Value's int value.
		 * @return A String describing this Value's int value.
		 */
		public int getValue(){
			return value;
		}
		
		/**
		 * Used to obtain a String describing this Value's image file path from the program's root directory.
		 * @return A String describing this Value's image file path from the program's root directory.
		 */
		public String getFilePath(){
			return filePath;
		}
	}
}