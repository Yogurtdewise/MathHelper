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
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import project.buttons.HomeButtonMaker;
import project.database.ModuleReportSummary;
import project.interfaces.ClickableObserver;
import project.interfaces.EnumerableButtonFactory;
import project.interfaces.ModuleSelectButtonInterface;
import project.run.GUIManager;
import project.tools.ContentPane;
import project.tools.FontMaker;
import project.tools.ImageLoader;
import project.tools.MainWindow;

/**
 * This class is used to display a table of student grades for each Test Module and each DifficultyLevel
 *  of a Test Module. It also grants easy access to Test Module details and Rewards via a "See Details"
 *  and "View Rewards" buttons, respectively. Lastly, a user may return to the "Welcome Screen" by clicking
 *  a "Home" button.
 * @author Kenneth Chin
 */
public class ReportCardScreen implements ClickableObserver{
	
	//Layer constants for ReportCardScreen components.
	private static final int BUTTON_LAYER = 3;
	private static final int TEXT_LAYER   = 2;
	
	//Image file paths from the program's root directory for ReportCardScreen components.
	private static final String detailFilePath     = "\\images\\report\\SeeDetailsBtn.png";
	private static final String rewardFilePath     = "\\images\\report\\RewardsBtn.png";
	private static final String lineFilePath       = "\\images\\report\\Line.png";
	private static final String backgroundFilePath = "\\images\\report\\Background.png";
	
	//The preferred font.
	private static final int PREFERRED_FONT = FontMaker.CHALK;
	
	//The EnumerableButtonFactory that defines all tests who's grades are to be displayed.
	private EnumerableButtonFactory buttons;
	//A HashMap with a grade mapping to each ModuleSelectButtonInterface in buttons.
	private HashMap<ModuleSelectButtonInterface, ModuleReportSummary> reportMap;
	
	private ArrayList<JLabel> reportTable; //An array of all text elements in the ReportCardScreen table.
	private ContentPane detailsBtn;        //The "See Details" button.
	private ContentPane rewardBtn;         //The "View Rewards" button.
	private ContentPane homeBtn;           //The "Home" button.
	
	private ContentPane textArea;          //The ContentPane that is used as a container for reportTable elements.
	
	private ArrayList<ContentPane> lines;  //An array of lines that separate each row in the ReportCardScreen table.
	private BufferedImage line;            //A BufferedImage of a line separator.
	
	private GUIManager manager;    //The GUIManager that manages mainWindow & all GUI screens.
	private MainWindow mainWindow; //The MainWindow that will have ReportCardScreen components added to.
	
	/**
	 * Creates and displays a ReportCardScreen.
	 * @param manager The GUIManager that manages the primary MainWindow & all GUI screens.
	 * @param buttons An EnumerableButtonFactory that defines each ModuleSelectButtonInterface
	 *  in reportMap.
	 * @param reportMap A HashMap of each ModuleSelectButtonInterface in buttons, who's values
	 *  are ModuleReportSummary that define a student's grade for the Test module specified by
	 *  the ModuleSelectButtonInterface key.
	 * @throws IOException Thrown if any image file can not be read, or is missing.
	 */
	public ReportCardScreen(GUIManager manager, EnumerableButtonFactory buttons,
			HashMap<ModuleSelectButtonInterface, ModuleReportSummary> reportMap) throws IOException{
		
		this.manager    = manager;
		this.mainWindow = manager.getMainWindow();
		this.buttons    = buttons;
		this.reportMap  = reportMap;
		
		init();
		makeTable();
	}
	
	/**
	 * Used to initialize ReportCardScreen components.
	 * @throws IOException Thrown if any image file can not be read, or is missing.
	 */
	private void init() throws IOException{
		reportTable = new ArrayList<JLabel>();
		lines       = new ArrayList<ContentPane>();
		line        = ImageLoader.getBufferedImage(lineFilePath);
		textArea    = new ContentPane(mainWindow.getWidth(), mainWindow.getHeight() - 150, false, false);
		mainWindow.setBackgroundImage(ImageLoader.getBufferedImage(backgroundFilePath));
		initDetailsButton();
		initRewardsButton();
		initHomeButton();
	}
	
	/**
	 * Used to create and display the "See Details" button.
	 * @throws IOException Thrown if the "See Details" button image file can not be read, or is missing.
	 */
	private void initDetailsButton() throws IOException{
		detailsBtn = makeButton(detailFilePath, "See Details");
		detailsBtn.registerObserver(this);
		mainWindow.addLayer(detailsBtn, BUTTON_LAYER, 260, 575);
	}
	
	/**
	 * Used to create and display the "View Rewards" button.
	 * @throws IOException Thrown if the "View Rewards" button image file can not be read, or is missing.
	 */
	private void initRewardsButton() throws IOException{
		rewardBtn = makeButton(rewardFilePath, "View Rewards");
		rewardBtn.registerObserver(this);
		mainWindow.addLayer(rewardBtn, BUTTON_LAYER, 660, 575);
	}
	
