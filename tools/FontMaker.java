/**
 * Name:         Math Helper
 * Version:      0.10.0
 * Version Date: 04/16/2015
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

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import javax.swing.UIManager;

/**
 * This class provides methods for extracting and obtaining a java.awt.Font that may not otherwise
 *  be available to a system. Available fonts are described by the FontMaker public static fields.
 *  Only TrueType or system default fonts are supported.
 * @author Kenneth Chin
 */
public final class FontMaker{
	
	/** Indicates the System's default font */
	public final static int SYSTEM = 0;
	/** Indicates the Arial Bold font */
	public final static int ARIAL  = 1;
	/** Indicates the Berlin Sans FB font */
	public final static int TITLE  = 2;
	/** Indicates the DK Crayon Crumble font */
	public final static int CHALK  = 3;

	/**
	 * Private constructor prevents instantiation.
	 */
	private FontMaker(){}
	
	/**
	 * Used to obtain a Font object of the specified point size.
	 * @param fontType A font type indicated by the FontMaker field constants.
	 * @param fontSize An int indicating the font "point" size.
	 * @return A Font object of the specified point size.
	 * @throws IOException Thrown if there is a problem reading the Font's .ttf file.
	 * @throws FontFormatException Thrown if the font is unreadable by the Font class.
	 * @throws IndexOutOfBoundsException Thrown if the specified font is not available to the FontMaker class.
	 */
	public static Font getFont(int fontType, int fontSize)
			throws IOException, FontFormatException, IndexOutOfBoundsException{
		if(fontType == SYSTEM)
			return getDefaultFont(fontSize);
		Font newFont = Font.createFont(Font.TRUETYPE_FONT, new File(getFontPath(fontType)));
		return newFont.deriveFont((float)fontSize);
	}
	
	/**
	 * A convenience method that provides an Exception-free alternative to obtaining
	 *  a font. Obtains and returns the platform's system font at a specified point size.
	 * @param fontSize An int indicating the font "point" size.
	 * @return A Font object of the platform's system font, at a specified point size.
	 */
	public static Font getDefaultFont(int fontSize){
		Font newFont = UIManager.getDefaults().getFont("TabbedPane.font");
		return newFont.deriveFont((float)fontSize);
	}
	
	/**
	 * A convenience method used to determine a Font's pixel height.
	 * @param font A java.awt.Font object that is to have its height measured.
	 * @param g The java.awt.Graphics object that will draw "font".
	 * @return An int indicating the pixel height of the specified Font object.
	 */
	public static int getFontHeight(Font font, Graphics g){
		FontMetrics metrics = g.getFontMetrics(font);
		return metrics.getHeight();
	}
	
	/**
	 * A convenience method used to determine a String's pixel width in the specified Font.
	 * @param font The java.awt.Font that will be used to draw the given String.
	 * @param g The java.awt.Graphics object that will draw the given String in the specified font. 
	 * @param text The String that is to be measured.
	 * @return An int indicating the specified String's pixel width, using the specified Font.
	 */
	public static int getStringWidth(Font font, Graphics g, String text){
		FontMetrics metrics = g.getFontMetrics(font);
		return metrics.stringWidth(text);
	}
	
	/**
	 * Used to obtain a font with the bold style applied. If the font is already bold, the given
	 *  font is returned unchanged.
	 * @param font The Font object to have the "bold" style applied.
	 * @return The Font object with the "bold" style applied, or the unchanged Font if the font
	 *  is already bold.
	 */
	public static Font getBoldFont(Font font){
		if(font.isBold())
			return font;
		return font.deriveFont((Font.BOLD));
	}
	
	/**
	 * A helper method used to get a font's File path. Throws IndexOutOfBoundsException
	 *  if the selected fontID is not a font recognized by the FontMaker constants.
	 * @param fontID The int indicated by the FontMaker field constant supplied by the user.
	 * @return A String indicating the font's File path.
	 */
	private static String getFontPath(int fontID){
		for(FontTypes font:FontTypes.values()){
			if(font.getID() == fontID)
				return font.getPath();
		}
		throw new IndexOutOfBoundsException("The selected font type does not exist.");
	}
	
	/**
	 *An enumeration used to store and organize font file paths.
	 *Each font enumeration must have an int "identifier" greater than 0,
	 * and must be made publicly available as a FontMaker field constant.
	 */
	public enum FontTypes{
		/**
		 * The Arial Bold font.
		 */
		ARIAL(1, "\\fonts\\arialbd.ttf"),
		
		/**
		 * The Berlin Sans FB font.
		 */
		TITLE(2, "\\fonts\\BRLNSR.TTF"),
		
		/**
		 * The DK Crayon Crumble font.
		 */
		CHALK(3, "\\fonts\\DK Crayon Crumble.ttf");
		
		private int identifier;
		private String path;
		
		/**
		 * The private constructor for FontTypes.
		 * @param identifier An int > 0, and equal to one of the FontMaker constants.
		 * @param path This font's file path from the program's root directory.
		 */
		private FontTypes(int identifier, String path){
			this.identifier = identifier;
			this.path = path;
		}
		
		/**
		 * Used to obtain this font's ID, that is equal to its identifier in the
		 *  FontMaker constants.
		 * @return An int indicating this font's ID, that is equal to its FontMaker constant.
		 */
		private int getID(){
			return identifier;
		}
		
		/**
		 * Used to obtain this font's file path from the program's root directory.
		 * @return A String indicating this font's file path from the program's root directory.
		 */
		public String getPath(){
			String parentDir = System.getProperty("user.dir");
    		return parentDir + path;
		}
	}

}
