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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import project.buttons.PreKModuleSelectTestButtons;
import project.constants.DifficultyLevel;
import project.interfaces.ModuleSelectButtonInterface;
import project.interfaces.Questionable;
import project.interfaces.QuestionableObserver;
import project.run.GUIManager;
import project.screens.RewardScreen;
import project.tools.TextFileMaker;

public class PreKTestFinal implements QuestionableObserver{

	//The ModuleSelectButtonInterface that describes this test.
	private static final ModuleSelectButtonInterface TEST_BUTTON = PreKModuleSelectTestButtons.Button.FINAL;
	
	//Difficulty settings. Note: MUST be less than the maximum number of question permutations.
	private static final int EASY_MAX_QUESTIONS = 20;   //The maximum number of questions for the "Easy" difficulty.
	private static final int NORM_MAX_QUESTIONS = 30;   //The maximum number of questions for the "Normal" difficulty.
	private static final int HARD_MAX_QUESTIONS = 40;   //The maximum number of questions for the "Hard" difficulty.
	private int maxNumberOfQuestions = EASY_MAX_QUESTIONS; //The actual maximum number of questions for this test.
	
	private Random rng = new Random(System.currentTimeMillis()); //A random number generator.
	
	private boolean isPractice = false;   //Used to indicate that this test is a practice test.
	private DifficultyLevel difficulty;   //The current DifficultyLevel of this test.
	
	private int currentTestIndex   = 0;   //The index of the Question whose question is being asked.
	private int currentQuestionNum = 1;   //The current question number.
	
	private int numCorrect = 0;     //The number of correct answers obtained from the user.
	private ArrayList<String> wrongAnswers = new ArrayList<String>(); //Used to track incorrect answers.
	
	private GUIManager manager;    //The GUIManager that manages mainWindow & all GUI screens.
	
