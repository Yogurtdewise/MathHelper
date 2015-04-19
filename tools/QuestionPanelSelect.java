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
package project.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import project.buttons.HomeButtonMaker;
import project.interfaces.ClickableObserver;
import project.interfaces.Testable;
import project.interfaces.TestableObserver;

/**
 * This class is used to ask a user a question that requires that they select one of two answer panels.
 *  It displays a question, two answer panels, a "Submit" button, and a "Home" button. A user may select
 *  one of two answer panels. If one panel is selected and the other is clicked, the previously selected
 *  panel becomes un-selected, and the clicked panel becomes selected. When the "Sumbit" button is
 *  clicked, all registered TestableObservers are notified with a String specified by the
 *  QuestionPanelSelect.Answer enum. If the user clicks the "Home" button, all registered
 *  TestableObservers will have their homeClicked() method called. It is up to the individual
 *  TestableObservers to implement an action to take on the homeClicked() event. The tearDown() method
 *  should be used to remove this QuestionPanelText from mainWindow.
 * @author Kenneth Chin
 */
public class QuestionPanelSelect implements Testable, ClickableObserver{
	
	//Constants used to create the black question box, where questions are asked.
	private static final String  QUESTION_BOX_IMAGE_PATH = "\\images\\test\\QuestionBox.png";
	private static final int     TEXT_LAYER              = 2;
	private static final int     PREFERRED_FONT          = FontMaker.ARIAL; //The preferred question font.
	
	//Constants used to create the "Submit" button.
	private static final String  SUBMIT_BTN_NAME       = "Submit";
	private static final String  SUBMIT_BTN_IMAGE_PATH = "\\images\\test\\SubmitBtn.png";
	private static final int     BUTTON_LAYER          = 3;
	
	//The file path from the program's root directory for the background image.
	private static final String  BACKGROUND_IMAGE_PATH = "\\images\\test\\background_plain.png";
	
	//The file path for the correct/incorrect images from the program's root directory.
	private static final String CORRECT_ICON_PATH = "\\images\\test\\Correct.png";
	private static final String WRONG_ICON_PATH   = "\\images\\test\\Wrong.png";

	private static final String  ANSWER = "answer"; //Used to identify a user answer event.
	private static final String  HOME   = "home";   //Used to identify a "Home" button event.
	private static final String  NEXT   = "next";   //Used to identify a "Next" button event.
	
	//The list of registered TestableObservers.
	private CopyOnWriteArrayList<TestableObserver> observers = new CopyOnWriteArrayList<TestableObserver>();
	
	private ContentPane nextButton   = null; //The ContentPane used to display the practice test's "Next" button.
	private ContentPane answerNote   = null; //The ContentPane used to display a question's answer note.
	private ContentPane answerIcon   = null; //The ContentPane used to display the icon indicating correct/incorrect.
	private ContentPane questionCounter;     //The ContentPane used to display the number of remaining questions.
	private ContentPane questionBox;         //The ContentPane where questions are asked.
	private JLabel      questionText = null; //The JLable used to write question text.
	private ContentPane leftPanel    = null; //The ContentPane used to display the left  answer panel.
	private ContentPane rightPanel   = null; //The ContentPane used to display the right answer panel.
	private ContentPane submitBtn;           //The ContentPane used to display the "Submit" button.
	private ContentPane homeBtn;             //The ContentPane used to display the "Home" button.
	
	private int maxNumQuestions     = 0;     //The maximum number of questions to be displayed in questionCounter.
	private Font counterFont;                //The Font used for questionCounter's text.
	
	private boolean isLeftSelected  = false; //Used to determine if the left  ContentPane is selected.
	private boolean isRightSelected = false; //Used to determine if the right ContentPane is selected.
	private boolean isLeftWrong     = false; //Used to determine if the left  ContentPane is selected.
	private boolean isRightWrong    = false; //Used to determine if the right ContentPane is selected.
	
	private int submitBtnX; //Used to identify the X origin for submitBtn & nextButton.
	private int submitBtnY; //Used to identify the Y origin for submitBtn & nextButton.
	
	private MainWindow mainWindow; //The MainWindow which is to have QuestionPanelSelect components added to.
	
