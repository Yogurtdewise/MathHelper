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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.View;

import project.buttons.HomeButtonMaker;
import project.constants.Operator;
import project.interfaces.ClickableObserver;
import project.interfaces.Testable;
import project.interfaces.TestableObserver;

/**
 * This class is used to ask a user a question that requires a text answer. It creates and displays
 *  a JTextField used to obtain a user's answer, a Question Box used to display a question, a "Submit"
 *  button, and a "Home" button. If the user clicks the "Submit" button and the user's answer is
 *  "" (the empty String), a message is printed into the answer JTextField stating that they didn't enter
 *  an answer. If the "Submit" button is clicked with any text other than "", all registered
 *  TestableObservers will have their answered(String) method called. The String passed into the
 *  answered(String) method will be the exact String that was in the answer JTextField. After all
 *  TestableObservers are notified, the answer JTextField is cleared of all text. If the user clicks the
 *  "Home" button, all registered TestableObservers will have their homeClicked() method called. It is up
 *  to the individual TestableObservers to implement an action to take on the homeClicked() event. The
 *  tearDown() method should be used to remove this QuestionPanelText from mainWindow.
 *  
 * @author Kenneth Chin
 */
public class QuestionPanelText implements Testable, ClickableObserver{

	//Constants used to define answerBox.
	private static final int     ANSWER_BOX_SIZE  = 15;
	private static final Integer ANSWER_BOX_LAYER = new Integer(4);
	
	//Constants used to define questionBox.
	private static final String  QUESTION_BOX_NAME       = "QuestionBox";
	private static final String  QUESTION_BOX_IMAGE_PATH = "\\images\\test\\QuestionBox.png";
	private static final Integer TEXT_LAYER              = new Integer(2);
	
	//The preferred font for answerBox and questionBox.
	private static final int     PREFERRED_FONT           = FontMaker.ARIAL;
	
	//Constants used to create the "Submit" button.
	private static final String  SUBMIT_BTN_NAME       = "Submit";
	private static final String  SUBMIT_BTN_IMAGE_PATH = "\\images\\test\\SubmitBtn.png";
	private static final Integer BUTTON_LAYER          = new Integer(3);
	
	//The file path for the background image from the program's root directory.
	private static final String BACKGROUND_IMAGE_PATH = "\\images\\test\\background_plain.png";
	
	//The file path for the correct/incorrect images from the program's root directory.
	private static final String CORRECT_ICON_PATH = "\\images\\test\\Correct.png";
	private static final String WRONG_ICON_PATH   = "\\images\\test\\Wrong.png";
	
	private static final String  ANSWER = "answer"; //Used to identify a user answer event.
	private static final String  HOME   = "home";   //Used to identify a "Home" button event.
	private static final String  NEXT   = "next";   //Used to identify a "Next" button event.
	
	//An array of all registered TestableObservers.
	private CopyOnWriteArrayList<TestableObserver> observers = new CopyOnWriteArrayList<TestableObserver>();
	
	private String currentAnswer = "";  //The text in answerBox when the user clicks the "Submit" button.
	private int maxNumQuestions  = 0;   //The maximum number of questions to be displayed in questionCounter.
	
	private boolean isEquation = false; //Used to determine if the current question is an equation.
	private int lineXStart = 0;         //The starting x-origin of the equation line.
	private int lineXEnd   = 0;         //The ending x-coordinate of the equation line.
	private int lineY      = 0;         //The y-coordinate of the equation line.
	
	private int submitBtnX; //Used to identify the X origin for submitBtn & nextButton.
	private int submitBtnY; //Used to identify the Y origin for submitBtn & nextButton.
	
	private ContentPane nextButton = null;   //The ContentPane used to display the practice test's "Next" button.
	private ContentPane answerText = null;   //The ContentPane used to display an equation question's answer.
	private ContentPane answerNote = null;   //The ContentPane used to display a question's answer or answer's note.
	private ContentPane answerIcon = null;   //The ContentPane used to display the icon indicating correct/incorrect.
	private JTextField  answerBox;           //The JTextField where a user enters their text answer.
	private ContentPane answerBoxContainer;  //The ContentPane used as a container for answerBox.
	private ContentPane questionBox;         //The ContentPane used to display a question.
	private ContentPane questionCounter;     //The ContentPane used to display the number of remaining questions.
	private JLabel      questionText = null; //The JLabel used to display a question in questionBox.
	private JLabel      equationText = null; //The JLabel used to display an equation in questionBox.
	private ContentPane submitBtn;           //The "Submit" button.
	private ContentPane homeBtn;             //The "Home" button.
	