	/**
	 * Used to create and display the "Home" button.
	 * @throws IOException Thrown if the "Home" button image file can not be read, or is missing.
	 */
	private void initHomeButton() throws IOException{
		homeBtn = HomeButtonMaker.getContentPane();
		homeBtn.registerObserver(this);
		mainWindow.addLayer(homeBtn, BUTTON_LAYER, HomeButtonMaker.getXDefault(), HomeButtonMaker.getYDefault());
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
	 * Used to create the student's grade table for each Test button specified in buttons.
	 */
	private void makeTable(){
		//Used to offset the table's y-origin.
		int verticalShift = -30;
		
		//Column x-origins.
		int titleColumn = 80;
		int easyColumn  = 296;
		int normColumn  = 512;
		int hardColumn  = 728;
		
		int textHeight;
		int initialVerticalPadding = 70;
		int verticalPadding = 12;
		
		//Set the font.
		Font font;
		try{
			font = FontMaker.getFont(PREFERRED_FONT, 24);
		}catch(IndexOutOfBoundsException | IOException | FontFormatException e){
			font = FontMaker.getDefaultFont(24);
		}
		
		textHeight = FontMaker.getFontHeight(font, mainWindow.getGraphics());
		
		//Build the title data.
		JLabel skillTitle = new JLabel("<HTML><div style=\"text-align:left\">Skill</div></HTML>)");
		JLabel easyTitle  = new JLabel("<HTML><div style=\"text-align:center\">Easy</div></HTML>");
		JLabel normTitle  = new JLabel("<HTML><div style=\"text-align:center\">Normal</div></HTML>");
		JLabel hardTitle  = new JLabel("<HTML><div style=\"text-align:center\">Hard</div></HTML>");
		
		reportTable.add(skillTitle);
		reportTable.add(easyTitle);
		reportTable.add(normTitle);
		reportTable.add(hardTitle);
		
		ModuleSelectButtonInterface[] buttonList = buttons.getButtons();
		
		//Build the grade data.
		for(int i = 0; i < buttonList.length; i++){
			String[] grades  = getGradeStrings(buttonList[i]);
			JLabel nameLabel = new JLabel("<HTML><div style=\"text-align:left\">" + buttonList[i].getName() + "</div></HTML>)");
			JLabel easyLabel = new JLabel("<HTML><div style=\"text-align:center\">" + grades[0] + "</div></HTML>");
			JLabel normLabel = new JLabel("<HTML><div style=\"text-align:center\">" + grades[1] + "</div></HTML>");
			JLabel hardLabel = new JLabel("<HTML><div style=\"text-align:center\">" + grades[2] + "</div></HTML>");
			
			reportTable.add(nameLabel);
			reportTable.add(easyLabel);
			reportTable.add(normLabel);
			reportTable.add(hardLabel);
		}
		
		//TODO There's some odd behavior here when trying to center the text. (ColumnX - (label.getWidth() / 2))
		// provides the x origin required to center a label. However, on implementation, there's some scalar
		// effect that offsets the column as a whole, instead of on a per-label event. JLabel.setHorizontalPosition,
		// JLabel.setHorizontalTextPosition (both with SwingConstants.CENTER), HTML tags, and CSS all have no
		// effect on the text position. All System.out.println tests show the math is correctly implemented...
		
		//Find x/y coordinates for each component & set their size.
		//Y origin is determined with the following information:
		//  i = iteration, i%4 = column number, (i - i%4) / 4 = row number
		for(int i = 0; i < reportTable.size(); i++){
			boolean hasMore = ((i + 4) < reportTable.size());
			JLabel label = reportTable.get(i);
			label.setFont(font);
			label.setForeground(Color.WHITE);
			//First column (Names).
			if((i%4) == 0){
				int yOrigin = ((i / 4) * (textHeight + verticalPadding));
				label.setSize(new Dimension(easyColumn, textHeight));
				textArea.addComponent(label, titleColumn, initialVerticalPadding + yOrigin);
				//Draw a line beneath the text.
				if(hasMore){
					ContentPane aLine = new ContentPane(line, false, false);
					lines.add(aLine);
					//The multiplier of 1.35 was chosen via trial & error tests to find an esthetically pleasing location.
					mainWindow.addLayer(aLine, TEXT_LAYER, titleColumn, (int)(((initialVerticalPadding * 1.35) + yOrigin) + verticalShift));
				}
			//Second column (Easy Grades).
			}else if(i%4 == 1){
				label.setSize(new Dimension((normColumn - easyColumn), textHeight));
				label.setHorizontalTextPosition(SwingConstants.CENTER);
				textArea.addComponent(label, easyColumn, initialVerticalPadding + (((i - 1) / 4) * (textHeight + verticalPadding)));
			//Third column (Normal Grades).
			}else if(i%4 == 2){
				label.setSize(new Dimension((hardColumn - normColumn), textHeight));
				textArea.addComponent(label, normColumn, initialVerticalPadding + (((i - 2) / 4) * (textHeight + verticalPadding)));
			//Fourth column (Hard Grades).
			}else if(i%4 == 3){
				label.setSize(new Dimension((textArea.getWidth() - hardColumn - titleColumn), textHeight));
				textArea.addComponent(label, hardColumn, initialVerticalPadding + (((i - 3) / 4) * (textHeight + verticalPadding)));
			}
		}
		//Show the table.
		mainWindow.addLayer(textArea, TEXT_LAYER, 0, verticalShift);
	}
	
	/**
	 * A helper method used to convert reportMap data into Strings for use in the student's grade table.
	 * Returns a String array of length == 3. Each index has a String with a format of "grade / maxGrade".
	 * If a student's grade is < 0, then the grade is replaced with "-". Index 0 corresponds to the "Easy"
	 * difficulty level. Index 1 corresponds to the "Normal" difficulty level, and index 2; the "Hard"
	 * difficulty level.
	 * @param button The ModuleSelectButtonInterface that describes the Test Module who's grades are to be
	 *  returned in array-form.
	 * @return A String array (length == 3) of grades for the specified ModuleSelectButtonInterface.
	 *  Indexes 0 through 2 are (in increasing order) "Easy", "Normal", and "Hard" grades / maxGrade Strings.
	 */
	private String[] getGradeStrings(ModuleSelectButtonInterface button){
		String grade;
		String maxGrade;
		String[] grades = new String [3];
		ModuleReportSummary report = reportMap.get(button);
		grade     = (report.isEasyTaken())   ? new Integer(report.getEasyGrade()).toString()   : "-";
		maxGrade  = (report.isEasyTaken())   ? new Integer(report.getEasyMax()).toString()     : "-";
		grades[0] = grade + " / " + maxGrade;
		grade     = (report.isNormalTaken()) ? new Integer(report.getNormalGrade()).toString() : "-";
		maxGrade  = (report.isNormalTaken()) ? new Integer(report.getNormalMax()).toString()   : "-";
		grades[1] = grade + " / " + maxGrade;
		grade     = (report.isHardTaken())   ? new Integer(report.getHardGrade()).toString()   : "-";
		maxGrade  = (report.isHardTaken())   ? new Integer(report.getHardMax()).toString()     : "-";
		grades[2] = grade + " / " + maxGrade;
		return grades;
	}
	
	/**
	 * Used to open the directory specified by "path". Uses the system's default file browser to
	 *  open the specified folder. If this operation is not supported by the current platform,
	 *  a pop-up dialog is displayed asking the user to manually open the specified folder.
	 * @param path A String describing the path from the program's root directory to the folder
	 *  that is to be opened.
	 */
	private void openFolder(String path){
		Desktop desktop;
		File file = new File(path);
	    if(Desktop.isDesktopSupported()){
	    	desktop = Desktop.getDesktop();
	    	try{
	    		desktop.open(file);
	    	}catch (IOException e){
	    		manager.handleException(e);
	    	}
	    }else{
	    	String message = "Sorry, your system won't let us open the folder.\n" + ""
	    			+ "Please goto the folder that contains this program and open the following folders:\n\n" + path;
	    	JOptionPane.showMessageDialog(null, message, "Can't Open Folder", JOptionPane.INFORMATION_MESSAGE);
	    }
	}
	
	/**
	 * Used to remove all components that were created by this ReportCardScreen from mainWindow.
	 * WARNING: This method renders this instance of ReportCardScreen inoperable. All references
	 *  to this instance should be removed for garbage collection. If another ReportCardScreen
	 *  screen is needed, a new instance should be created.
	 */
	public void tearDown(){
		detailsBtn.removeObserver(this);
		rewardBtn.removeObserver(this);
		homeBtn.removeObserver(this);
		
		for(JLabel label: reportTable){
			textArea.remove(label);
			label = null;
		}
		
		for(ContentPane aLine: lines){
			mainWindow.getContainer().remove(aLine);
			aLine = null;
		}
		
		mainWindow.getContainer().remove(textArea);
		mainWindow.getContainer().remove(detailsBtn);
		mainWindow.getContainer().remove(rewardBtn);
		mainWindow.getContainer().remove(homeBtn);
		
		textArea    = null;
		detailsBtn  = null;
		rewardBtn   = null;
		homeBtn     = null;
		reportTable = null;
		buttons     = null;
		reportMap   = null;
		lines       = null;
	}
	
	
	@Override
	public void clicked(JComponent component) {
		if(component == detailsBtn){
			System.out.println("Opening this student's folder!");
			openFolder(manager.getTestFolderPath());
		}else if(component == rewardBtn){
			System.out.println("Opening the rewards page!");
			openFolder(manager.getRewardsFolderPath());
		}else if(component == homeBtn){
			System.out.println("Home clicked!");
			tearDown();
			try {
				manager.buildWelcomeScreen();
			} catch (IOException e) {
				manager.handleException(e);
			}
		}else{
			manager.handleException(
					new IllegalStateException("ReportCardScreen recieved a click event, but nothing happened!"));
		}
			
		
	}
	
}