	/**
	 * The QuestionPanelSelect constructor. Creates and displays a QuestionPanelSelect with no question or
	 *  answer panels. Use showQuestion() to display a question and two answer panels.
	 * @param mainWindow The MainWindow which is to have QuestionPanelSelect components added to.
	 * @throws IOException Thrown if any image file is missing.
	 */
	public QuestionPanelSelect(MainWindow mainWindow, int maxNumQuestions) throws IOException{
		this.mainWindow      = mainWindow;
		this.maxNumQuestions = maxNumQuestions;
		init();
	}
	
	/**
	 * Used to initialize the QuestionPanelSelect screen.
	 * @throws IOException Thrown if any image file is missing.
	 */
	private void init() throws IOException{
		initBackground();
		initCounter();
		initQuestionBox();
		initSubmitBtn();
		initHomeBtn();
		initNextBtn();
	}
	
	/**
	 * Used to set the background image.
	 * @throws IOException Thrown if the background image file is missing.
	 */
	private void initBackground() throws IOException{
		BufferedImage backgroundImage = ImageLoader.getBufferedImage(BACKGROUND_IMAGE_PATH);
		mainWindow.setBackgroundImage(backgroundImage);
	}
	
	/**
	 * Used to create and display the Question Box, where questions are asked.
	 * @throws IOException Thrown if the Question Box image file is missing.
	 */
	private void initQuestionBox() throws IOException{
		BufferedImage questionBoxImage = ImageLoader.getBufferedImage(QUESTION_BOX_IMAGE_PATH);
		questionBox = new ContentPane(questionBoxImage, false, false);
		questionBox.setName("Question Box");
		mainWindow.addLayer(questionBox, TEXT_LAYER, 80, 300);
	}
	
	/**
	 * Used to create and display the "Submit" button.
	 * @throws IOException Thrown if the "Submit" button image file is missing.
	 */
	private void initSubmitBtn() throws IOException{
		BufferedImage submitBtnImage = ImageLoader.getBufferedImage(SUBMIT_BTN_IMAGE_PATH);
		submitBtn = new ContentPane(submitBtnImage, true, false);
		submitBtn.setName(SUBMIT_BTN_NAME);
		
		//Find the horizontal center of the window & submit button offset.
		int windowCenter    = (int)(mainWindow.getPreferredSize().getWidth() / 2);
		int submitBtnCenter = (int)(submitBtn.getPreferredSize().getWidth()  / 2);
		submitBtnX          = windowCenter - submitBtnCenter;
		int yOffset         = mainWindow.getInsets().top + 10;
		submitBtnY          = (int)(mainWindow.getPreferredSize().getHeight()
								- submitBtn.getPreferredSize().getHeight()) - yOffset;
		
		mainWindow.addLayer(submitBtn, BUTTON_LAYER, submitBtnX, submitBtnY);
		submitBtn.registerObserver(this);
	}
	
	/**
	 * Used to create and display the "Home" button.
	 * @throws IOException Thrown if the "Home" button image file is missing.
	 */
	private void initHomeBtn() throws IOException{
		homeBtn = HomeButtonMaker.getContentPane();
		mainWindow.addLayer(homeBtn, BUTTON_LAYER, HomeButtonMaker.getXDefault(), HomeButtonMaker.getYDefault());
		homeBtn.registerObserver(this);
	}
	
	/**
	 * Used to create the "Next" button, which is not displayed on initialization.
	 * @throws IOException Thrown if the "Next" button image file can not be read.
	 */
	private void initNextBtn() throws IOException{
		nextButton = new ContentPane(ImageLoader.getBufferedImage("\\images\\test\\Next.png"), true, false);
		nextButton.registerObserver(this);
	}
	
