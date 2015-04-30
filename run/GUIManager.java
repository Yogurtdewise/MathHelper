/**
 * Name:         Math Helper
 * Version:      0.11.4
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
package project.run;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.HashMap;

import javafx.embed.swing.JFXPanel;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.sun.media.jfxmedia.MediaException;

import project.buttons.Grade1ModuleSelectPracticeButtons;
import project.buttons.Grade1ModuleSelectTestButtons;
import project.buttons.Grade1ModuleSelectTutorialButtons;
import project.buttons.Grade3ModuleSelectPracticeButtons;
import project.buttons.Grade3ModuleSelectTestButtons;
import project.buttons.Grade3ModuleSelectTutorialButtons;
import project.buttons.PreKModuleSelectPracticeButtons;
import project.buttons.PreKModuleSelectTestButtons;
import project.buttons.PreKModuleSelectTutorialButtons;
import project.constants.DifficultyLevel;
import project.constants.Operator;
import project.database.ModuleReportSummary;
import project.database.ReportCard;
import project.database.UserDatabase;
import project.interfaces.ModuleSelectButtonInterface;
import project.screens.MathHelperLogin;
import project.screens.PreKModuleSelect;
import project.screens.ReportCardScreen;
import project.screens.RewardScreen;
import project.screens.WelcomeScreen;
import project.tools.FontMaker;
import project.tools.FontMaker.FontTypes;
import project.tools.ImageLoader;
import project.tools.MainWindow;
import project.tools.QuestionPanelSelect;
import project.tools.QuestionPanelText;
import project.tools.TextFileMaker;
import project.tools.fxMediaPanel;

/**
 * Manages the implementation of the project by acting as a intermediary between
 * GUI elements and back-end logic. Used to manage which "screen" is to be
 * displayed, passing required parameters to the appropriate screen. This class
 * is the first class that should be called by the project's Event Dispatch
 * Thread. NOTE: PreKModuleSelect was never intended for the Grade 1-2 and Grade
 * 3-4 ModuleSelectScreens. However, no distinct design changes were ever made
 * for Grades 1-2 and 3-4. Therefore, for simplicity, PreKModuleSelect is
 * re-used for Grades 1-2 and 3-4. NOTE: This implementation of gradeLevel (vs
 * an enum) was requested by the Cool Math team. TODO Currently, this class
 * doubles as a test class. It must be cleaned up and re-factored before the
 * software's production.
 * 
 * @author Kenneth Chin w/ colaborations from Austin H Clark (
 *         {@link project.run.GUIManager#addUser(String, String, String, String, int)
 *         See addUser(String, String, String, String, int)} &
 *         {@link project.run.GUIManager#checkForNullValue(String, String) See
 *         checkForNullValue(String, String}).
 */
public final class GUIManager {

	/*************************************
	 * For easier location of methods, * a Table of Contents is included. * Use
	 * the "find" tool to locate the * "gm*" code that corresponds to * the
	 * desired section. * * Table of Contents(TOC) * * gm1 - Class Variables *
	 * gm2 - Constructor * gm3 - Initialize & Run * gm4 - Directory Structure *
	 * gm5 - Database Management * gm6 - Window Management * gm7 - Exception
	 * Handling * gm8 - Shutdown * gm9 - TODO TEST METHODS * *
	 *************************************/

	/*************************
	 * Class Variables (gm1) *
	 *************************/

	// The file path of the serialized database object.
	private static final String DB_FILEPATH = System.getProperty("user.dir")
			+ "\\db.dat";

	private static GUIManager singleInstance = new GUIManager(); // The
																	// singleton
																	// instance
																	// of
																	// GUIManager.

	// Exception handler variables.
	private String runTimeErrorSeparator = "**** NEW LOG ****"; // Used to
																// indicate a
																// new error log
																// in a single
																// error log
																// file.
	private StringBuffer errorLog = new StringBuffer(runTimeErrorSeparator
			+ "\r\n");
	private int errorNumber = 1; // A counter for each caught exception during
									// this run.

	private int width = 1024; // The pixel width of MainWindow's drawing area.
	private int height = 768; // The pixel height of MainWindow's drawing area.

	private int jfxPanelHeight = 394; // The pixel height of the JFX panel's
										// drawing area.
	private int jfxPanelWidth = 700; // The pixel width of the JFX panel's
										// drawing area.