	/**
	 * This class is used to test PreK-K students on a random sampling of skills taught and tested
	 *  by the system. It is a "Cumulative" or "Final" exam, but no guarantees are made that an
	 *  even distribution of skills will be tested.
	 * @param manager The GUIManager that manages the primary MainWindow & all GUI screens.
	 * @param isPractice A boolean indicating true if this test is a practice test, false otherwise.
	 * @param difficulty The DifficultyLevel of this test.
	 */
	public PreKTestFinal(GUIManager manager, boolean isPractice, DifficultyLevel difficulty){
		this.manager    = manager;
		this.isPractice = isPractice;
		this.difficulty = difficulty;
		
		setDifficulty();
		
		initTests();
		askQuestion();
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
	 * Used to initialize all PreK-K tests so they may be accessed. Each test is instantiated
	 *  using a "do nothing" constructor. Tests must have their showQuestion(int) method called
	 *  before they display anything.
	 */
	private void initTests(){
		for(int i = 0; i < Question.values().length; i++){
			Question test = Question.values()[i];
			test.initTest(manager, isPractice, difficulty, maxNumberOfQuestions, this);
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
	 * Used to choose a psudo-random PreK-K test & have it display a question.
	 */
	private void askQuestion(){
		currentTestIndex  = getRandomInt(0, (Question.values().length - 1));
		Questionable test = Question.values()[currentTestIndex].getTest();
		try {
			test.showQuestion(currentQuestionNum);
		} catch (IOException e) {
			manager.handleException(e);
		}
		currentQuestionNum++;
	}
	
	/**
	 * A helper method, used to create a String that describes an incorrectly answered question.
	 *  Adds the created String to the wrongAnswers ArrayList.
	 */
	private void logWrongAnswer(String wrongAnswerLogEntry){
		wrongAnswers.add(wrongAnswerLogEntry);
	}
	
	/**
	 * Used to create a text file containing all questions, user answers, and correct answers
	 *  for all questions that the user answered incorrectly. If the user did not answer any
	 *  question incorrectly, no file is created.
	 * @throws IOException Thrown if there is a problem writing the file.
	 */
	private void makeTestDetailFile() throws IOException{
		if(wrongAnswers.size() > 0){
			String filePath = manager.getTestFolderPath() + "\\Final\\";
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
	
	/**
	 * Used to display a pop-up message after a user completes a practice version of this test.
	 * Returns the user to the Welcome Screen after they click "ok".
	 */
	private void practiceComplete(){
		String message = "All practice questions answered!\n\n"
				   + "Your grade is: " + getGrade() + "%!\n\n"
				   + "Click OK to return to the Welcome Screen!";
		JOptionPane.showMessageDialog(null, message, "Practice Complete!", JOptionPane.INFORMATION_MESSAGE);
		try{
			manager.buildWelcomeScreen();
		}catch (IOException e){
			manager.handleException(e);
		}
	}
	
	/**
	 * Used to set all tests to null so they may be added to the garbage collection queue.
	 * NOTE: Use of this method will render this instance of PreKTestFinal useless. A new
	 *  instance must be created if another PreKTestFinal is to be used.
	 */
	private void tearDown(){
		for(int i = 0; i < Question.values().length; i++)
			Question.values()[i].tearDown();
	}
	
	@Override
	public void answered(Questionable object, boolean correct, String wrongAnswerLogEntry) {
		if(object == Question.values()[currentTestIndex].getTest()){
			if(correct)
				numCorrect++;
			if(!isPractice && !correct)
				logWrongAnswer(wrongAnswerLogEntry);
			if(currentQuestionNum <= maxNumberOfQuestions){
				askQuestion();
			}else if(isPractice){
				practiceComplete();
			}else{
				tearDown();
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
	
	/**
	 * An enum of all PreK-K tests. Used to instantiate and access each test.
	 * Note: Each enum's initTest() method must be called before this enum can be useful.
	 * @author Kenneth Chin
	 */
	private enum Question{
		/**
		 * The PreKTestCounting test.
		 */
		COUNTING {
			@Override
			protected void initTest(GUIManager manager, boolean isPractice, DifficultyLevel difficulty,
					int maxQuestions, QuestionableObserver finalTest) {
				try {
					this.test = new PreKTestCounting(manager, isPractice, difficulty, maxQuestions, finalTest);
				} catch (IOException e) {
					manager.handleException(e);
				}
			}
		},
		/**
		 * The PreKTestMatching test.
		 */
		MATCHING {
			@Override
			protected void initTest(GUIManager manager, boolean isPractice,
					DifficultyLevel difficulty, int maxQuestions, QuestionableObserver finalTest) {
				try {
					this.test = new PreKTestMatching(manager, isPractice, difficulty, maxQuestions, finalTest);
				} catch (IOException e) {
					manager.handleException(e);
				}
			}
		},
		/**
		 * The PreKTestSequences test.
		 */
		SEQUENCES {
			@Override
			protected void initTest(GUIManager manager, boolean isPractice,
					DifficultyLevel difficulty, int maxQuestions, QuestionableObserver finalTest) {
				try {
					this.test = new PreKTestSequences(manager, isPractice, difficulty, maxQuestions, finalTest);
				} catch (IOException e) {
					manager.handleException(e);
				}	
			}
		},
		/**
		 * The PreKTestComparison test.
		 */
		COMPARISON {
			@Override
			protected void initTest(GUIManager manager, boolean isPractice,
					DifficultyLevel difficulty, int maxQuestions, QuestionableObserver finalTest) {
				try {
					this.test = new PreKTestComparison(manager, isPractice, difficulty, maxQuestions, finalTest);
				} catch (IOException e) {
					manager.handleException(e);
				}
			}
		},
		/**
		 * The PreKTestFractions test.
		 */
		FRACTIONS {
			@Override
			protected void initTest(GUIManager manager, boolean isPractice,
					DifficultyLevel difficulty, int maxQuestions, QuestionableObserver finalTest) {
				try {
					this.test = new PreKTestFractions(manager, isPractice, difficulty, maxQuestions, finalTest);
				} catch (IOException e) {
					manager.handleException(e);
				}
			}
		},
		/**
		 * The PreKTestCoins test.
		 */
		COINS {
			@Override
			protected void initTest(GUIManager manager, boolean isPractice,
					DifficultyLevel difficulty, int maxQuestions, QuestionableObserver finalTest) {
				try {
					this.test = new PreKTestCoins(manager, isPractice, difficulty, maxQuestions, finalTest);
				} catch (IOException e) {
					manager.handleException(e);
				}
			}
		},
		/**
		 * The PreKTestArithmetic test.
		 */
		ARITHMETIC {
			@Override
			protected void initTest(GUIManager manager, boolean isPractice,
					DifficultyLevel difficulty, int maxQuestions, QuestionableObserver finalTest) {
				try {
					this.test = new PreKTestArithmetic(manager, isPractice, difficulty, maxQuestions, finalTest);
				} catch (IOException e) {
					manager.handleException(e);
				}
			}
		},
		/**
		 * The PreKTestEstimate test.
		 */
		ESTIMATE {
			@Override
			protected void initTest(GUIManager manager, boolean isPractice,
					DifficultyLevel difficulty, int maxQuestions,
					QuestionableObserver finalTest) {
				try {
					this.test = new PreKTestEstimate(manager, isPractice, difficulty, maxQuestions, finalTest);
				} catch (IOException e) {
					manager.handleException(e);
				}
			}
		};
		
		protected Questionable test = null; //The instance of this this test.
		
		/**
		 * The empty Question constructor.
		 * Note: initTest() must be called for this enum to be useful.
		 */
		private Question(){}
		
		/**
		 * Used to create an instance of this test. This method must be called once before this test's
		 *  methods may be used.
		 * @param manager The GUIManager that manages the primary MainWindow & all GUI screens.
		 * @param isPractice A boolean indicating true if this test is a practice test, false otherwise.
		 * @param difficulty The DifficultyLevel of this test.
		 * @param maxQuestions An int indicating the maximum number of questions that PreKTestFinal will
		 *  ask the user.
		 * @param finalTest The QuestionableObserver object that wants to be notified when a user answers
		 *  a question.
		 */
		protected abstract void initTest(GUIManager manager, boolean isPractice, DifficultyLevel difficulty,
				int maxQuestions, QuestionableObserver finalTest);
		
		/**
		 * Used to obtain the instance of this test.
		 * Note: This method should not be called before initTest(), or after tearDown().
		 * @return The instance of this Questionable test.
		 */
		protected Questionable getTest(){
			return test;
		}
		
		/**
		 * Used to set this Questionable test to null. This helps ensure garbage collection.
		 * Note: Once this method is called, this test will no longer be accessible.
		 */
		protected void tearDown(){
			test = null;
		}
	}
}