	/**
	 * Used to create an empty questionCounter.
	 */
	private void initCounter(){
		//Set the font.
		try {
			counterFont = FontMaker.getFont(PREFERRED_FONT, 18);
		} catch (IndexOutOfBoundsException | IOException | FontFormatException e) {
			counterFont = FontMaker.getDefaultFont(18);
		}
		//Get the width of the largest possible String width.
		String biggestString = " Question: " + maxNumQuestions + " / " + maxNumQuestions + " ";
		int stringWidth = FontMaker.getStringWidth(counterFont, mainWindow.getGraphics(), biggestString);
		int padding     = 20;
		
		//Make and display the questionCounter.
		questionCounter = new ContentPane(stringWidth,
				(FontMaker.getFontHeight(counterFont, mainWindow.getGraphics()) + padding), false, false){
					@Override
					public void paintComponent(Graphics g) {
						super.paintComponent(g);
				
						Color oldColor = g.getColor();
						g.setColor(Color.GRAY);
						g.fillRect(0, 0, getWidth(), getHeight());
						g.setColor(oldColor);
					}
				};
		questionCounter.setBackground(Color.BLUE);
		questionCounter.setOpaque(true);
		questionCounter.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		int xOrigin     = mainWindow.getPreferredSize().width - (stringWidth + padding);
		int yOrigin     = mainWindow.getPreferredSize().height - (questionCounter.getPreferredSize().height + 50);
		mainWindow.addLayer(questionCounter, TEXT_LAYER, xOrigin, yOrigin);
	}
	
	/**
	 * Used to create and display two answer panels.
	 * @param image1 The background BufferedImage for the left answer panel.
	 * @param image2 The background BufferedImage for the right answer panel.
	 */
	private void makePanels(BufferedImage image1, BufferedImage image2){
		final int image1Width  = image1.getWidth();
		final int image1Height = image1.getHeight();
		final int image2Width  = image2.getWidth();
		final int image2Height = image2.getHeight();
		final int padding      = 20;
		
		//Make the panels.
		leftPanel = new ContentPane(image1, true, true, false){
			@Override
		    public void paintComponent(Graphics g) {
				super.paintComponent(g);
				if(isLeftWrong){
					//Draw a red "X" mark over the panel, if isLeftWrong == true.
		            Graphics2D g2 = (Graphics2D) g;
		            Color  oldColor = g2.getColor();
		            Stroke oldStroke = g2.getStroke();
		            
		            g2.setColor(Color.BLACK);
		            g2.setStroke(new BasicStroke(7));
		            g2.draw(new Line2D.Double(padding, padding, image1Width - padding, image1Height - padding));
		            g2.draw(new Line2D.Double(image1Width - padding, padding, padding, image1Height - padding));
		            
		            g2.setColor(Color.RED);
		            g2.setStroke(new BasicStroke(5));
		            g2.draw(new Line2D.Double(padding, padding, image1Width - padding, image1Height - padding));
		            g2.draw(new Line2D.Double(image1Width - padding, padding, padding, image1Height - padding));
		            
		            g2.setColor(oldColor);
		            g2.setStroke(oldStroke);
		        }
			}
		};
		leftPanel.setName("Left Panel");
		leftPanel.registerObserver(this);
		
		rightPanel = new ContentPane(image2, true, true, false){
			@Override
		    public void paintComponent(Graphics g) {
				super.paintComponent(g);
				if(isRightWrong){
					//Draw a red "X" mark over the panel, if isLeftWrong == true.
		            Graphics2D g2 = (Graphics2D) g;
		            Color  oldColor = g2.getColor();
		            Stroke oldStroke = g2.getStroke();
		            
		            g2.setColor(Color.BLACK);
		            g2.setStroke(new BasicStroke(7));
		            g2.draw(new Line2D.Double(padding, padding, image1Width - padding, image1Height - padding));
		            g2.draw(new Line2D.Double(image1Width - padding, padding, padding, image1Height - padding));
		            
		            g2.setColor(Color.RED);
		            g2.setStroke(new BasicStroke(5));
		            g2.draw(new Line2D.Double(padding, padding, image2Width - padding, image2Height - padding));
		            g2.draw(new Line2D.Double(image2Width - padding, padding, padding, image2Height - padding));
		            
		            g2.setColor(oldColor);
		            g2.setStroke(oldStroke);
		        }
			}
		};
		rightPanel.setName("Right Panel");
		rightPanel.registerObserver(this);
		
		//Find where to put the panels.
		int leftX;
		int rightX;
		int yOrigin;
		
		//Find x-coordinates.
		int windowWidth = mainWindow.getWidth();
		int totalPanelWidth = leftPanel.getWidth() + rightPanel.getWidth();
		//If the total image width is larger than the screen, crop the images to fit the max width.
		if(totalPanelWidth >= windowWidth){
			leftX  = 0;
			rightX = windowWidth / 2;
			leftPanel.setSize (new Dimension((windowWidth / 2), leftPanel.getHeight()));
			rightPanel.setSize(new Dimension((windowWidth / 2), rightPanel.getHeight()));
		}else{
			int spacer = (windowWidth - totalPanelWidth) / 4;
			leftX  = spacer;
			rightX = (spacer * 3) + leftPanel.getWidth();
		}
		
		//Find y-coordinates.
		int topPadding     = 20;
		int verticalHeight = (int)(mainWindow.getHeight() - questionBox.getBounds().getY()) - 20;
		int panelHeight    = (leftPanel.getHeight() > rightPanel.getHeight())
								? leftPanel.getHeight() : rightPanel.getHeight();
		//If the image heights are larger than the available space above the question box,
		// crop the images to the max height.
		if(panelHeight >= verticalHeight){
			yOrigin = (topPadding / 2);
			leftPanel.setSize (new Dimension(leftPanel.getWidth(),  verticalHeight));
			rightPanel.setSize(new Dimension(rightPanel.getWidth(), verticalHeight));
		}else{
			yOrigin = topPadding;
		}
		
		//Add the panels to mainWindow.
		mainWindow.addLayer(leftPanel,  BUTTON_LAYER, leftX,  yOrigin);
		mainWindow.addLayer(rightPanel, BUTTON_LAYER, rightX, yOrigin);
	}
	