	private String defaultBackgroundPath = "\\images\\welcome\\Background.png"; // The
																				// default
																				// background
																				// image
																				// of
																				// the
																				// Main
																				// Window.

	// TODO Test data that should be removed or accessed differently once a
	// database is implemented.
	// The current user's data.
	private static String userName = "Johnny"; // The current user's username.
	// private String userName = "Janie"; //Grade 1-2 username.
	// private String userName = "TheApple"; //Grade 3-4 username.
	private String studentFolderName = null; // The current user's directory
												// name. (lastname, firstname)
	private int gradeLevel = 0; // An int describing the the current user's
								// grade level.
	private UserDatabase database;

	private MainWindow mainWindow; // The root container of the Main Window.
	private fxMediaPanel mediaPanel; // The container of the jfx media panel

	private static final int MEDIA_LAYER = 1;

	/**
	 * Used to catch any Throwable exceptions not caught by GUIManager's
	 * constructor's try/catch. NOTE: This is called before the thread
	 * terminates. TODO Should implement a shutdown hook.
	 */
	private Thread.UncaughtExceptionHandler globalExceptionHandler = new Thread.UncaughtExceptionHandler() {
		@Override
		public void uncaughtException(Thread t, Throwable e) {
			handleException(e);
			errorLog.append("UNCAUGHT EXCEPTION!\r\n");
			errorLog.append("Thread: " + t.getName() + "\r\n");
			exit();
		}
	};

	/**********************
	 * Constructor (gm2) *
	 **********************/

	/**
	 * The private constructor of GUIManager. Sets the exception handler for
	 * this thread, and adds FontMaker fonts to this graphics environment.
	 * Additionally, initializes the database if none exists, or reads a
	 * pre-existing database.
	 */
	private GUIManager() {
		setUncaughtExecptionHandler();
		initFonts();
		if (dbExists())
			readDatabase();
		else
			database = new UserDatabase();
	}

	/**
	 * Used to obtain the single instance of GUIManager.
	 * 
	 * @return The single instance of GUIManager
	 */
	public static GUIManager getInstance() {
		if (singleInstance == null)
			singleInstance = new GUIManager();
		return singleInstance;
	}

	/***************************
	 * Initialize & Run (gm3) *
	 ***************************/

	/**
	 * Used to open and run the MathHelperLogin's login screen.
	 */
	public static void runLaunch() {
		MathHelperLogin.runLaunch(getInstance());
	}

