/**
 * Name:         Math Helper
 * Version:      0.11.3
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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import project.interfaces.ClickableObserver;
import project.interfaces.EnumerableButtonFactory;
import project.interfaces.ModuleSelectButtonInterface;
import project.run.GUIManager;
import project.tools.ContentPane;
import project.tools.FontMaker;
import project.tools.ImageLoader;
import project.tools.MainWindow;

/**
 * This class is used as a concrete version of ModuleSelectScreen. It specifies
 *  actions that can be performed by PreK-K students. A particular set of selectable
 *  modules are defined by the EnumerableButtonFactory that is passed to this class.
 *  The class populates a MainWindow with the passed button set, with a maximum number
 *  of buttons shown defined by ModuleSelectScreen.MAX_BUTTONS_SHOWING. Clicking one
 *  such button will execute the module that is associated with the button. All module
 *  buttons can be viewed via pagination, by using left and right arrows displayed at
 *  the bottom of the screen. A student may return to the WelcomeScreen by clicking
 *  the "Home" button located at the far bottom-left of the screen. The background
 *  image is also defined by ModuleSelectScreen. Finally, a title is displayed at
 *  the top of the MainWindow. The title text message is defined by the
 *  EnumerableButtonFactory that is passed to this class.
 * @author Kenneth Chin
 *
 */
public class PreKModuleSelect extends ModuleSelectScreen implements ClickableObserver {
	
	//Tracks the index of the first button showing.
	private int currentFirstButtonIndex = DEFAULT_FIRST_BUTTON_INDEX;
	
	private ModuleSelectButtonInterface[] buttonList; //An array of module selection buttons.
	private int numberOfButtons; //The number of buttons in buttonList.
	private String titleText;    //The message to be displayed as a title.
	
	private JComponent titleLabel; //The JLabel used to draw the titleText message.
	
	//Used to determine if the LEFT_ARROW ControlButton is showing.
	private boolean isLeftArrowShowing  = false;
	//Used to determine if the RIGHT_ARROW ControlButton is showing.
	private boolean isRightArrowShowing = false;
	
	/**
	 * Creates a ModuleSelectScreen that displays the buttons and title described by
	 *  the specified EnumerableButtonFactory.
	 * @param manager The GUIManager that manages the primary MainWindow & all GUI screens.
	 * @param buttonFactory The EnumerableButtonFactory that describes the buttons that will
	 *  be used to populate mainWindow.
	 * @throws IOException Thrown if any image file can not be read.
	 */
	public PreKModuleSelect(GUIManager manager, EnumerableButtonFactory buttonFactory)
			throws IOException{
		super(manager);
		
		buttonList = buttonFactory.getButtons();
		numberOfButtons = buttonList.length;
		titleText = buttonFactory.getTitleText();
		
		initGraphics();
	}
	
	/**
	 * Used to initialize all of PreKModuleSelect's graphic components.
	 * @throws IOException Thrown if any image file can not be read.
	 */
	private void initGraphics() throws IOException{
		
		addButtons(DEFAULT_FIRST_BUTTON_INDEX);
		addTitleText();
		initControlButtons();
	}
	
