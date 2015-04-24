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

import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import project.run.GUIManager;
import project.tools.ImageLoader;

import javax.swing.JTextField;
import javax.swing.JPasswordField;

import java.awt.GridBagLayout;
import java.awt.BorderLayout;

import javax.swing.SpringLayout;
import javax.swing.JButton;



import java.awt.Color;

import javax.swing.JLayeredPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MathHelperLogin {
	
	private static GUIManager GUI;
	private JFrame frame;
	private static final String DIRECTORY_PATH = "\\images\\Login_CreateUser\\";
	private JPasswordField passwordField;
	private JTextField textField;
	private JLabel lblUserName;
	private JLabel lblPassword;
	
	private boolean isValid = false;

	/**
	 * Launch the application.
	 */
	public static void runLaunch(final GUIManager GUI) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MathHelperLogin window = new MathHelperLogin(GUI);
					window.frame.setVisible(true);
				} catch (Exception e) {
					GUI.handleException(e);
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MathHelperLogin(GUIManager GUI) throws IOException {
		this.GUI = GUI;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() throws IOException {
		frame = new JFrame();
		frame.setBounds(100, 100, 630, 448);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initBackground();
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		passwordField = new JPasswordField();
		springLayout.putConstraint(SpringLayout.NORTH, passwordField, 222, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, passwordField, 230, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, passwordField, 361, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(passwordField);
		
		textField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, textField, 176, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, textField, 230, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, textField, 361, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		lblUserName = new JLabel("User Name");
		springLayout.putConstraint(SpringLayout.NORTH, lblUserName, 156, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblUserName, 269, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(lblUserName);
		
		lblPassword = new JLabel("Password");
		springLayout.putConstraint(SpringLayout.NORTH, lblPassword, 202, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblPassword, 272, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(lblPassword);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					String userName = textField.getText();
					String password = passwordField.getText();
					
					validateUser(userName, password);
					if(getIsValid()){
						frame.dispose();
						GUI.runGUI();
					}
					else{
						JOptionPane.showMessageDialog(null, "Invalid User Name and/or Password");
					}
					
				}
				catch(Exception e){
			
				}
				
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnLogin, 256, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, btnLogin, 230, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnLogin, 361, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(btnLogin);
		
		JButton btnCreateUser = new JButton("Create User");
		btnCreateUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					CreateUser launch = new CreateUser();
					launch.launchCreateUser();
				}
				catch(Exception exception){
			
				}
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnCreateUser, 285, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, btnCreateUser, 230, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnCreateUser, 361, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(btnCreateUser);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnExit, 314, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, btnExit, 230, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnExit, 361, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(btnExit);
	}
	
	private void initBackground() throws IOException{
		String path = DIRECTORY_PATH + "LoginBackground.png";
		BufferedImage image = ImageLoader.getBufferedImage(path);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setContentPane(new JLabel(new ImageIcon(image)));
	}
	
	/**
	 * validateUser method must be implemented once database is constructed
	 * @param userName
	 * @param Password
	 */
	private void validateUser(String userName, String Password){
		isValid = true;
	}
	
	public boolean getIsValid(){
		return isValid;
	}
}
