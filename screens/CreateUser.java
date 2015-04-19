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
package project.screens;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Font;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CreateUser {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JComboBox comboBox;
	private JLabel lblUserName;
	private JLabel lblPassword;
	private JLabel lblFirstName;
	private JLabel lblLastName;
	private JLabel lblSelectGradeLevel;
	private JButton btnSubmit;
	private JButton btnExit;
	private JLabel lblonceYouCreate;

	/**
	 * Launch the application.
	 */
	public void launchCreateUser() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreateUser window = new CreateUser();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CreateUser() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		String[] gradeOptions = {"Pre K - K", "Grades 1 - 2", "Grades 3 - 4"};
		
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Segoe Script", Font.PLAIN, 22));
		frame.getContentPane().setBackground(Color.BLUE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		JLabel lblCreateAccount = new JLabel("Create Account");
		springLayout.putConstraint(SpringLayout.NORTH, lblCreateAccount, 36, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, lblCreateAccount, -164, SpringLayout.EAST, frame.getContentPane());
		lblCreateAccount.setForeground(Color.CYAN);
		lblCreateAccount.setFont(new Font("Segoe Print", Font.BOLD, 34));
		frame.getContentPane().add(lblCreateAccount);
		
		textField = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, textField, -190, SpringLayout.EAST, lblCreateAccount);
		springLayout.putConstraint(SpringLayout.EAST, textField, 0, SpringLayout.EAST, lblCreateAccount);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		springLayout.putConstraint(SpringLayout.EAST, textField_1, 0, SpringLayout.EAST, lblCreateAccount);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, textField_2, 20, SpringLayout.SOUTH, textField_1);
		springLayout.putConstraint(SpringLayout.EAST, textField_2, -164, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, textField_3, 23, SpringLayout.SOUTH, textField_2);
		springLayout.putConstraint(SpringLayout.EAST, textField_3, -164, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(textField_3);
		textField_3.setColumns(10);
		
		comboBox = new JComboBox(gradeOptions);
		springLayout.putConstraint(SpringLayout.NORTH, comboBox, 24, SpringLayout.SOUTH, textField_3);
		springLayout.putConstraint(SpringLayout.EAST, comboBox, -164, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(comboBox);
		
		lblUserName = new JLabel("User Name");
		springLayout.putConstraint(SpringLayout.NORTH, lblUserName, 45, SpringLayout.SOUTH, lblCreateAccount);
		springLayout.putConstraint(SpringLayout.EAST, lblUserName, -19, SpringLayout.WEST, textField);
		springLayout.putConstraint(SpringLayout.NORTH, textField, 2, SpringLayout.NORTH, lblUserName);
		lblUserName.setFont(new Font("Segoe Print", Font.PLAIN, 12));
		lblUserName.setForeground(Color.CYAN);
		frame.getContentPane().add(lblUserName);
		
		lblPassword = new JLabel("Password");
		springLayout.putConstraint(SpringLayout.NORTH, lblPassword, 21, SpringLayout.SOUTH, lblUserName);
		springLayout.putConstraint(SpringLayout.NORTH, textField_1, 2, SpringLayout.NORTH, lblPassword);
		springLayout.putConstraint(SpringLayout.WEST, textField_1, 19, SpringLayout.EAST, lblPassword);
		lblPassword.setFont(new Font("Segoe Print", Font.PLAIN, 12));
		lblPassword.setForeground(Color.CYAN);
		springLayout.putConstraint(SpringLayout.EAST, lblPassword, 0, SpringLayout.EAST, lblUserName);
		frame.getContentPane().add(lblPassword);
		
		lblFirstName = new JLabel("First Name");
		springLayout.putConstraint(SpringLayout.WEST, textField_2, 19, SpringLayout.EAST, lblFirstName);
		lblFirstName.setFont(new Font("Segoe Print", Font.PLAIN, 12));
		lblFirstName.setForeground(Color.CYAN);
		springLayout.putConstraint(SpringLayout.SOUTH, lblFirstName, 0, SpringLayout.SOUTH, textField_2);
		springLayout.putConstraint(SpringLayout.EAST, lblFirstName, 0, SpringLayout.EAST, lblUserName);
		frame.getContentPane().add(lblFirstName);
		
		lblLastName = new JLabel("Last Name");
		springLayout.putConstraint(SpringLayout.WEST, textField_3, 19, SpringLayout.EAST, lblLastName);
		lblLastName.setFont(new Font("Segoe Print", Font.PLAIN, 12));
		lblLastName.setForeground(Color.CYAN);
		springLayout.putConstraint(SpringLayout.SOUTH, lblLastName, 0, SpringLayout.SOUTH, textField_3);
		springLayout.putConstraint(SpringLayout.EAST, lblLastName, 0, SpringLayout.EAST, lblUserName);
		frame.getContentPane().add(lblLastName);
		
		lblSelectGradeLevel = new JLabel("Select Grade Level");
		springLayout.putConstraint(SpringLayout.WEST, comboBox, 19, SpringLayout.EAST, lblSelectGradeLevel);
		lblSelectGradeLevel.setFont(new Font("Segoe Print", Font.PLAIN, 12));
		lblSelectGradeLevel.setForeground(Color.CYAN);
		springLayout.putConstraint(SpringLayout.SOUTH, lblSelectGradeLevel, 0, SpringLayout.SOUTH, comboBox);
		springLayout.putConstraint(SpringLayout.EAST, lblSelectGradeLevel, 0, SpringLayout.EAST, lblUserName);
		frame.getContentPane().add(lblSelectGradeLevel);
		
		btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					String userName = textField.getText();
					String password = textField_1.getText();
					String firstName = textField_2.getText();
					String lastName = textField_3.getText();
					String gradeLevel = (String) comboBox.getSelectedItem();
					
					submitUser(userName, password, firstName, lastName, gradeLevel);
					
					System.out.println(userName);
					System.out.println(password);
					System.out.println(firstName);
					System.out.println(lastName);
					System.out.println(gradeLevel);
					
					frame.dispose();
				}
				catch(Exception exception){
			
				}
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnSubmit, 25, SpringLayout.SOUTH, comboBox);
		springLayout.putConstraint(SpringLayout.WEST, btnSubmit, 0, SpringLayout.WEST, textField);
		frame.getContentPane().add(btnSubmit);
		
		btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnExit, 25, SpringLayout.SOUTH, comboBox);
		springLayout.putConstraint(SpringLayout.WEST, btnExit, 59, SpringLayout.EAST, btnSubmit);
		springLayout.putConstraint(SpringLayout.EAST, btnExit, 0, SpringLayout.EAST, lblCreateAccount);
		frame.getContentPane().add(btnExit);
		
		lblonceYouCreate = new JLabel("(Once you create an account you willbe brough back to the Login screen)");
		springLayout.putConstraint(SpringLayout.NORTH, lblonceYouCreate, 0, SpringLayout.SOUTH, lblCreateAccount);
		springLayout.putConstraint(SpringLayout.EAST, lblonceYouCreate, -88, SpringLayout.EAST, frame.getContentPane());
		lblonceYouCreate.setFont(new Font("Segoe Print", Font.PLAIN, 11));
		lblonceYouCreate.setForeground(Color.CYAN);
		frame.getContentPane().add(lblonceYouCreate);
		frame.setBackground(Color.BLUE);
		frame.setBounds(100, 100, 630, 449);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	/** submitUser method must be implemented once database is constructed
	 * 
	 * @param userName
	 * @param password
	 * @param firstName
	 * @param LastName
	 * @param gradeLevel
	 */
	public void submitUser(String userName, String password, String firstName, String LastName, String gradeLevel){
		
	}
}

