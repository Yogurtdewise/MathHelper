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
package project.screens;

import java.awt.Color;
import java.awt.FontFormatException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JLabel;

import project.interfaces.ClickableObserver;
import project.run.GUIManager;
import project.tools.ContentPane;
import project.tools.FontMaker;
import project.tools.ImageLoader;
import project.tools.MainWindow;

import java.awt.image.BufferedImage;

/**
 * This class is used to display a Welcome Screen in a MainWindow. A WelcomeScreen
 *  has a welcome message in the top-left of of the MainWindow & includes the specified
 *  student's name. Additionally, a WelcomeScreen has four option buttons; Tutorial,
 *  Practice, Test, and Report Card. Clicking the first 3 option buttons opens the
 *  appropriate ModuleSelectScreen. Clicking the last option button, opens the
 *  ReportCardScreen. Lastly, a Logout button is displayed in the bottom-left of the
 *  MainWindow.
 * @author Kenneth Chin
 */
public class WelcomeScreen implements ClickableObserver{
	
	private static final int BUTTON_LAYER = 2; //The default layer for button objects.
	private static final int TEXT_LAYER   = 3; //The default layer for text objects.
	
	private String welcomeMsg   = "Welcome, "; //The text that will be displayed as a welcome message.
	private JLabel welcomeText; //The JLabel that will display welcomeMsg.
	
	private ContentPane logoutButton;
	
	private GUIManager manager;    //The GUIManager that manages mainWindow & all GUI screens.
	private MainWindow mainWindow; //The MainWindow that this WelcomeScreen will add components to.
	
	/**
	 * The constructor for WelcomeScreen. Populates the specified MainWindow with four
	 *  option buttons, a "Logout" button, and a welcome message.
	 * @param manager The GUIManager that manages the primary MainWindow & all GUI screens.
	 * @param studentName A String describing a student's name, that will be displayed in
	 *  the welcome message.
	 * @throws IOException Thrown if a image or font file can not be read.
	 */
	public WelcomeScreen(GUIManager manager, String studentName)
			throws IOException{
		this.manager    = manager;
		this.mainWindow = manager.getMainWindow();
		welcomeMsg = welcomeMsg +"<br>&nbsp;&nbsp;&nbsp;&nbsp;"+ studentName + "!";
		init();
	}
	
	/**
	 * Used to initialize and display the graphical components of WelcomeScreen.
	 * @throws IOException Thrown if a image or font file can not be read.
	 */
	private void init() throws IOException{
		setBackground();
		initButtons();
		addButtons();
		addWelcomeText();
	}
	
	/**
	 * Used to obtain the MainWindow that WelcomeScreen components are added to.
	 * @return The MainWindow that WelcomeScreen components are added to.
	 */
	private MainWindow getMainWindow(){
		return mainWindow;
	}
	
	/**
	 * Used to set mainWindow's background image to its default.
	 */
	private void setBackground(){
		mainWindow.setBackgroundToDefault();
	}
	
	/**
	 * Used to create and set a ContentPane for each Button object.
	 * @throws IOException Thrown if a button's image file can not be read.
	 */
	private void initButtons() throws IOException{
		for(Button button:Button.values()){
			button.setButton(makeButton(button.getPath(), button.getName()));
		}
		initLogoutButton();
	}
	
	/**
	 * TODO Change this implementation to use a Logout icon.
	 * @throws IOException Thrown if the logout button's image file can not be read.
	 */
	private void initLogoutButton() throws IOException{
		String filePath = "\\images\\global\\ExitBtn.png";
		String name     = "Exit";
		logoutButton = makeButton(filePath, name);
	}
	
	/**
	 * A helper method used to create a ContentPane for the specified button.
	 * @param imagePath A String describing a button's image file path from the program's root directory.
	 * @param name A String describing a button's name.
	 * @return A ContentPane for the specified button.
	 * @throws IOException Thrown if a button's image file can not be read.
	 */
	private ContentPane makeButton(String imagePath, String name) throws IOException{
		BufferedImage image = ImageLoader.getBufferedImage(imagePath);
		ContentPane   panel = new ContentPane(image, true, false); 
		panel.setName(name);
		return panel;
	}
	
	/**
	 * Used to create and display a welcome message in mainWindow.
	 * @throws IOException Thrown if the ARIAL font file can not be read.
	 * @throws FontFormatException Thrown if the ARIAL font can not be read.
	 */
	private void addWelcomeText() throws IOException{
		welcomeText = new JLabel("<HTML><i>" + welcomeMsg + "</i><HTML>");
		try{
			welcomeText.setFont(FontMaker.getFont(FontMaker.ARIAL, 32));
		}catch(IndexOutOfBoundsException | FontFormatException e){
			welcomeText.setFont(FontMaker.getDefaultFont(32));
		}
		welcomeText.setForeground(Color.YELLOW);
		mainWindow.addLayer(welcomeText, TEXT_LAYER, 60, 100);
	}
	
	/**
	 * Adds all Button ContentPanes to mainWindow, and registers this WelcomeScreen with each button.
	 */
	private void addButtons(){
		for(Button button:Button.values()){
			mainWindow.addLayer(button.getButton(), BUTTON_LAYER, button.getX(), button.getY());
		}
		registerButtons();
		mainWindow.addLayer(logoutButton, BUTTON_LAYER, 20, 640);
	}
	
