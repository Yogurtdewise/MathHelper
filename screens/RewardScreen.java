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
package project.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JComponent;
import javax.swing.JLabel;

import project.buttons.HomeButtonMaker;
import project.constants.DifficultyLevel;
import project.interfaces.ClickableObserver;
import project.interfaces.ModuleSelectButtonInterface;
import project.run.GUIManager;
import project.tools.ContentPane;
import project.tools.FontMaker;
import project.tools.ImageLoader;
import project.tools.MainWindow;

/**
 * This class is used to display a "Reward" image along with the percentage of correct answers
 *  a student achieved after taking a Test module. Before a "Home" button is added to the screen,
 *  a screen-capture is performed. The image is then saved as the format specified by IMAGE_FILE_TYPE.
 *  The image is written the name and directory as specified by the constructor at instantiation.
 *  Once the image has been written to the user's storage drive, the "Home" button is placed onto
 *  the screen. If clicked, the "Home" button will tear down the RewardScreen & create a WelcomeScreen.
 *  Also enables the next available test, if the student's grade is passing. Lastly, forces the database
 *  to write to file. 
 * @author Kenneth Chin
 */
public class RewardScreen implements ClickableObserver{
	
	//TODO More rewards should be implemented.
	private static final String REWARD_BACKGROUND = "\\images\\rewards\\Reward.png";
	private static final String IMAGE_FILE_TYPE   = "png";            //The image file format.
	private static final int    PREFERRED_FONT    = FontMaker.ARIAL;  //Used to write the % correct answered.
	private static final int    TEXT_LAYER        = 2;
	private static final int    BUTTON_LAYER      = 3;
	//The grade percent that is allows a student to take the next test.
	private static final int    PASSING_GRADE     = 60;
	
	private ContentPane homeBtn;    //Used to display the "Home" button.
	private ContentPane gradePanel; //Used to display the % correct answered text.
	private String pathFromRoot;    //The path from the program's directory, in which the screen-shot image is stored.
	private int grade;              //The % correct answers that a student received on a Test module.
	private boolean isFileMade = false; //Used to determine if the screen shot should be written to file.
	//The ModuleSelectButtonInterface that describes the test that the student took.
	private ModuleSelectButtonInterface button;
	private DifficultyLevel difficulty;
	
	private GUIManager manager;    //The GUIManager that manages mainWindow & all GUI screens.
	private MainWindow mainWindow;  //The MainWindow that is to have components added to.
	
	/**
	 * Creates a RewardScreen that displays a reward image, student grade (as a % correct), and a
	 *  "Home" button. Additionally, a screen-capture is performed on mainWindw, prior to adding the
	 *  "Home" button. The image is stored into the specified directory, with a name specified by
	 *  testName. Also enables the next available test, if the student's grade is passing. Lastly,
	 *  forces the database to write to file. 
	 * @param manager The GUIManager that manages the primary MainWindow & all GUI screens.
	 * @param test The ModuleSelectButtonInterface that represents the test that was taken.
	 *  Used for image file name & to determine if the next test should be available.
	 * @param difficulty The DifficultyLevel that describes the test's difficulty.
	 * @param grade An int indicating the percentage of correct answers that a student received on
	 *  the specified Test module.
	 * @param isFileMade A boolean indicating true if the reward image should be written to file,
	 *  false otherwise.
	 * @param pathFromRoot A String indicating the path of the directory in which the screen-capture image
	 *  is to be stored. The path should begin from the program's directory. 
	 * @throws IOException Thrown if any image file can not be read.
	 */
	public RewardScreen(GUIManager manager, ModuleSelectButtonInterface test, DifficultyLevel difficulty, int grade, boolean isFileMade, String pathFromRoot) throws IOException{
		this.manager      = manager;
		this.mainWindow   = manager.getMainWindow();
		this.button       = test;
		this.difficulty   = difficulty;
		this.grade        = grade;
		this.isFileMade   = isFileMade;
		this.pathFromRoot = pathFromRoot;
		init();
	}
	
	/**
	 * Used to initialize the RewardScreen's display & obtain a screen capture before adding a "Home" button.
	 *  Also enables the next test if the student's grade is >= PASSING_GRADE. Lastly, forces the database
	 *  to be written to file.
	 */
	private void init() throws IOException{
		setBackground();
		playSound();
		addGrade();
		if(isFileMade)
			makeScreenShot();
		if(grade >= PASSING_GRADE)
			manager.incrementHighestTest(button.getOrdinal() + 1);
		manager.writeDatabase();
		showHomeBtn();
	}
	