	/**
	 * Starts the Swing Event Dispatch Thread & executes the main program
	 * (Starting at the Welcome Screen). GUIManager will open a single JFrame
	 * and manage all GUI components on the JFrame. If the JFrame is closed, the
	 * thread will receive a System.exit(0) call.
	 */
	public static void runGUI() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				getInstance().start();
			}
		});
	}

	/**
	 * Opens the Welcome Screen using the specified user's data. The user's data
	 * is obtained from the database's key value that matches the specified
	 * username. If the username does not exist in the database, the default
	 * user is loaded & used to display a Welcome Screen.
	 * 
	 * @param userName
	 *            The String that is used as the database's key for the current
	 *            user.
	 */
	public static void runGUI(String userName) {
		if (userName.equals(null))
			runGUI();
		else {
			setUserName(userName);
			runGUI();
		}
	}

	/**
	 * Used to initialize the current user's data & open a WelcomeScreen that is
	 * appropriate for the user.
	 */
	private void start() {
		initStudent();
		try {
			buildMainWindow();
			buildMediaPanel();
			// buildPreKTestCounting();
			// buildTextTestScreen();
			// buildChooseOneTest();
			// buildPreKModuleSelect();
			// buildReportCardScreen();
			// buidRewardScreen();
			buildWelcomeScreen();
			// runModule();
		} catch (Exception e) {
			handleException(e);
		} finally {
			// TODO This will always happen
		}
	}

	/**
	 * Used to load all fonts into the platform's graphics environment.
	 */
	private void initFonts() {
		for (FontTypes font : FontMaker.FontTypes.values()) {
			GraphicsEnvironment ge = GraphicsEnvironment
					.getLocalGraphicsEnvironment();
			try {
				ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(
						font.getPath())));
			} catch (FontFormatException | IOException e) {
				handleException(e);
			}
		}
	}

	/**
	 * A helper method used to set the userName field. Note: This method should
	 * be called before runGUI() is called.
	 * 
	 * @param userName
	 *            The String that is used as the database's key for the current
	 *            user.
	 */
	private static void setUserName(String userName) {
		GUIManager.userName = userName;
	}

	/**
	 * Used to initialize the program for use with the specified student.
	 */
	private void initStudent() {
		studentFolderName = database.getLastName(userName) + ", "
				+ database.getFirstName(userName);
		gradeLevel = database.getGradeLevel(userName);
		checkFileStructure();
	}

	/******************************
	 * Directory Structure (gm4) *
	 ******************************/

	/**
	 * Used to ensure the student's directory structure is ready to be written
	 * to. TODO The "Tests" folder might iterate through all of a student's
	 * possible test folders.
	 */
	private void checkFileStructure() {
		makeFolder(getTestFolderPath());
		makeFolder(getRewardsFolderPath());
		makeFolder("ErrorLogs\\");
	}

	/**
	 * A convenience method used to obtain the path, from the program's
	 * directory, to the student's "Tests" folder.
	 * 
	 * @return A String describing the path, from the program's directory, to
	 *         the student's "Tests" folder.
	 */
	public String getTestFolderPath() {
		return "Students\\" + studentFolderName + "\\Tests\\";
	}

	/**
	 * A convenience method used to obtain the path, from the program's
	 * directory, to the student's "Rewards" folder.
	 * 
	 * @return A String describing the path, from the program's directory, to
	 *         the student's "Rewards" folder.
	 */
	public String getRewardsFolderPath() {
		return "Students\\" + studentFolderName + "\\Rewards\\";
	}

	/**
	 * Used to make the specified folder(s), if the path does not already exist.
	 * 
	 * @param pathFromRoot
	 *            A String indicating the directory path, from the program's
	 *            directory, which is to be created.
	 * @return A boolean indicating true if the specified path structure was
	 *         created or already exists; false, otherwise.
	 */
	private boolean makeFolder(String pathFromRoot) {
		File directory = new File(pathFromRoot);
		if (directory.exists())
			return true;
		return directory.mkdirs();
	}

	/******************************
	 * Database Management (gm5) *
	 ******************************/

	/**
	 * Used to obtain the String password of a specified username. Returns null
	 * if the specified username does not exist.
	 * 
	 * @param username
	 *            A String describing the username who's password is to be
	 *            obtained.
	 * @return A String describing the password for the specified username.
	 */
	public String getPassword(String username) {
		return database.getPassword(username);
	}

	/**
	 * A helper method, used to build the primary media panel that is used as
	 * the program's container for videos.
	 * 
	 * @throws IOException
	 *             {@link project.tools.ImageLoader See the ImageLoader class
	 *             for details.}
	 */
	private void buildMediaPanel() throws IOException {
		this.mediaPanel = new fxMediaPanel(this, this.jfxPanelWidth,
				this.jfxPanelHeight);

		// Add JFX Panel component to the Main Window
		int padding = ((this.mainWindow.getPreferredSize().width - this.mediaPanel
				.getPanelWidth()) / 2);
		this.mainWindow.addLayer(this.mediaPanel.getMediaPanel(), MEDIA_LAYER,
				padding, 125);
		this.mediaPanel.getMediaPanel().setVisible(false);
	}

	/**
	 * Used to add a user to the database. Returns true if the specified user
	 * was successfully added to the database; false otherwise. Reasons adding a
	 * user may fail: The specified userName is already used, OR a null value
	 * was passed to this method, OR gradeLevel is not 0,1, or 2.
	 * 
	 * @param userName
	 *            The String that is used as the database's key for the current
	 *            user.
	 * @param password
	 *            A String that describes the password that will be associated
	 *            with the specified userName.
	 * @param firstName
	 *            A String that describes the "First Name" that will be
	 *            associated with the specified userName.
	 * @param lastName
	 *            A String that describes the "Last Name" that will be
	 *            associated with the specified userName.
	 * @param gradeLevel
	 *            An int that describes the "Grade Level" that will be
	 *            associated with the specified userName. The value of
	 *            gradeLevel may be 0 (for PreK-K users), 1 (for Grade 1-2
	 *            users), or 2 (for Grade 3-4 users).
	 * @return A boolean indicating true if the specified user was successfully
	 *         added to the database; false otherwise.
	 *
	 *         Author Austin H Clark Changes Made by Austin H Clark. No check on
	 *         null values, that is executed prior to addUser Method call in
	 *         CreateUser.java
	 */
	public boolean addUser(String userName, String password, String firstName,
			String lastName, int gradeLevel) {
		if (gradeLevel < 0 || gradeLevel > 2)
			return false;

		String checkPwd = getPassword(userName);

		boolean checkNull = checkForNullValue(password, checkPwd);

		if (!checkNull) {
			database.addUser(userName, password, firstName, lastName,
					gradeLevel);
			return true;
		}
		return false;
	}

	/**
	 * Used to obtain the JFX Media Panel that is used as a container for all
	 * displayed videos/media.
	 * 
	 * @return The Media Panel that is used as a primary container for displayed
	 *         videos.
	 */
	public fxMediaPanel getMediaPanel() {
		return this.mediaPanel;
	}

	/**
	 * Author Austin H Clark checkForNullValue checks that neither str1 or str2
	 * are null. If one is null return false otherwise return true
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	boolean checkForNullValue(String str1, String str2) {
		if (str1 == null || str2 == null) {
			// return false; if you assume null not equal to null
			return str1 == str2;
		}
		return true;
	}

	/**
	 * Used to obtain the current user's username.
	 * 
	 * @return A String describing the current user's username.
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Used to obtain the current user's grade for the specified test and
	 * difficulty level.
	 * 
	 * @param button
	 *            A ModuleSelectButtonInterface that describes the test who's
	 *            grade is to be retrieved.
	 * @param difficulty
	 *            A DifficultyLevel that describes the difficulty level of the
	 *            test who's grade is to be retrieved.
	 * @return An int describing the current user's number of correct answers
	 *         for the specified test and difficulty level.
	 */
	public int getPreviousGrade(ModuleSelectButtonInterface button,
			DifficultyLevel difficulty) {
		return ReportCard.getGrade(database.getReportCard(userName), button,
				difficulty);
	}

	/**
	 * Used to set the current users grade and maximum attainable grade for the
	 * specified test and difficulty level.
	 * 
	 * @param button
	 *            A ModuleSelectButtonInterface that describes the test who's
	 *            grade is to be changed.
	 * @param difficulty
	 *            A DifficultyLevel that describes the difficulty level of the
	 *            test who's grade is to be changed.
	 * @param numCorrect
	 *            An int describing the number of correct answers that the
	 *            current user achieved for the specified test and difficulty
	 *            level. The number will be recorded to the user's database
	 *            record for the specified test and difficulty level.
	 * @param maxGrade
	 *            An int describing the maximum attainable grade for the
	 *            specified test and difficulty level, which will also be set
	 *            for the specified test and difficulty level.
	 */
	public void setGrade(ModuleSelectButtonInterface button,
			DifficultyLevel difficulty, int numCorrect, int maxGrade) {
		HashMap<ModuleSelectButtonInterface, ModuleReportSummary> reportCard = database
				.getReportCard(userName);
		database.setReportCard(userName, ReportCard.setGrade(reportCard,
				button, numCorrect, maxGrade, difficulty));
	}

	/**
	 * Used to increment the current user's lastActiveTest by one(enable use of
	 * the next test). If the user already has access to all tests, the
	 * lastActiveTest will not be incremented. If the passed testNumber is less
	 * than the student's current lastActiveTest, then lastActiveTest will not
	 * be incremented.
	 * 
	 * @param testNumber
	 *            An int describing the test's position in the button order.
	 *            This value can be determined by using
	 *            ModuleSelectButtonInterface.ordinal() + 1, where the
	 *            ModuleSelectButtonInterface is of the test's enum type.
	 */
	public void incrementHighestTest(int testNumber) {
		int maxValue;
		int lastActive = database.getLastActiveTest(userName);
		if (lastActive > testNumber)
			return;
		switch (gradeLevel) {
		case 0:
			maxValue = PreKModuleSelectTestButtons.Button.values().length;
			break;
		case 1:
			maxValue = Grade1ModuleSelectTestButtons.Button.values().length;
			break;
		case 2:
			maxValue = Grade3ModuleSelectTestButtons.Button.values().length;
			break;
		default:
			maxValue = PreKModuleSelectTestButtons.Button.values().length;
			break;
		}
		if (lastActive < maxValue) {
			lastActive++;
			database.setLastActiveTest(userName, lastActive);
		}
	}

	/**
	 * Used to write the database object to disk.
	 */
	public void writeDatabase() {
		try {
			FileOutputStream fout = new FileOutputStream(DB_FILEPATH);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(database);
			oos.close();
			fout.close();
		} catch (IOException e) {
			handleException(e);
		}
	}

	/**
	 * Used to retrieve the database object from a file.
	 */
	private void readDatabase() {
		try {
			FileInputStream fis = new FileInputStream(DB_FILEPATH);
			ObjectInputStream ois = new ObjectInputStream(fis);
			database = (UserDatabase) ois.readObject();
			ois.close();
			fis.close();
		} catch (IOException | ClassNotFoundException e) {
			handleException(e);
		}
	}

	/**
	 * Used to check if the database file exists.
	 * 
	 * @return A boolean indicating true if the database file exists, false
	 *         otherwise.
	 */
	private boolean dbExists() {
		return new File(DB_FILEPATH).exists();
	}

	/****************************
	 * Window Management (gm6) *
	 ****************************/

	/**
	 * A helper method, used to build the primary window that is used as the
	 * program's root container. Also adds a window listener to mainWindow that
	 * listens for a window close event. If the window is closed, the database
	 * is written to a file.
	 * 
	 * @throws IOException
	 *             {@link project.tools.ImageLoader See the ImageLoader class
	 *             for details.}
	 */
	private void buildMainWindow() throws IOException {
		BufferedImage background = ImageLoader
				.getBufferedImage(defaultBackgroundPath);
		mainWindow = MainWindow.getInstance(this, width, height, background);
		mainWindow.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				MainWindow frame = (MainWindow) e.getSource();
				writeDatabase();
				frame.setDefaultCloseOperation(MainWindow.EXIT_ON_CLOSE);
			}
		});
	}

	/**
	 * Used to obtain the MainWindow that is used as a primary container for all
	 * displayed screens.
	 * 
	 * @return The MainWindow that is used as a primary container for displayed
	 *         objects.
	 */
	public MainWindow getMainWindow() {
		return mainWindow;
	}

	/**
	 * Creates a new WelcomeScreen that will change mainWindow's background and
	 * populate mainWindow with appropriate JComponents. All Exceptions are
	 * thrown to the caller method.
	 * 
	 * @throws IOException
	 *             {@link project.screens.WelcomeScreen See the WelcomeScreen's
	 *             constructor details}.
	 */
	public void buildWelcomeScreen() throws IOException {
		new WelcomeScreen(this, database.getFirstName(userName));
	}

	/**
	 * Creates a new ReportCardScreen of the specified type, using the specified
	 * reportMap. TODO This method must be changed to get the databases'
	 * HashMap!
	 * 
	 * @throws IOException
	 *             {@link project.screens.ReportCardScreen See the
	 *             ReportCardScreen's constructor details.}
	 */
	public void buildReportCardScreen() throws IOException {
		int size;
		HashMap<ModuleSelectButtonInterface, ModuleReportSummary> reportMap = database
				.getReportCard(userName);
		switch (gradeLevel) {
		case 0:
			size = PreKModuleSelectTestButtons.Button.values().length;
			new ReportCardScreen(this, new PreKModuleSelectTestButtons(size),
					reportMap);
			break;
		case 1:
			size = Grade1ModuleSelectTestButtons.Button.values().length;
			new ReportCardScreen(this, new Grade1ModuleSelectTestButtons(size),
					reportMap);
			break;
		case 2:
			size = Grade3ModuleSelectTestButtons.Button.values().length;
			new ReportCardScreen(this, new Grade3ModuleSelectTestButtons(size),
					reportMap);
			break;
		default:
			size = PreKModuleSelectTestButtons.Button.values().length;
			new ReportCardScreen(this, new PreKModuleSelectTestButtons(size),
					reportMap);
			break;
		}
	}

	/**
	 * Creates a new ModuleSelect screen for the current gradeLevel when a user
	 * selects the "Test" option. Invoking this method will change mainWindow's
	 * background and populate mainWindow with appropriate JComponents. All
	 * Exceptions are thrown to the caller method.
	 * 
	 * @throws IOException
	 *             {@link project.screens.PreKModuleSelect See the
	 *             PreKModuleSelect constructor details.}
	 */
	public void buildTestModuleSelect() throws IOException {
		int lastActiveButton = database.getLastActiveTest(userName);
		switch (gradeLevel) {
		case 0:
			new PreKModuleSelect(this, new PreKModuleSelectTestButtons(
					lastActiveButton));
			break;
		case 1:
			new PreKModuleSelect(this, new Grade1ModuleSelectTestButtons(
					lastActiveButton));
			break;
		case 2:
			new PreKModuleSelect(this, new Grade3ModuleSelectTestButtons(
					lastActiveButton));
			break;
		default:
			new PreKModuleSelect(this, new PreKModuleSelectTestButtons(
					lastActiveButton));
			break;
		}
	}

	/**
	 * Creates a new ModuleSelect screen for the current gradeLevel when a user
	 * selects the "Practice" option. Invoking this method will change
	 * mainWindow's background and populate mainWindow with appropriate
	 * JComponents. All Exceptions are thrown to the caller method.
	 * 
	 * @throws IOException
	 *             {@link project.screens.PreKModuleSelect See the
	 *             PreKModuleSelect constructor details.}
	 */
	public void buildPracticeModuleSelect() throws IOException {
		switch (gradeLevel) {
		case 0:
			new PreKModuleSelect(this, new PreKModuleSelectPracticeButtons());
			break;
		case 1:
			new PreKModuleSelect(this, new Grade1ModuleSelectPracticeButtons());
			break;
		case 2:
			new PreKModuleSelect(this, new Grade3ModuleSelectPracticeButtons());
			break;
		default:
			new PreKModuleSelect(this, new PreKModuleSelectPracticeButtons());
			break;
		}
	}

	/**
	 * Creates a new ModuleSelect screen for the current gradeLevel when a user
	 * selects the "Tutorial" option. Invoking this method will change
	 * mainWindow's background and populate mainWindow with appropriate
	 * JComponents. All Exceptions are thrown to the caller method.
	 * 
	 * @throws IOException
	 *             {@link project.screens.PreKModuleSelect See the
	 *             PreKModuleSelect constructor details.}
	 */
	public void buildTutorialModuleSelect() throws IOException {
		switch (gradeLevel) {
		case 0:
			new PreKModuleSelect(this, new PreKModuleSelectTutorialButtons());
			break;
		case 1:
			new PreKModuleSelect(this, new Grade1ModuleSelectTutorialButtons());
			break;
		case 2:
			new PreKModuleSelect(this, new Grade3ModuleSelectTutorialButtons());
			break;
		default:
			new PreKModuleSelect(this, new PreKModuleSelectTutorialButtons());
			break;
		}
	}

	/*****************************
	 * Exception Handling (gm7) *
	 *****************************/

	// TODO Exception handling!
	/**
	 * Checks if the current environment supports transparent pixels.
	 * 
	 * @return True if the system supports transparent pixels, false otherwise.
	 */
	private boolean isTransparentcyCompatible() {
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		return gd
				.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT);
	}

	/**
	 * Used to set the UncaughtExecptionHandler, globalExceptionHandler.
	 */
	private void setUncaughtExecptionHandler() {
		Thread.setDefaultUncaughtExceptionHandler(globalExceptionHandler);
		Thread.currentThread().setUncaughtExceptionHandler(
				globalExceptionHandler);
	}

	/**
	 * This method is used as a catch-all for all Exceptions thrown by this
	 * thread. It aids in error reporting by creating an error log, and provides
	 * user feedback by using an pop-up error dialogue before the thread dies.
	 * 
	 * @param e
	 *            The Throwable clause that was thrown.
	 */
	public void handleException(Throwable e) {
		// TODO e.printStackTrace() should be removed after proper execution is
		// developed.
		e.printStackTrace();
		String errorMessage;
		String localMessage;
		if (e.getMessage() == null)
			errorMessage = "No message provided.";
		else
			errorMessage = e.getMessage();
		if (e.getLocalizedMessage() == null)
			localMessage = "No local message provided.";
		else
			localMessage = e.getLocalizedMessage();

		StackTraceElement[] stackTrace = e.getStackTrace();
		errorLog.append("ERROR " + errorNumber + ": " + errorMessage + "\r\n");
		errorLog.append("Local Message: " + localMessage + "\r\n");
		for (StackTraceElement element : stackTrace) {
			errorLog.append("    " + element.toString() + "\r\n");
		}
		errorNumber++;
		// TODO Should implement a shutdown hook.

		String message = "";
		if (e.getClass().getName() == "javafx.scene.media.MediaException") {
			message = "<p><CENTER>Sorry, there was a problem loading the tutorial video.</CENTER></p>"
					+ "<p><CENTER>You may try re-installing the \"media\" folder.</CENTER></p>"
					+ "<p><CENTER>OR</CENTER></p>"
					+ "<p><CENTER>Contact Cool Math at:<br>"
					+ "      kchin@student.framingham.edu<br>"
					+ "      Please send the \"ErrorLogs\" folder,<br>"
					+ "      if you choose to contact us.</CENTER></p>";
		}
		else {
			message = "<p><CENTER>Sorry, there was a problem loading this screen.</CENTER></p>"
					+ "<p><CENTER>You may try re-installing the \"image\" folder.</CENTER></p>"
					+ "<p><CENTER>OR</CENTER></p>"
					+ "<p><CENTER>Contact Cool Math at:<br>"
					+ "      kchin@student.framingham.edu<br>"
					+ "      Please send the \"ErrorLogs\" folder,<br>"
					+ "      if you choose to contact us.</CENTER></p>";
		}
		JLabel label = new JLabel("<HTML><div>" + message + "</div></HTML>",
				JLabel.CENTER);
		JOptionPane.showMessageDialog(null, label, "Oops!",
				JOptionPane.INFORMATION_MESSAGE);
		exit();
	}

	/*******************
	 * Shutdown (gm8) *
	 *******************/

	/**
	 * Used to gracefully exit the program. Creates an error log text file, if
	 * there are errors to report.
	 */
	public void exit() {
		if (errorNumber > 1) {
			Calendar date = Calendar.getInstance();
			date.setTimeInMillis(System.currentTimeMillis());
			String fileName = date.get(Calendar.YEAR) + "_"
					+ date.get(Calendar.DAY_OF_MONTH) + "_"
					+ date.get(Calendar.HOUR_OF_DAY) + "_"
					+ date.get(Calendar.MINUTE);
			try {
				TextFileMaker.writeString("ErrorLogs\\", fileName,
						errorLog.toString());
			} catch (IOException e) {
				// If the error log can not be written, exit.
				System.exit(-1);
			}
		}
		mainWindow.dispatchEvent(new WindowEvent(mainWindow,
				WindowEvent.WINDOW_CLOSING));
		// Ensure the JVM is closed.
		System.exit(0);
	}

	/****************************
	 * TODO TEST METHODS (gm9) *
	 ****************************/

	// TODO TEST METHODS. All methods to be removed or re-written!
	private void buildTextTestScreen() throws IOException {
		QuestionPanelText panel = new QuestionPanelText(mainWindow, 10);
		// panel.showQuestion("How big is this? <br>1<br>2<br>3<br>4<br>5<br>6<br>7<br>8<br>9<br>10",
		// 1);
		// panel.showAnswer("Correct!", false);
		panel.showEquation("Answer this: ", 1, 5, 2, Operator.ADD);
		panel.showEquationAnswer("7", "Incorrect!", false);
	}

	private void buidRewardScreen() throws IOException {
		int grade = 80;
		new RewardScreen(this, PreKModuleSelectTestButtons.Button.ARITHMETIC,
				DifficultyLevel.EASY, grade, true, "Rewards\\");
	}

	private void buildChooseOneTest() throws IOException {
		String image1Path = "\\images\\test\\apples\\01AppleBoard.png";
		String image2Path = "\\images\\test\\apples\\08AppleBoard.png";
		QuestionPanelSelect panel = new QuestionPanelSelect(mainWindow, 10);
		panel.showQuestion("This is a Test", 1, image1Path, image2Path);
		panel.showAnswer("Some text goes here!",
				QuestionPanelSelect.Answer.LEFT, true);
	}

	private void runModule() throws IOException {
		QuestionPanelSelect module = new QuestionPanelSelect(mainWindow, 10);
	}
}