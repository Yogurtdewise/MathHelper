/**
 * Name:         Math Helper
 * Version:      0.9.2
 * Version Date: 04/11/2015
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
import java.io.IOException;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JLabel;

import project.buttons.HomeButtonMaker;
import project.interfaces.ClickableObserver;
import project.interfaces.ModuleSelectButtonInterface;
import project.tools.fxMediaPanel;
import project.run.GUIManager;
import project.tools.ContentPane;
import project.tools.FontMaker;
import project.tools.ImageLoader;
import project.tools.MainWindow;

import javafx.embed.swing.JFXPanel;

//import javafx.scene.text.Font;
import javafx.scene.text.Text;
//import javafx.scene.paint.Color;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.scene.media.MediaView;


//import javafx.util.Duration;

/**
 * This class is used by classes/enums that implement ModuleSelectButtonInterface to obtain
 *  a DifficultyLevel from the user. This class populates GUIManager's MainWindow with a "Easy", 
 *  "Normal", and "Hard" difficulty level buttons, as well as a "Home" button. Clicking on a
 *  difficulty level button calls the difficultySelected(DifficultyLevel) method of the passed
 *  ModuleSelectButtonInterface. Clicking the "Home" button calls tearDown(), then creates a
 *  new WelcomeScreen. (update this)
 * @author Kenneth Chin, Christopher Moraal
 */
public class TutorialPlayerScreen implements ClickableObserver{
	//Layer constants for TutorialPlayerScreen components.
		private static final int BUTTON_LAYER = 3;
		private static final int TEXT_LAYER   = 2;
		
		private static final int PREFERRED_FONT = FontMaker.ARIAL;
		
		//The path from the program's directory to the DifficultySelectScreen image folder.
		private static final String DIRECTORY_PATH = "\\images\\tutorial\\";
		private static final String MEDIA_PATH = "\\media\\tutorials\\";
		
		private ContentPane playButton;      //The ContentPane for the "Play" button.
		private ContentPane pauseButton;	//The ContentPane for the "Pause" button.
		private ContentPane replayButton;	//The ContentPane for the "Replay" button.
		private ContentPane homeButton;      //The ContentPane for the "Home" button.
		//private ContentPane instructionText; //The ContentPane for DifficultySelectScreen's instruction text.
		
		private GUIManager manager;    //The GUIManager that manages mainWindow.
		private MainWindow mainWindow; //The MainWindow that DifficultySelectScreen components are added to.
		private fxMediaPanel mediaPanel;
		
		//The ModuleSelectButtonInterface that will be notified when a difficulty level has been selected by the user.
		private ModuleSelectButtonInterface observer;

		//Media Player Support
		private Group mediaRoot;
		private Scene mediaScene;
		
		private Media tutorialVideo;
		private MediaPlayer VideoPlayer;
		private MediaView mediaViewer;	
		
	/**
	 * The public constructor for DifficultySelectScreen. Populates the MainWindow specified by
	 *  manager.getMainWindow() with DifficultySelectScreen components.
	 * @param manager The GUIManager that manages the MainWindow that is to be populated.
	 * @param observer The ModuleSelectButtonInterface that is to be notified when the user selects a
	 *  difficulty level. (change this)
	 * @throws IOException Thrown if any DifficultySelectScreen image file can not be read.
	 */
	public TutorialPlayerScreen(GUIManager manager, ModuleSelectButtonInterface observer) throws IOException{
		this.manager    = manager;
		this.mainWindow = manager.getMainWindow();
		this.observer   = observer;
		this.mediaPanel = manager.getMediaPanel();
		init();
	}
	
	/**
	 * Used to initialize TutorialPlayerScreen components.
	 * @throws IOException Thrown if any DifficultySelectScreen image file can not be read.
	 */
	private void init() throws IOException{
		initBackground();
		initMediaPlayer();
		initButtons();
		//initText();
	}
	
	/**
	 * Used to change mainWindow's background image.
	 * @throws IOException Thrown if the background image file can not be read.
	 */
	private void initBackground() throws IOException{
		String path = DIRECTORY_PATH + "background_plain.png";
		BufferedImage image = ImageLoader.getBufferedImage(path);
		mainWindow.setBackgroundImage(image);
	}
	
	private void initMediaPlayer() throws IOException {
		this.mediaPanel.getMediaPanel().setVisible(true);
		
		//Initialize FX Panel
		this.mediaRoot = new Group();
		this.mediaScene = new Scene(this.mediaRoot, 0, 0);
		
		//Open/prepare the file
		//String tutorialFilePath = new File("").getAbsolutePath() + DIRECTORY_PATH + "Tutorial.mp4";
		String tutorialFilePath = new File("").getAbsolutePath() + MEDIA_PATH + this.observer.getName() +"Tutorial.mp4";
		File mediaFile = new File(tutorialFilePath);
		this.tutorialVideo = new Media(mediaFile.toURI().toString());
		
		//Create the media player
		this.VideoPlayer = new MediaPlayer(this.tutorialVideo);  //Error here
		this.VideoPlayer.setAutoPlay(false);
		
		this.mediaViewer = new MediaView(this.VideoPlayer);
		this.mediaViewer.setFitHeight(this.mediaPanel.getPanelHeight());
		this.mediaViewer.setFitWidth(this.mediaPanel.getPanelWidth());
		((Group)this.mediaScene.getRoot()).getChildren().add(this.mediaViewer);
		
		this.mediaPanel.getMediaPanel().setScene(this.mediaScene);
	}
	