	/**
	 * Used to display a question in the Question Box, a left answer panel that is associated with
	 *  QuestionPanelSelect.Answer.LEFT, and a right answer panel that is associated with 
	 *  QuestionPanelSelect.Answer.RIGHT.
	 * @param question A String representing the question to be displayed.
	 * @param questionNumber An int indicating the this question number out of the maxNumQuestions,
	 *  as set by QuestionPanelSelect's constructor.
	 * @param leftImagePath A String representing the left answer panel's background image file path
	 *  from the program's root directory.
	 * @param rightImagePath A String representing the right answer panel's background image file path
	 *  from the program's root directory.
	 * @throws IOException Thrown if either of the specified image files can not be read, or are missing.
	 */
	public void showQuestion(String question, int questionNumber, String leftImagePath, String rightImagePath)
			throws IOException{
		updateCounter(questionNumber);
		removeAnswer();
		removeQuestion();
		question     = "<CENTER>" + question + "</CENTER>";
		questionText = makeQuestionText(question);
		
		//Center the text on the Question Box.
		int boxCenter  = (int)(questionBox.getPreferredSize().getWidth()  / 2);
		int textCenter = (int)(questionText.getPreferredSize().getWidth() / 2);
		int textX      = boxCenter - textCenter;
		
		questionBox.addComponent(questionText, textX, 20);
		
		makePanels(ImageLoader.getBufferedImage(leftImagePath), ImageLoader.getBufferedImage(rightImagePath));
	}
	
	/**
	 * Used to create a JLabel containing the specified text.
	 * @param question A String representing the text to be displayed on the JPanel.
	 * @return A JLabel containing the specified text.
	 */
	private JLabel makeQuestionText(String question){
		int textPadding = 4;
		JLabel label = new JLabel("<HTML><div>" + question + "</div><HTML>", SwingConstants.CENTER);
		Font font;
		try{
			font = FontMaker.getFont(PREFERRED_FONT, 24);
		}catch(IndexOutOfBoundsException | IOException | FontFormatException e){
			font = FontMaker.getDefaultFont(24);
		}
		label.setFont(font);
		label.setForeground(Color.WHITE);
		int textWidth = FontMaker.getStringWidth(label.getFont(), questionBox.getGraphics(), question);
		label.setSize(new Dimension((textWidth + textPadding), questionBox.getHeight()));
		return label;
	}
	
