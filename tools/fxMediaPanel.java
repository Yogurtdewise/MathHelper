package project.tools;

import project.run.GUIManager;
import javafx.embed.swing.JFXPanel;

/**
 * This class is the program's media panel; the primary container for all jfx media elements.
 * @author Chris Moraal
 *
 */
public class fxMediaPanel {
	//Default Sizes
	private int panelWidth = 700; //The pixel height of the JFX panel's drawing area.
	private int panelHeight = 394; //The pixel width of the JFX panel's drawing area.
	
	private JFXPanel mediaPanel; //The JFX Panel
	
	private GUIManager manager; //The GUIManager that is managing MainWindow.
	
	/**
	 * A basic constructor. Sets media panel size to default size.
	 * @param manager The GUIManager object that will manage this MainWindow.
	 */
	public fxMediaPanel(GUIManager manager) {
		this.mediaPanel = new JFXPanel();
		this.manager = manager;
		
		this.mediaPanel.setSize(panelWidth , panelHeight);
	}
	
	/**
	 * A constructor defining the width and height of the JFX panel.
	 * @param width An int representing the number of pixels panels width.
	 * @param height An int representing the number of pixels panels height.
	 * @param manager The GUIManager object that will manage this MainWindow.
	 */
	public fxMediaPanel(GUIManager manager, int width, int height) {
		this.mediaPanel = new JFXPanel();
		this.manager = manager;
		
		this.panelWidth = width;
		this.panelHeight = height;
		
		this.mediaPanel.setSize(width , height);
	}
	
	public JFXPanel getMediaPanel() {
		return this.mediaPanel;
	}
	
	public int getPanelHeight() {
		return this.panelHeight;
	}
	
	public int getPanelWidth() {
		return this.panelWidth;
	}
}