	/**
	 * Used to create and set ContentPane button objects for each ControlButton enumeration.
	 * Afterwards, this method displays the appropriate ControlButtons.
	 * @throws IOException Thrown if any image file can not be read.
	 */
	private void initControlButtons() throws IOException{
		for(ControlButton control:ControlButton.values()){
			control.setButton(makeButton(control.getPath(), control.getName()));
		}
		MainWindow mainWindow = getMainWindow();
		ControlButton home = ControlButton.HOME;
		home.getButton().registerObserver(this);
		mainWindow.addLayer(home.getButton(), BUTTON_LAYER, home.getX(), home.getY());
		updatePageControls();
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
	 * Used to determine which pagination buttons should be displayed & displays them.
	 */
	private void updatePageControls(){
		//Check the left arrow.
		if(hasPrevPages() && !isLeftArrowShowing){
			addPageControlButton(ControlButton.LEFT_ARROW);
			isLeftArrowShowing = true;
		}else if(!hasPrevPages() && isLeftArrowShowing){
			removePageControlButton(ControlButton.LEFT_ARROW);
			isLeftArrowShowing = false;
		}
		//Check the right arrow.
		if(hasMorePages() && !isRightArrowShowing){
			addPageControlButton(ControlButton.RIGHT_ARROW);
			isRightArrowShowing = true;
		}else if(!hasMorePages() && isRightArrowShowing){
			removePageControlButton(ControlButton.RIGHT_ARROW);
			isRightArrowShowing = false;
		}
			
	}
	
	/**
	 * Displays the specified ControlButton.
	 * @param control A ControlButton to be displayed.
	 */
	private void addPageControlButton(ControlButton control){
		MainWindow mainWindow = getMainWindow();
		ContentPane button = control.getButton();
		button.registerObserver(this);
		mainWindow.addLayer(button, BUTTON_LAYER, control.getX(), control.getY());
	}
	
	/**
	 * Removes the specified ControlButton from mainWindow.
	 * @param control A ControlButton to be removed from mainWindow.
	 */
	private void removePageControlButton(ControlButton control){
		MainWindow mainWindow = getMainWindow();
		ContentPane button = control.getButton();
		button.removeObserver(this);
		mainWindow.getContainer().remove(button);
	}
	
	/**
	 * Used to control which module buttons are display. The range of buttons
	 *  to be displayed is specified by the passed parameter, firstButtonToShow.
	 *  The range is equal to (firstButtonToShow, firstButtonToShow + MAX_BUTTONS_SHOWING - 1),
	 *  or (firstButtonToShow, numberOfButtons - 1) if the last page of buttons is to be shown.
	 *  Any module buttons that were previously displayed will be removed from mainWindow.
	 * @param firstButtonToShow The index of the first module button to show.
	 */
	private void changePage(int firstButtonToShow){
		removeButtons();
		currentFirstButtonIndex = firstButtonToShow;
		addButtons(currentFirstButtonIndex);
		updatePageControls();
	}
	
	/**
	 * Used to create and display the JLabel that displays the titleText message at the
	 *  top-center of mainWindow. 
	 */
	private void addTitleText(){
		MainWindow mainWindow = getMainWindow();
		titleLabel = new JLabel("<HTML>" + titleText + "<HTML>", SwingConstants.CENTER);
		Font font;
		try{
			font = FontMaker.getFont(FontMaker.ARIAL, 32);
		}catch(IndexOutOfBoundsException | IOException | FontFormatException e){
			font = FontMaker.getDefaultFont(32);
		}
		titleLabel.setFont(font);
		titleLabel.setForeground(Color.YELLOW);
		titleLabel.setPreferredSize(new Dimension(mainWindow.getPreferredSize().width,
				FontMaker.getFontHeight(font, mainWindow.getGraphics())));
		mainWindow.addLayer(titleLabel, TEXT_LAYER, 0, 70);
	}
	
	/**
	 * Used to display a range of module buttons equal to
	 *  (firstButtonToShow, firstButtonToShow + MAX_BUTTONS_SHOWING - 1), or
	 *  (firstButtonToShow, numberOfButtons - 1) if the last page of buttons is to be shown.
	 * @param firstButtonIndex The index of the first module button to show.
	 */
	private void addButtons(int firstButtonIndex){
		int lastButtonIndex;                           //The index of the last button to show.
		if(firstButtonIndex + MAX_BUTTONS_SHOWING >= numberOfButtons)
			lastButtonIndex = numberOfButtons - 1;
		else
			lastButtonIndex = firstButtonIndex + MAX_BUTTONS_SHOWING - 1;
		for(int i = firstButtonIndex; i <= lastButtonIndex; i++){
			ContentPane buttonPane = buttonList[i].getButton();
			buttonPane.registerObserver(this);
			getMainWindow().addLayer(buttonPane, BUTTON_LAYER, buttonList[i].getX(), buttonList[i].getY());
		}
	}
	
	/**
	 * Removes all module buttons currently being displayed in mainWindow.
	 */
	private void removeButtons(){
		int lastButtonIndex = currentFirstButtonIndex + MAX_BUTTONS_SHOWING - 1; //The index of the last button that is showing.
		if(lastButtonIndex >= numberOfButtons - 1)
			lastButtonIndex = numberOfButtons - 1;
		for(int i = currentFirstButtonIndex; i<=lastButtonIndex; i++){
			ContentPane buttonPane = buttonList[i].getButton();
			buttonPane.removeObserver(this);
			getMainWindow().getContainer().remove(buttonPane);
		}
	}
	
	/**
	 * A convenience method used to determine the total number of module buttons supported by
	 *  this ModuleSelectScreen.
	 * @return An int indicating the total number of module buttons supported by this
	 *  ModuleSelectScreen.
	 */
	public int getNumberOfButtons(){
		return numberOfButtons;
	}
	
	/**
	 * A helper method used to determine if there are more module buttons that could be displayed
	 *  by clicking the right pagination button.
	 * @return A boolean indicating true if there are more module buttons that could be displayed
	 *  by clicking the right pagination button; false otherwise.
	 */
	private boolean hasMorePages(){
		return (currentFirstButtonIndex + MAX_BUTTONS_SHOWING < numberOfButtons);
	}
	
	/**
	 * A helper method used to determine if there are more module buttons that could be displayed
	 *  by clicking the left pagination button.
	 * @return A boolean indicating true if there are more module buttons that could be displayed
	 *  by clicking the left pagination button; false otherwise.
	 */
	private boolean hasPrevPages(){
		return(currentFirstButtonIndex > 0);
	}
	
	/**
	 * Used to remove all components that were created by this PreKModuleSelect from mainWindow.
	 * WARNING: This method renders this instance of PreKModuleSelect inoperable. All references
	 *  to this instance should be removed for garbage collection. If another PreKModuleSelect
	 *  screen is needed, a new instance should be created.
	 */
	@Override
	public void tearDown(){
		MainWindow mainWindow = getMainWindow();
		mainWindow.getContainer().remove(titleLabel);
		titleLabel = null;
		removeButtons();
		buttonList = null;
		for(ControlButton control:ControlButton.values()){
			ContentPane button = control.getButton();
			if((control == ControlButton.LEFT_ARROW && isLeftArrowShowing)
					|| (control == ControlButton.RIGHT_ARROW && isRightArrowShowing)
					|| (control == ControlButton.HOME)){
				mainWindow.getContainer().remove(button);
			}
			button.removeObserver(this);
		}
	}
	
	@Override
	public void clicked(JComponent component) {
		boolean actionPerformed = false;
		//Check if a ModuleSelectButtonInterface button was clicked.
		for(int i = 0; i< buttonList.length; i++){
			ModuleSelectButtonInterface button = buttonList[i];
			if(component == button.getButton()){
				System.out.println(button.getName() + " was clicked!");
				button.doAction(this);
				actionPerformed = true;
				break;
			}
		}
		if(!actionPerformed){
			//Check if a ControlButton was clicked.
			for(ControlButton control:ControlButton.values()){
				if(component == control.getButton()){
					System.out.println(control.getButton().getName() + " was clicked!");
					control.doAction(this);
					actionPerformed = true;
					break;
				}
			}
		}
		//An unexpected action occurred.
		if(!actionPerformed)
			throw new IllegalStateException("PreKModuleSelect.clicked(component) heard an event,"
					+ " but didn't do anything!");
	}
	
	/**
	 * This enum describes the control buttons that are used by all PreKModuleSelect objects.
	 * @author Kenneth Chin
	 */
	private enum ControlButton{
		/**
		 * The "Home" button.
		 */
		HOME       ("Home"         , "\\images\\global\\HomeBtn.png"         ,  20, 640) {
			@Override
			public void doAction(PreKModuleSelect screen){
				screen.tearDown();
				try{
					screen.getManager().buildWelcomeScreen();
				}catch (IOException e) {
					screen.getManager().handleException(e);
				}
			}
		},
		/**
		 * The Left pagination arrow.
		 */
		LEFT_ARROW ("Previous Page", "\\images\\moduleSelect\\LeftArrow.png" , 300, 640) {
			@Override
			public void doAction(PreKModuleSelect screen) {
				int newStartingButton = screen.currentFirstButtonIndex - MAX_BUTTONS_SHOWING;
				screen.changePage(newStartingButton);
			}
		},
		/**
		 * The Right pagination arrow.
		 */
		RIGHT_ARROW("Next Page"    , "\\images\\moduleSelect\\RightArrow.png", 660, 640) {
			@Override
			public void doAction(PreKModuleSelect screen) {
				int newStartingButton = screen.currentFirstButtonIndex + MAX_BUTTONS_SHOWING;
				screen.changePage(newStartingButton);
			}
		};
		
		private String name; //The button's name.
		private String path; //The button's image file path.
		private int x;       //The button's x-origin.
		private int y;       //The button's y-origin.
		
		private ContentPane button; //The button's ContentPane.
		
		/**
		 * The constructor for all ControlButtons.
		 * @param name A String describing the button's name.
		 * @param filePath A String describing the button's image file path from the program's root directory.
		 * @param xOrigin An int describing the button's x-origin.
		 * @param yOrigin An int describing the button's y-origin.
		 */
		private ControlButton(String name, String filePath, int xOrigin, int yOrigin){
			this.name   = name;
			this.path   = filePath;
			this.x      = xOrigin;
			this.y      = yOrigin;
		}
		
		/**
		 * Used to obtain the button's name.
		 * @return A String describing the button's name.
		 */
		protected String getName(){
			return name;
		}
		
		/**
		 * Used to obtain the button's image file path from the program's root directory.
		 * @return A String describing the button's image file path from the program's root directory.
		 */
		private String getPath(){
			return path;
		}
		
		/**
		 * Used to obtain the button's x-origin.
		 * @return An int describing the button's x-origin.
		 */
		private int getX(){
			return x;
		}
		
		/**
		 * Used to obtain the button's y-origin.
		 * @return An int describing the button's y-origin.
		 */
		private int getY(){
			return y;
		}
		
		/**
		 * Used to set the button's ContentPane.
		 * @param button The button's ContentPane.
		 */
		private void setButton(ContentPane button){
			this.button = button;
		}
		
		/**
		 * Used to obtain the button's ContentPane.
		 * @return The button's ContentPane.
		 */
		private ContentPane getButton(){
			return button;
		}
		
		/**
		 * Executes the actions that are associated with this button, when it is clicked.
		 * @param screen The PreKModuleSelect that called this method.
		 */
		public abstract void doAction(PreKModuleSelect screen);
	}
}