	/**
	 * Used to show a short answer and icon along the bottom-left of the question box. Also draws a
	 *  red "X" through the panel that is incorrect. Lastly, changes the "Submit" button into a
	 *  "Next" button.
	 * If isCorrect == true, then a green "check" icon will be displayed to the left of "answer" &
	 *  the text indicated by "answer" will be colored green. If isCorrect == false, a red "X" icon
	 *  will appear to the left of "answer" & the text indicated by "answer" will be colored red. 
	 * NOTE: removeAnswer() is called automatically when a new question is displayed.
	 * @param answer A String indicating the text to be displayed as an answer dialog. This text
	 *  should be short, as it is displayed on the bottom of the question box.
	 * @param correctPanel A QuestionPanelSelect.Answer object indicating which answer panel is the
	 *  correct answer. The panel that is not correctPanel will have a red "X" drawn through it.
	 * @param isCorrect A boolean indicating true if the question was answered correctly; false otherwise.
	 * @throws IOException Thrown if the icon image can not be read.
	 */
	public void showAnswer(String answer, Answer correctPanel, boolean isCorrect) throws IOException{
		showNextButton();
		showAnswerIcon(isCorrect);
		showAnswerNote(answer, isCorrect);
		isLeftWrong  = (correctPanel == Answer.RIGHT) ? true : false;
		isRightWrong = (correctPanel == Answer.LEFT)  ? true : false;
		
		//Unlikely, but this is added just in case...
		if(correctPanel == Answer.NONE){
			isLeftWrong  = true;
			isRightWrong = true;
		}
		
		mainWindow.reDraw();
	}
	
	/**
	 * Used to set answerIcon & display the correct/incorrect icon in the bottom-left of questionBox.
	 * @param correct A boolean indicating true if the question was answered correctly; false otherwise.
	 * @throws IOException Thrown if the icon's image can not be read.
	 */
	private void showAnswerIcon(boolean correct) throws IOException{
		if(correct)
			answerIcon = new ContentPane(ImageLoader.getBufferedImage(CORRECT_ICON_PATH), false, false);
		else
			answerIcon = new ContentPane(ImageLoader.getBufferedImage(WRONG_ICON_PATH), false, false);
		
		int padding = 10;
		int xOrigin = padding;
		int yOrigin = questionBox.getHeight() - answerIcon.getHeight() - padding;
		
		questionBox.addComponent(answerIcon, xOrigin, yOrigin);
	}
	
	/**
	 * Used to show a short note to the right of an existing answerIcon. If an answerIcon does not exist,
	 *  then the text indicated by "note" is simply displayed in questionBox's bottom-left.
	 * If isCorrect == true, then the text indicated by "note" will be colored green. If
	 *  isCorrect == false, the text indicated by "note" will be colored red. 
	 * @param note A String indicating the text to be displayed to the right of a answerIcon.
	 *  This text should be short, as it is displayed on the bottom of the question box.
	 * @param isCorrect A boolean indicating true if the question was answered correctly; false otherwise.
	 */
	private void showAnswerNote(String note, boolean isCorrect){
		if(note != null){
			Font font;
			try {
				font = FontMaker.getFont(PREFERRED_FONT, 24);
			} catch (IndexOutOfBoundsException | IOException | FontFormatException e) {
				font = FontMaker.getDefaultFont(24);
			}
		
			JLabel answerLabel = new JLabel("<HTML>" + note + "</HTML>");
			answerLabel.setFont(font);
			
			//Change the default text color, depending on if the user was correct or not.
			if(isCorrect)
				answerLabel.setForeground(Color.GREEN);
			else
				answerLabel.setForeground(Color.RED);
			answerNote = new ContentPane(answerLabel.getPreferredSize().width,
							answerLabel.getPreferredSize().height, false, false);
			
			//Find the coordinates for answerNote.
			int xOrigin;
			int yOrigin;
			int padding = 10;
			if(answerIcon != null){
				xOrigin = answerIcon.getX() + answerIcon.getWidth() + padding;
			
				int baseLine = answerIcon.getY() + answerIcon.getHeight();
				yOrigin  = baseLine - answerLabel.getPreferredSize().height - padding;
			}else{
				xOrigin = padding;
				yOrigin = questionBox.getHeight() - answerLabel.getPreferredSize().height - padding;
			}
			
			//Add answerNote to questionBox.
			answerNote.addComponent(answerLabel, 0, 0);
			questionBox.addComponent(answerNote, xOrigin, yOrigin);
		}
	}
	