	/**
	 * Used to create and register TutorialPlayerScreen's buttons, as well as add the buttons
	 *  to mainWindow.
	 * @throws IOException Thrown if any button's image file can not be read.
	 */
	private void initButtons() throws IOException{
		playButton   = makeButton(DIRECTORY_PATH + "playButton.png", "Play");
		pauseButton   = makeButton(DIRECTORY_PATH + "pauseButton.png", "Pause");
		replayButton   = makeButton(DIRECTORY_PATH + "replayButton.png", "Replay");
		homeButton   = HomeButtonMaker.getContentPane();
		
		playButton.registerObserver(this);
		pauseButton.registerObserver(this);
		replayButton.registerObserver(this);
		homeButton.registerObserver(this);
		
		addButtons();
	}
	
	/**
	 * Used to add TutorialPlayerScreen's buttons, to mainWindow.
	 */
	private void addButtons(){
		int totalButtonWidth = playButton.getPreferredSize().width;
		int padding = ((mainWindow.getPreferredSize().width - totalButtonWidth) / 2);
		
		mainWindow.addLayer(playButton, BUTTON_LAYER, padding, 550);
		mainWindow.addLayer(homeButton, BUTTON_LAYER, HomeButtonMaker.getXDefault(), HomeButtonMaker.getYDefault());
	}
	
	/**
	 * A helper method used to create ContentPane button objects.
	 * @param imagePath The String describing the button's file path from the program's
	 *  root directory.
	 * @param name A String describing the button's name.
	 * @return A ContentPane that has the background image specified by imagePath, and
	 *  name specified by name.
	 * @throws IOException Thrown if the specified image file can not be read.
	 */
	private ContentPane makeButton(String imagePath, String name) throws IOException{
		BufferedImage image = ImageLoader.getBufferedImage(imagePath);
		ContentPane   panel = new ContentPane(image, true, false); 
		panel.setName(name);
		return panel;
	}
	
	/**
	 * Used to remove all components that were created by this TutorialPlayerScreen from mainWindow.
	 * WARNING: This method renders this instance of TutorialPlayerScreen inoperable. All references
	 *  to this instance should be removed for garbage collection. If another TutorialPlayerScreen
	 *  screen is needed, a new instance should be created.
	 */
	public void tearDown(){
		playButton.removeObserver(this);
		pauseButton.removeObserver(this);
		replayButton.removeObserver(this);
		homeButton.removeObserver(this);
		
		mainWindow.getContainer().remove(playButton);
		mainWindow.getContainer().remove(pauseButton);
		mainWindow.getContainer().remove(replayButton);
		mainWindow.getContainer().remove(homeButton);
		//mainWindow.getContainer().remove(instructionText);
		
		playButton      = null;
		pauseButton		= null;
		replayButton	= null;
		homeButton      = null;
		//instructionText = null;
		
		//Stop the JFX Player and Remove
		//this.mainWindow.removeLayer(this.mediaPanel.getMediaPanel());
		this.mediaPanel.getMediaPanel().setVisible(false);
		this.VideoPlayer.stop();
		//this.VideoPlayer.dispose();
		//this.fxPanel.removeAll();
		//this.mediaRoot.getChildren().removeAll();
		
		//this.mediaRoot = null;
		//this.mediaScene = null;
		//this.mediaViewer = null;
		//this.tutorialVideo  = null;
		//this.VideoPlayer = null;
		//this.fxPanel = null;
	}
	
	@Override
	public void clicked(JComponent component) {
		if(component == playButton) {
			System.out.println("I am play button");
			//Pause video
			this.VideoPlayer.play();
			
			//Remove play button
			mainWindow.removeLayer(playButton);
			
			//Add pause button
			int totalButtonWidth = pauseButton.getPreferredSize().width;
			int padding = ((mainWindow.getPreferredSize().width - totalButtonWidth) / 2);
			mainWindow.addLayer(pauseButton, BUTTON_LAYER, padding, 550);
		}
		else if(component == pauseButton) {
			System.out.println("I am pause button");
			
			//Pause video
			this.VideoPlayer.pause();
			
			//Remove pause button
			mainWindow.removeLayer(pauseButton);
			
			//Add play button
			int totalButtonWidth = playButton.getPreferredSize().width;
			int padding = ((mainWindow.getPreferredSize().width - totalButtonWidth) / 2);
			mainWindow.addLayer(playButton, BUTTON_LAYER, padding, 550);
		}
		else if(component == replayButton) {
			System.out.println("I am replay button");
			
			//Replay video
			this.VideoPlayer.play();
			
			//Remove replay button
			mainWindow.removeLayer(replayButton);
			
			//Add pause button
			int totalButtonWidth = pauseButton.getPreferredSize().width;
			int padding = ((mainWindow.getPreferredSize().width - totalButtonWidth) / 2);
			mainWindow.addLayer(pauseButton, BUTTON_LAYER, padding, 550);
			
		}
		//Listener?
		else if(component == homeButton){
			tearDown();
			
			try {
				manager.buildWelcomeScreen();
			} catch (IOException e) {
				manager.handleException(e);
			}
		}else
			throw new IllegalStateException("TutorialPlayerScreen observed a click event, but did nothing!");
	}
}