	/**
	 * Used to change mainWindow's background image to the "REWARD_BACKGROUND".
	 * @throws IOException Thrown if the reward image file can not be read.
	 */
	private void setBackground() throws IOException{
		mainWindow.setBackgroundImage(ImageLoader.getBufferedImage(REWARD_BACKGROUND));
	}
	
	/**
	 * Used to draw the student's grade onto the center of the reward image.
	 */
	private void addGrade(){
		Font font;
		try {
			font = FontMaker.getFont(PREFERRED_FONT, 100);
		} catch (IndexOutOfBoundsException | IOException | FontFormatException e) {
			font = FontMaker.getDefaultFont(100);
		}
		
		font = FontMaker.getBoldFont(font);
		
		String gradeText = "<HTML>" + grade + "%</HTML>";
		
		//Create the text label & add it to a ContentPane.
		JLabel gradeLabel = new JLabel(gradeText);
		gradeLabel.setFont(font);
		gradeLabel.setForeground(Color.BLACK);
		
		int gradeWidth = gradeLabel.getPreferredSize().width;
		
		gradePanel = new ContentPane(gradeWidth, gradeLabel.getPreferredSize().height, false, false);
		gradePanel.addComponent(gradeLabel, 0, 0);
		
		//Determine where to place the text.
		int windowCenter = mainWindow.getWidth() / 2;
		int gradeCenter  = gradeWidth / 2;
		int gradeX       = windowCenter - gradeCenter;
		int xOffset      = 35;
		
		mainWindow.addLayer(gradePanel, TEXT_LAYER, (gradeX + xOffset), 355);
	}
	
	/**
	 * Used to create, register, and display a "Home" button.
	 * @throws IOException Thrown if the home button's image file can not be read.
	 */
	private void showHomeBtn() throws IOException{
		homeBtn = HomeButtonMaker.getContentPane();
		homeBtn.registerObserver(this);
		mainWindow.addLayer(homeBtn, BUTTON_LAYER, HomeButtonMaker.getXDefault(), HomeButtonMaker.getYDefault());
	}
	
	/**
	 * Used to obtain a screen-capture of mainWindow and create a file as specified by RewardScreen's
	 *  constructor parameters.
	 */
	private void makeScreenShot(){
		int insetWidth  = mainWindow.getInsets().left +  mainWindow.getInsets().right;
		int insetHeight = mainWindow.getInsets().top +  mainWindow.getInsets().bottom;
		String fileName = button.getName() + "(" + difficulty.getName() + ")." + IMAGE_FILE_TYPE;
		BufferedImage image = new BufferedImage(mainWindow.getWidth() - insetWidth,
				                        mainWindow.getHeight() - insetHeight, BufferedImage.TYPE_INT_RGB);
		mainWindow.getContentPane().paint( image.getGraphics() );
		try {
            ImageIO.write(image, IMAGE_FILE_TYPE, new File(pathFromRoot, fileName));
          } catch(Exception e){
            manager.handleException(e);
          }
	}
	
	/**
	 * Used to play an cheer sound while this RewardScreen loads.
	 */
	public void playSound(){
	    try{
	    	String filePath = "audio\\Rewards\\cheer.wav";
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    }catch(Exception e) {
	        manager.handleException(e);
	    }
	}
	
	/**
	 * Used to remove all components that were created by this RewardScreen from mainWindow.
	 * WARNING: This method renders this instance of RewardScreen inoperable. All references
	 *  to this instance should be removed for garbage collection. If another RewardScreen
	 *  screen is needed, a new instance should be created.
	 */
	public void tearDown(){
		mainWindow.getContainer().remove(gradePanel);
		mainWindow.getContainer().remove(homeBtn);
		homeBtn.removeObserver(this);
		gradePanel = null;
		homeBtn    = null;
	}

	@Override
	public void clicked(JComponent component) {
		if(component == homeBtn){
			tearDown();
			try {
				manager.buildWelcomeScreen();
			} catch (IOException e) {
				manager.handleException(e);
			}
		}else{
			throw new IllegalStateException("RewardScreen recieved a click event, but didn't do anything!");
		}
	}

}