	/**
	 * Helper method used to hide the "Submit" button, and display the "Next" button.
	 */
	private void showNextButton(){
		if(nextButton != null){
			hideSubmitBtn();
			mainWindow.addLayer(nextButton, BUTTON_LAYER, submitBtnX, submitBtnY);
		}
	}
	
	/**
	 * Helper method used to hide the "Next" button, and display the "Submit" button.
	 */
	private void hideNextButton(){
		if(nextButton != null){
			mainWindow.getContainer().remove(nextButton);
			showSubmitBtn();
		}
	}
	
	/**
	 * Helper method called by hideNextButton() to display the "Submit" button.
	 */
	private void showSubmitBtn(){
		mainWindow.addLayer(submitBtn, BUTTON_LAYER, submitBtnX, submitBtnY);
	}
	
	/**
	 * Helper method called by showNextButton() to hide the "Submit" button.
	 */
	private void hideSubmitBtn(){
		mainWindow.getContainer().remove(submitBtn);
	}
	
	/**
	 * Used to remove the questionText JPanel from questionBox. 
	 */
	private void removeQuestion(){
		removePanels();
		if(questionText != null){
			questionBox.remove(questionText);
			questionBox.reDraw();
		}
	}
	
	/**
	 * Used to remove leftPanel and rightPanel from mainWindow.
	 */
	private void removePanels(){
		if(leftPanel != null){
			leftPanel.removeObserver(this);
			mainWindow.getContainer().remove(leftPanel);
			leftPanel = null;
		}
		if(rightPanel != null){
			rightPanel.removeObserver(this);
			mainWindow.getContainer().remove(rightPanel);
			rightPanel = null;
		}
	}
	
	/**
	 * Used to remove the answer icon and text(if text exists) from the question box.
	 *  Also removes any "X" marks from the panels. Finally, changes the "Next" button
	 *  back into a "Submit" button.
	 * NOTE: Called automatically when a new question is displayed.
	 */
	public void removeAnswer(){
		hideNextButton();
		if(answerIcon != null)
			questionBox.remove(answerIcon);
		if(answerNote != null)
			questionBox.remove(answerNote);
		
		answerIcon = null;
		answerNote = null;
		
		isLeftWrong  = false;
		isRightWrong = false;
		
		mainWindow.reDraw();
	}
	
	/**
	 * Used to set isRightSelected and isLeftSelected to false, after the "Submit" button is clicked.
	 */
	private void resetSelections(){
		if(isRightSelected){
			rightPanel.toggleSelection();
			isRightSelected = false;
		}
		if(isLeftSelected){
			leftPanel.toggleSelection();
			isLeftSelected = false;
		}
	}
	
	/**
	 * Used to update questionCounter's text to reflect "currentQuestionNumber / maxNumQuestions".
	 * @param currentQuestionNumber An int indicating this question's number out of the maxNumQuestions
	 *  specified by this QuestionPanelSelect's constructor.
	 */
	private void updateCounter(int currentQuestionNumber){
		String labelText = "Question: " + currentQuestionNumber + " / " + maxNumQuestions;
		JLabel label = new JLabel("<HTML><div align=\"right\">" + labelText + "</div><HTML>");
		label.setForeground(Color.BLACK);
		label.setFont(counterFont);
		label.setAlignmentX(Component.RIGHT_ALIGNMENT);
		int xOrigin = ((questionCounter.getSize().width - label.getPreferredSize().width) / 2);
		int yOrigin = ((questionCounter.getSize().height - label.getPreferredSize().height) / 2);
		questionCounter.removeAll();
		questionCounter.addComponent(label, xOrigin, yOrigin);
		questionCounter.reDraw();
	}
	