	/**
	 * A helper method used to register this WelcomeScreen with each Button, as a ClickableObserver.
	 */
	private void registerButtons(){
		for(Button button:Button.values()){
			button.getButton().registerObserver(this);
		}
		logoutButton.registerObserver(this);
	}
	
	/**
	 * This enum describes all option buttons used by WelcomeScreen.
	 * @author Kenneth Chin
	 */
	private enum Button{
		/**
		 * The Tutorial button.
		 */
		TUTORIAL("tutorial", "\\images\\welcome\\TutorialBtn.png", 275, 350) {
			@Override
			public void doAction(WelcomeScreen screen) {
				screen.tearDown();
				try{
					screen.manager.buildTutorialModuleSelect();
				}catch (IndexOutOfBoundsException | IOException e) {
					screen.manager.handleException(e);
				}
			}
		},
		/**
		 * The Practice button.
		 */
		PRACTICE("practice", "\\images\\welcome\\PracticeBtn.png", 600, 350) {
			@Override
			public void doAction(WelcomeScreen screen) {
				screen.tearDown();
				try{
					screen.manager.buildPracticeModuleSelect();
				}catch (IndexOutOfBoundsException | IOException e) {
					screen.manager.handleException(e);
				}
			}
		},
		/**
		 * The Test button.
		 */
		TEST    ("test",     "\\images\\welcome\\TestBtn.png", 275, 500) {
			@Override
			public void doAction(WelcomeScreen screen) {
				screen.tearDown();
				try{
					screen.manager.buildTestModuleSelect();
				}catch (IndexOutOfBoundsException | IOException e) {
					screen.manager.handleException(e);
				}
			}
		},
		/**
		 * The Report Card button.
		 */
		REPORT  ("report",   "\\images\\welcome\\ReportCardBtn.png", 600, 500) {
			@Override
			public void doAction(WelcomeScreen screen) {
				screen.tearDown();
				try {
					screen.manager.buildReportCardScreen();
				} catch (IOException e) {
					screen.manager.handleException(e);
				}
			}
		};
		
		private String name; //The button's name.
		private String path; //The button's image file path from the program's root directory.
		private int x;       //The button's x-origin relative to mainWindow.
		private int y;       //The button's y-origin relative to mainWindow.
		private ContentPane button; //The button's ContentPane.
		
		/**
		 * The constructor for all Button objects.
		 * @param name A String describing the button's name.
		 * @param path A String describing the button's image file path from the program's root directory.
		 * @param xOrigin An int describing the button's x-origin relative to mainWindow.
		 * @param yOrigin An int describing the button's y-origin relative to mainWindow.
		 */
		private Button(String name, String path, int xOrigin, int yOrigin){
			this.name = name;
			this.path = path;
			this.x    = xOrigin;
			this.y    = yOrigin;
		}
		
		/**
		 * Used to obtain this button's name.
		 * @return A String describing this button's name.
		 */
		protected String getName(){
			return name;
		}
		
		/**
		 * Used to obtain this button's image file path from the program's root directory.
		 * @return A String describing this button's image file path from the program's root directory.
		 */
		private String getPath(){
			return path;
		}
		
		/**
		 * Used to obtain this button's x-origin relative to mainWindow.
		 * @return An int describing this button's x-origin relative to mainWindow.
		 */
		private int getX(){
			return x;
		}
		
		/**
		 * Used to obtain this button's y-origin relative to mainWindow.
		 * @return An int describing this button's y-origin relative to mainWindow.
		 */
		private int getY(){
			return y;
		}
		
		/**
		 * Used to set this button's ContentPane.
		 * @param button This button's ContentPane.
		 */
		private void setButton(ContentPane button){
			this.button = button;
		}
		
		/**
		 * Used to obtain this button's ContentPane.
		 * @return This button's ContentPane.
		 */
		private ContentPane getButton(){
			return button;
		}
		
		/**
		 * Executes the actions that are associated with this button, when it is clicked.
		 * @param screen The WelcomeScreen that called this method.
		 */
		public abstract void doAction(WelcomeScreen screen);
	}
	
	/**
	 * Used to remove all components that were created by this WelcomeSreen from mainWindow.
	 * WARNING: This method renders this instance of WelcomeSreen inoperable. All references
	 *  to this instance should be removed for garbage collection. If another WelcomeSreen
	 *  screen is needed, a new instance should be created.
	 */
	public void tearDown(){
		mainWindow.getContainer().remove(welcomeText);
		mainWindow.getContainer().remove(logoutButton);
		for(Button button:Button.values()){
			ContentPane thisButton= button.getButton();
			thisButton.removeObserver(this);
			mainWindow.getContainer().remove(thisButton);
		}
	}
	
	@Override
	public void clicked(JComponent component) {
		System.out.println(component.getName() + " was clicked!");
		//TODO Change this implementation to open a LoginScreen.
		if(component == logoutButton)
			manager.exit();;
		for(Button button:Button.values()){
			if(component == button.getButton()){
				button.doAction(this);
				break;
			}
		}
	}
}