	private Font counterFont;                //The Font used for questionCounter's text.
	
	private MainWindow mainWindow; //The MainWindow that is to have QuestionPanelText components added to.
	
	/**
	 * Creates and displays a QuestionPanelText with no question displayed. Use showQuestion(String) or
	 * showEquation(String, int, int, Operator) to display a question.
	 * @param mainWindow The MainWindow that is to have QuestionPanelText components added to.
	 * @throws IOException Thrown if any image file can not be read or is missing.
	 */
	public QuestionPanelText(MainWindow mainWindow, int maxNumQuestions) throws IOException{
		this.mainWindow      = mainWindow;
		this.maxNumQuestions = maxNumQuestions;
		init();
	}
	
	/**
	 * Used to initialize and display the QuestionPanelText components.
	 * @throws IOException Thrown if any image file can not be read or is missing.
	 */
	private void init() throws IOException{
		initBackground();
		initAnswerBox();
		initQuestionBox();
		initCounter();
		initSubmitBtn();
		initNextBtn();
		initHomeBtn();
	}
	
	/**
	 * Used to set the background image.
	 * @throws IOException Thrown if the background image file can not be read or is missing.
	 */
	private void initBackground() throws IOException{
		BufferedImage backgroundImage = ImageLoader.getBufferedImage(BACKGROUND_IMAGE_PATH);
		mainWindow.setBackgroundImage(backgroundImage);
	}
	
	/**
	 * Used to create the JTextField that is used to obtain user answers.
	 */
	private void initAnswerBox(){
		answerBox = new JTextField("", ANSWER_BOX_SIZE);
		answerBox.setBackground(Color.WHITE);
		answerBox.setFont(FontMaker.getDefaultFont(32));
		answerBox.setOpaque(true);
		
		//answerBox had to be placed into a container, because its background color was falling
		// through to mainWindow's background layer(answerBox was transparent).
		answerBoxContainer = new ContentPane(answerBox.getPreferredSize().width,
								answerBox.getPreferredSize().height, false, false);
		answerBoxContainer.addComponent(answerBox, 0, 0);
		answerBoxContainer.setBackground(Color.WHITE);
		answerBoxContainer.setOpaque(false);
		
		//Find mainWindow's center and answerBox's offset.
		int windowCenter    = (int)(mainWindow.getPreferredSize().getWidth() / 2);
		int answerBoxCenter = (int)(answerBox.getPreferredSize().getWidth()  / 2);
		int xOrigin         = windowCenter - answerBoxCenter;
		
		mainWindow.addLayer(answerBoxContainer, ANSWER_BOX_LAYER, xOrigin, 200);
	}
	