	/**
	 * Used to remove all components that were created by this QuestionPanelSelect from mainWindow.
	 * WARNING: This method renders this instance of QuestionPanelSelect inoperable. All references
	 *  to this instance should be removed for garbage collection. If another QuestionPanelSelect
	 *  screen is needed, a new instance should be created.
	 */
	public void tearDown(){
		homeBtn.removeObserver(this);
		submitBtn.removeObserver(this);
		if(leftPanel != null)
			leftPanel.removeObserver(this);
		if(rightPanel != null)
			rightPanel.removeObserver(this);
		if(nextButton != null){
			nextButton.removeObserver(this);
			mainWindow.getContainer().remove(nextButton);
			nextButton = null;
		}
		
		removeAnswer();
		
		questionBox.remove(questionText);
		
		mainWindow.getContainer().remove(questionCounter);
		mainWindow.getContainer().remove(questionBox);
		mainWindow.getContainer().remove(leftPanel);
		mainWindow.getContainer().remove(rightPanel);
		mainWindow.getContainer().remove(submitBtn);
		mainWindow.getContainer().remove(homeBtn);
		
		questionCounter = null;
		questionText    = null;
		questionBox     = null;
		leftPanel       = null;
		rightPanel      = null;
		submitBtn       = null;
		homeBtn         = null;
	}
	
	/**
	 * This enum is used to define constants that are used to represent a user's answer.
	 * @author Kenneth Chin
	 */
	public enum Answer{
		/**
		 * The left answer panel.
		 */
		LEFT ("left"),
		/**
		 * The right answer panel.
		 */
		RIGHT("right"),
		/**
		 * No panel was selected.
		 */
		NONE ("none");
		
		private String value;
		
		/**
		 * The private constructor for the Answer enum.
		 * @param value
		 */
		private Answer(String value){
			this.value = value;
		}
		
		/**
		 * Used to obtain the String that represents a user's answer.
		 * @return The String that represents a user's answer.
		 */
		public String getStringValue(){
			return value;
		}
	}
	
	@Override
	public void clicked(JComponent component) {
		ContentPane pane = (ContentPane)component;
		//If the left answer panel is selected, set it as selected & ensure the right answer panel
		// is no longer selected.
		if(pane == leftPanel){
			isLeftSelected = pane.isSelected();
			if(isRightSelected && pane.isSelected()){
				rightPanel.toggleSelection();
				isRightSelected = false;
			}
		//If the right answer panel is selected, set it as selected & ensure the left answer panel
		// is no longer selected.
		}else if(pane == rightPanel){
			isRightSelected = pane.isSelected();
			if(isLeftSelected && pane.isSelected()){
				leftPanel.toggleSelection();
				isLeftSelected = false;
			}
		//The "Submit" button was clicked. Notify all observers of the user's answer.
		}else if(pane == submitBtn){
			notifiyObserver(ANSWER);
		//The "Next" button was clicked. Notify all observers.
		}else if(pane == nextButton){
			notifiyObserver(NEXT);
		//The "Home" button was clicked. Tear down the QuestionPanelSelect and create a WelcomeScreen.
		}else if(pane == homeBtn){
			notifiyObserver(HOME);
		}else
			throw new IllegalStateException("QuestionPanelSelect heard a clicked event, but didn't do anything!");
	}

	@Override
	public void registerObserver(TestableObserver obs){
		observers.add(obs);
		
	}

	@Override
	public void removeObserver(TestableObserver obs){
		int index = observers.indexOf(obs);
		if(index >= 0)
			observers.remove(index);
	}

	@Override
	public void notifiyObserver(String event){
		if(event.equals(HOME)){
			for(TestableObserver obs:observers)
				obs.homeClicked();
		}else if(event.equals(NEXT)){
			resetSelections();
			for(TestableObserver obs:observers)
				obs.nextClicked();
		}else if(event.equals(ANSWER)){
			String answer;
			if(isLeftSelected)
				answer = Answer.LEFT.getStringValue();
			else if(isRightSelected)
				answer = Answer.RIGHT.getStringValue();
			else if(!isLeftSelected && !isRightSelected)
				answer = Answer.NONE.getStringValue();
			else
				throw new IllegalStateException("QuestionPanelSelect reported an answer of both options selected!");
		
			//Set both answer panels to un-selected & notify all observers of the user's answer.
			resetSelections();
			for(TestableObserver obs:observers)
				obs.answered(answer);
		}else
			throw new IllegalStateException("QuestionPanelSelect received an event, but didn't do anything!");
	}
}
