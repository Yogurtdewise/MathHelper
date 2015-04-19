/**
 * Name:         Math Helper
 * Version:      0.11.0
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
import java.io.IOException;

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
 * This class is used by classes/enums that implement ModuleSelectButtonInterface to obtain
 *  a DifficultyLevel from the user. This class populates GUIManager's MainWindow with a "Easy", 
 *  "Normal", and "Hard" difficulty level buttons, as well as a "Home" button. Clicking on a
 *  difficulty level button calls the difficultySelected(DifficultyLevel) method of the passed
 *  ModuleSelectButtonInterface. Clicking the "Home" button calls tearDown(), then creates a
 *  new WelcomeScreen.
 * @author Kenneth Chin
 */
public class DifficultySelectScreen implements ClickableObserver{

	//Layer constants for DifficultySelectScreen components.
	private static final int BUTTON_LAYER = 3;
	private static final int TEXT_LAYER   = 2;
	
	private static final int PREFERRED_FONT = FontMaker.ARIAL;
	
	//The path from the program's directory to the DifficultySelectScreen image folder.
	private static final String DIRECTORY_PATH = "\\images\\difficulty\\";
	
	private ContentPane easyButton;      //The ContentPane for the "Easy" button.
	private ContentPane normalButton;    //The ContentPane for the "Normal" button.
	private ContentPane hardButton;      //The ContentPane for the "Hard" button.
	private ContentPane homeButton;      //The ContentPane for the "Home" button.
	private ContentPane instructionText; //The ContentPane for DifficultySelectScreen's instruction text.
	
	private GUIManager manager;    //The GUIManager that manages mainWindow.
	private MainWindow mainWindow; //The MainWindow that DifficultySelectScreen components are added to.
	
	//The ModuleSelectButtonInterface that will be notified when a difficulty level has been selected by the user.
	private ModuleSelectButtonInterface observer;
	
	/**
	 * The public constructor for DifficultySelectScreen. Populates the MainWindow specified by
	 *  manager.getMainWindow() with DifficultySelectScreen components.
	 * @param manager The GUIManager that manages the MainWindow that is to be populated.
	 * @param observer The ModuleSelectButtonInterface that is to be notified when the user selects a
	 *  difficulty level.
	 * @throws IOException Thrown if any DifficultySelectScreen image file can not be read.
	 */
	public DifficultySelectScreen(GUIManager manager, ModuleSelectButtonInterface observer) throws IOException{
		this.manager    = manager;
		this.mainWindow = manager.getMainWindow();
		this.observer   = observer;
		init();
	}
	
	/**
	 * Used to initialize DifficultySelectScreen components.
	 * @throws IOException Thrown if any DifficultySelectScreen image file can not be read.
	 */
	private void init() throws IOException{
		initBackground();
		initButtons();
		initText();
	}
	
	/**
	 * Used to change mainWindow's background image.
	 * @throws IOException Thrown if the background image file can not be read.
	 */
	private void initBackground() throws IOException{
		String path = DIRECTORY_PATH + "Background.png";
		BufferedImage image = ImageLoader.getBufferedImage(path);
		mainWindow.setBackgroundImage(image);
	}
	
	/**
	 * Used to create and register DifficultySelectScreen's buttons, as well as add the buttons
	 *  to mainWindow.
	 * @throws IOException Thrown if any button's image file can not be read.
	 */
	private void initButtons() throws IOException{
		easyButton   = makeButton(DIRECTORY_PATH + DifficultyLevel.EASY.getFilename(),
									DifficultyLevel.EASY.getName());
		normalButton = makeButton(DIRECTORY_PATH + DifficultyLevel.NORMAL.getFilename(),
									DifficultyLevel.NORMAL.getName());
		hardButton   = makeButton(DIRECTORY_PATH + DifficultyLevel.HARD.getFilename(),
									DifficultyLevel.HARD.getName());
		homeButton   = HomeButtonMaker.getContentPane();
		
		easyButton.registerObserver(this);
		normalButton.registerObserver(this);
		hardButton.registerObserver(this);
		homeButton.registerObserver(this);
		
		addButtons();
	}
	
	/**
	 * Used to add DifficultySelectScreen's buttons, to mainWindow.
	 */
	private void addButtons(){
		int totalButtonWidth = easyButton.getPreferredSize().width
								+ normalButton.getPreferredSize().width
								+ hardButton.getPreferredSize().width;
		int padding = ((mainWindow.getPreferredSize().width - totalButtonWidth) / 4);
		
		mainWindow.addLayer(easyButton,   BUTTON_LAYER, padding, 100);
		mainWindow.addLayer(normalButton, BUTTON_LAYER, ((2*padding) + easyButton.getPreferredSize().width), 50);
		mainWindow.addLayer(hardButton,   BUTTON_LAYER, ((3*padding) 
				+ easyButton.getPreferredSize().width + normalButton.getPreferredSize().width), 100);
		mainWindow.addLayer(homeButton, BUTTON_LAYER, HomeButtonMaker.getXDefault(), HomeButtonMaker.getYDefault());
	}
	
	/**
	 * Used to create the instruction text for DifficultySelectScreen, and add it to mainWindow.
	 */
	private void initText(){
		Font font;
		try {
			font = FontMaker.getFont(PREFERRED_FONT, 48);
		} catch (IndexOutOfBoundsException | IOException | FontFormatException e) {
			font = FontMaker.getDefaultFont(48);
		}
		String message = "<p><CENTER>Choose a difficulty<br>"
							+ "for " + observer.getName() + "<CENTER></p>";
		JLabel label = new JLabel("<HTML><div>" + message + "</div></HTML>");
		label.setForeground(Color.YELLOW);
		label.setFont(font);
		
		int windowCenter = (mainWindow.getPreferredSize().width / 2);
		int textCenter   = (label.getPreferredSize().width / 2);
		int xOrigin      = windowCenter - textCenter;
		
		instructionText = new ContentPane(label.getPreferredSize().width,
							label.getPreferredSize().height, false, false);
		instructionText.addComponent(label, 0, 0);
		mainWindow.addLayer(instructionText, TEXT_LAYER, xOrigin, 440);
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
	 * Used to remove all components that were created by this DifficultySelectScreen from mainWindow.
	 * WARNING: This method renders this instance of DifficultySelectScreen inoperable. All references
	 *  to this instance should be removed for garbage collection. If another DifficultySelectScreen
	 *  screen is needed, a new instance should be created.
	 */
	public void tearDown(){
		easyButton.removeObserver(this);
		normalButton.removeObserver(this);
		hardButton.removeObserver(this);
		homeButton.removeObserver(this);
		
		mainWindow.getContainer().remove(easyButton);
		mainWindow.getContainer().remove(normalButton);
		mainWindow.getContainer().remove(hardButton);
		mainWindow.getContainer().remove(homeButton);
		mainWindow.getContainer().remove(instructionText);
		
		easyButton      = null;
		normalButton    = null;
		hardButton      = null;
		homeButton      = null;
		instructionText = null;
	}

	@Override
	public void clicked(JComponent component) {
		if(component == easyButton)
			observer.difficultySelected(DifficultyLevel.EASY);
		else if(component == normalButton)
			observer.difficultySelected(DifficultyLevel.NORMAL);
		else if(component == hardButton)
			observer.difficultySelected(DifficultyLevel.HARD);
		else if(component == homeButton){
			tearDown();
			try {
				manager.buildWelcomeScreen();
			} catch (IOException e) {
				manager.handleException(e);
			}
		}else
			throw new IllegalStateException("DifficultySelectScreen observed a click event, but did nothing!");
	}
	
}