	/**
	 * Used to create the ContentPane that is used as a container for question text.
	 * If the question is an equation, an equals line is drawn beneath the equation.
	 * @throws IOException Thrown if the questionBox's image file can not be read, or is missing.
	 */
	private void initQuestionBox() throws IOException{
		BufferedImage questionBoxImage = ImageLoader.getBufferedImage(QUESTION_BOX_IMAGE_PATH);
		questionBox = new ContentPane(questionBoxImage, false, false){
			@Override
		    public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				//Paint the equation line, if the current question is an equation.
		        if(isEquation) {
		            Graphics2D g2 = (Graphics2D) g;
		            Color  oldColor = g2.getColor();
		            Stroke oldStroke = g2.getStroke();
		            
		            g2.setColor(Color.WHITE);
		            g2.setStroke(new BasicStroke(3));
		            g2.draw(new Line2D.Double(lineXStart, lineY, lineXEnd, lineY));
		            
		            g2.setColor(oldColor);
		            g2.setStroke(oldStroke);
		        }
		    }
		};
		questionBox.setName(QUESTION_BOX_NAME);
		mainWindow.addLayer(questionBox, TEXT_LAYER, 80, 300);
	}
	
	/**
	 * Used to create the "Submit" button.
	 * @throws IOException Thrown if the "Submit" button's image file can not be read, or is missing.
	 */
	private void initSubmitBtn() throws IOException{
		BufferedImage submitBtnImage = ImageLoader.getBufferedImage(SUBMIT_BTN_IMAGE_PATH);
		submitBtn = new ContentPane(submitBtnImage, true, false);
		submitBtn.setName(SUBMIT_BTN_NAME);
		
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
	 * Used to create the "Next" button, which is not displayed on initialization.
	 * @throws IOException Thrown if the "Next" button image file can not be read.
	 */
	private void initNextBtn() throws IOException{
		nextButton = new ContentPane(ImageLoader.getBufferedImage("\\images\\test\\Next.png"), true, false);
		nextButton.registerObserver(this);
	}
	
	/**
	 * Used to create the "Home" button.
	 * @throws IOException Thrown if the "Home" button's image file can not be read, or is missing.
	 */
	private void initHomeBtn() throws IOException{
		homeBtn = HomeButtonMaker.getContentPane();
		mainWindow.addLayer(homeBtn, BUTTON_LAYER, HomeButtonMaker.getXDefault(), HomeButtonMaker.getYDefault());
		homeBtn.registerObserver(this);
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
	 * Used to display a text question.
	 * NOTE: removeAnswer() is called automatically when a new question is displayed.
	 * @param question A String representing the question to be asked.
	 * @param questionNumber An int indicating the this question number out of the maxNumQuestions,
	 *  as set by QuestionPanelText's constructor.
	 */
	public void showQuestion(String question, int questionNumber){
		isEquation  = false;
		removeAnswer();
		removeQuestion();
		removeEquationText();
		updateCounter(questionNumber);
		question     = "<CENTER>" + question + "</CENTER>";
		questionText = makeQuestionText(question);
		
		//Center the question text in questionBox.
		int boxCenter  = (int)(questionBox.getPreferredSize().getWidth()  / 2);
		int textCenter = (int)(questionText.getPreferredSize().getWidth() / 2);
		int textX      = boxCenter - textCenter;
		
		questionBox.addComponent(questionText, textX, 20);
	}
	
	/**
	 * Used to display a question that is an equation. The specified question will be displayed to the
	 *  left of the equation. Due to space limitations, this question should be short, and no more than
	 *  three lines long. If the equation is Operator.ADD, Operator.SUBTRACT, or Operator.MULTIPLY,
	 *  the firstOperand is right-aligned on the second line below the question text. Additionally
	 *  the secondOperand is right-aligned below the firstOperand, with the Operator's getSymbol()
	 *  occupying the third space to the left of secondOperand. The equation is centered in the
	 *  Question Box, and an equals line is drawn below secondOperand.
	 *  NOTE: removeAnswer() is called automatically when a new question is displayed.
	 *  NOTE: TODO Operator.DIVIDE is not supported.
	 * @param question A String representing the question text.
	 * @param questionNumber An int indicating the this question number out of the maxNumQuestions,
	 *  as set by QuestionPanelText's constructor.
	 * @param firstOperand An int representing the first (top) operand.
	 * @param secondOperand An int representing the second (bottom) operand.
	 * @param operator An Operator indicating the arithmetic operation to be performed.
	 *  NOTE: Operator.DIVIDE is not supported.
	 */
	public void showEquation(String question, int questionNumber, int firstOperand, int secondOperand, Operator operator){
		int topPadding = 20;
		removeAnswer();
		removeQuestion();
		removeEquationText();
		updateCounter(questionNumber);
		questionText = makeQuestionText(question);
		
		//TODO if(!(operator == Operator.DIVIDE)){
		
		equationText = makeVerticalEquation(firstOperand, secondOperand, operator);
		
		//Find the questionBox's center and offset for the question and equation.
		int boxCenter      = (int)(questionBox.getPreferredSize().getWidth()  / 2);
		int equationCenter = (int)(equationText.getPreferredSize().getWidth() / 2);
		int equationX      = boxCenter - equationCenter;
		
		int questionCenter = (int)(questionText.getPreferredSize().getWidth() / 2);
		int questionX      = boxCenter - questionCenter - (2*(int)(equationText.getPreferredSize().getWidth()));
		//Prevent the question text from leaving the left box border (with padding).
		if(questionX < 3)
			questionX = 2;

		int equationY = (getQustionHeight(questionText.getWidth(), questionText.getHeight()) + (2*topPadding));
		
		questionBox.addComponent(questionText, questionX, topPadding);
		questionBox.addComponent(equationText, equationX, equationY);
		
		//Set the equation line coordinates.
		lineY = equationY + equationText.getHeight() + 2;
		lineXStart = equationX;
		lineXEnd   = equationText.getWidth() + equationX;
		
		isEquation = true;
	}
	
	/**
	 * Used to show a short answer and icon along the bottom-left of the question box. Also displays
	 *  the "Next" button and hides the "Submit" button.This method is meant to be used with
	 *  questions that were displayed using showQuesion(String,int).
	 * If isCorrect == true, then a green "check" icon will be displayed to the left of "answer" &
	 *  the text indicated by "answer" will be colored green. If isCorrect == false, a red "X" icon
	 *  will appear to the left of "answer" & the text indicated by "answer" will be colored red. 
	 * NOTE: removeAnswer() is called automatically when a new question is displayed.
	 * @param answer A String indicating the text to be displayed as an answer dialog. This text
	 *  should be short, as it is displayed on the bottom of the question box.
	 * @param isCorrect A boolean indicating true if the question was answered correctly; false otherwise.
	 * @throws IOException Thrown if the icon image can not be read.
	 */
	public void showAnswer(String answer, boolean isCorrect) throws IOException{
		showNextButton();
		showAnswerIcon(isCorrect);
		showAnswerNote(answer, isCorrect);
	}
	
	/**
	 * Used to show a numeric answer beneath a question's vertical equation, and an icon along
	 *  the bottom-left of the question box. Also displays the "Next" button and hides the "Submit"
	 *  button. This method is meant to be used with questions that were displayed using
	 *  showEquation(String, int, int, int, Operator).
	 * If isCorrect == true, then a green "check" icon will be displayed & the text indicated by
	 *  "answer" will be colored green. If isCorrect == false, a red "X" icon will be displayed
	 *  & the text indicated by "answer" will be colored red. 
	 * NOTE: removeAnswer() is called automatically when a new question is displayed.
	 * @param answer A String indicating the numeric answer to be displayed beneath the question's
	 *  vertical equation.
	 * @param note A String representing a short message that will be displayed to the right of the
	 *  correct/incorrect icon.
	 * @param isCorrect A boolean indicating true if the user's answer was correct; false otherwise.
	 * @throws IOException Thrown if the icon's image could not be read.
	 */
	public void showEquationAnswer(String answer, String note, boolean isCorrect) throws IOException{
		showNextButton();
		showAnswerIcon(isCorrect);
		if(note != null)
			showAnswerNote(note, isCorrect);
		
		if(answer != null){
			Font font;
			try {
				font = FontMaker.getFont(PREFERRED_FONT, 24);
			} catch (IndexOutOfBoundsException | IOException | FontFormatException e) {
				font = FontMaker.getDefaultFont(24);
			}
			
			JLabel answerLabel = new JLabel("<HTML>" + answer + "</HTML>");
			answerLabel.setFont(font);
			//Set the default text color depending on if the user was correct or not.
			if(isCorrect)
				answerLabel.setForeground(Color.GREEN);
			else
				answerLabel.setForeground(Color.RED);
			
			//Find answerText's coordinates.
			int textWidth  = answerLabel.getPreferredSize().width;
			int textHeight = answerLabel.getPreferredSize().height;
			
			answerText = new ContentPane(textWidth, textHeight, false, false);
			
			//If answerText wider than questionText, offset is negative; moves left.
			//If questionText wider than answerText, offset is positive; moves right.
			int answerXOffset = equationText.getWidth() - textWidth;
			int answerXOrigin = equationText.getX() + answerXOffset;
			
			int yPadding = 5;
			int answerYOrigin = lineY + yPadding;
			
			answerText.addComponent(answerLabel, 0, 0);
			questionBox.addComponent(answerText, answerXOrigin, answerYOrigin);
		}
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
			//Set the default text color depending on if the user was correct or not.
			if(isCorrect)
				answerLabel.setForeground(Color.GREEN);
			else
				answerLabel.setForeground(Color.RED);
			answerNote = new ContentPane(answerLabel.getPreferredSize().width,
							answerLabel.getPreferredSize().height, false, false);
			
			//Find answerText's coordinates.
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
					
			answerNote.addComponent(answerLabel, 0, 0);
			questionBox.addComponent(answerNote, xOrigin, yOrigin);
		}
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
	 * Used to update questionCounter's text to reflect "currentQuestionNumber / maxNumQuestions".
	 * @param currentQuestionNumber An int indicating this question's number out of the maxNumQuestions
	 *  specified by this QuestionPanelText's constructor.
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
	
	//This method is an adaptation of the answer from "Laurent K" at:
	//  http://stackoverflow.com/questions/1048224/
	//  get-height-of-multi-line-text-with-fixed-width-to-make-dialog-resize-properly
	/**
	 * Used to determine the pixel height of the question text, regardless of how many lines
	 *  the text occupies.
	 * @param width An int indicating the JPanel's width.
	 * @param height An int indicating the JPanel's height.
	 * @return An int indicating the total text height of the specified JPanel.
	 */
	private int getQustionHeight(int width, int height){
		View view = (View) questionText.getClientProperty(javax.swing.plaf.basic.BasicHTML.propertyKey);
    	view.setSize(width, height);
    	float h = view.getPreferredSpan(View.Y_AXIS);
    	return (int)Math.ceil(h);
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
	 * Used to remove questionText from questionBox.
	 */
	private void removeQuestion(){
		if(questionText != null){
			questionBox.remove(questionText);
			questionBox.reDraw();
		}
	}
	
	/**
	 * Used to remove equationText from questionBox.
	 */
	private void removeEquationText(){
		if(equationText != null){
			questionBox.remove(equationText);
			questionBox.reDraw();
		}
	}
	
	/**
	 * Used to remove the answer icon and text(if text exists) from the question box.
	 *  Also hides the "Next" button and displays the "Submit" button.
	 * NOTE: Called automatically when a new question is displayed.
	 */
	public void removeAnswer(){
		hideNextButton();
		if(answerText != null)
			questionBox.remove(answerText);
		if(answerIcon != null)
			questionBox.remove(answerIcon);
		if(answerNote != null)
			questionBox.remove(answerNote);
		
		answerText = null;
		answerIcon = null;
		answerNote = null;
		
		questionBox.reDraw();
	}
	
	/**
	 * Used to create a JPanel with the specified text.
	 * @param question A String representing the text to be displayed.
	 * @return A JPanel with the specified text.
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
	 * Used to create a JPanel with a vertical equation as text. The text is right-aligned, with
	 *  secondOperand directly below firstOperand. The Operator getSymbol() String occupies the
	 *  third space to the left of secondOperand.
	 * @param firstOperand An int representing the first (top) operand.
	 * @param secondOperand An int representing the second (bottom) operand).
	 * @param operator An Operator that is ADD, SUBTRACT, or MULTIPLY and indicates the operation
	 *  that should be performed on the operands.
	 * @return A JPanel with the specified vertical equation as text.
	 */
	private JLabel makeVerticalEquation(int firstOperand, int secondOperand, Operator operator){
		String symbol = operator.getSymbol();
		String question = firstOperand + "<br>" + symbol + "&nbsp;&nbsp;" + secondOperand;
		int textPadding = 4;
		JLabel label = new JLabel("<HTML><div align=\"right\">" + question + "</div><HTML>", SwingConstants.CENTER);
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
	 * Used to remove all components that were created by this QuestionPanelText from mainWindow.
	 * WARNING: This method renders this instance of QuestionPanelText inoperable. All references
	 *  to this instance should be removed for garbage collection. If another QuestionPanelText
	 *  screen is needed, a new instance should be created.
	 */
	public void tearDown(){
		removeAnswer();
		if(nextButton != null){
			nextButton.removeObserver(this);
			nextButton = null;
		}
		
		submitBtn.removeObserver(this);
		homeBtn.removeObserver(this);
		mainWindow.getContainer().remove(answerBoxContainer);
		mainWindow.getContainer().remove(questionCounter);
		mainWindow.getContainer().remove(questionBox);
		mainWindow.getContainer().remove(submitBtn);
		mainWindow.getContainer().remove(homeBtn);
		
		questionText = null;
		equationText = null;
		answerBox    = null;
		
		answerBoxContainer = null;
		questionCounter    = null;
		questionBox        = null;
		submitBtn          = null;
		homeBtn            = null;
	}
	
	@Override
	public void clicked(JComponent component) {
		if(component == submitBtn){
			String answer = answerBox.getText();
			if(answer == null || answer.equals("")){
				answerBox.setText("You didn't write an answer.");
			}else{
				currentAnswer = answer;
				answerBox.setText("");
				notifiyObserver(ANSWER);
			}
		}else if(component == nextButton){
			notifiyObserver(NEXT);
		}else if(component == homeBtn){
			notifiyObserver(HOME);
		}else
			throw new IllegalStateException("QuestionPanelText heard a clicked event, but didn't do anything!");
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
			for(TestableObserver obs:observers)
				obs.nextClicked();
		}else if(event.equals(ANSWER)){
			for(TestableObserver obs:observers)
				obs.answered(currentAnswer);
		}else
			throw new IllegalStateException("QuestionPanelText received an event, but didn't do anything!");
	}
}
