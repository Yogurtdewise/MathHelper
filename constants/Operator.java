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
package project.constants;

/**
 * This enum is used to define basic arithmetic operators. It provides String names, symbol
 *  representations, and operations on two operands for Addition, Subtraction, Multiplication,
 *  and Division.
 * @author Kenneth Chin
 */
public enum Operator{
	/**
	 * The Addition operator.
	 */
	ADD("Add", "+") {
		@Override
		public int doAction(int operand1, int operand2){
			return (operand1 + operand2);
		}
	},
	/**
	 * The Subtraction operator.
	 */
	SUBTRACT("Subtract", "-") {
		@Override
		/**
		 * Subtracts operand2 from operand1.
		 */
		public int doAction(int operand1, int operand2){
			return (operand1 - operand2);
		}
	},
	/**
	 * The Multiplication operator.
	 */
	MULTIPLY("Multiply", "X") {
		@Override
		public int doAction(int operand1, int operand2) {
			return (operand1 * operand1);
		}
	},
	/**
	 * The Division operator.
	 */
	DIVIDE("Divide", "/") {
		@Override
		/**
		 * Divides operand1 by operand2. Throws an ArithmeticException if operand2 == 0.
		 */
		public int doAction(int operand1, int operand2) {
			if(operand2 == 0)
				throw new ArithmeticException("Operator.DIVIDE: operand2 is zero. Can not divide by zero.");
			return (operand1 / operand2);
		}
	};
	
	private String name;
	private String symbol;
	
	/**
	 * The private Operator constructor.
	 * @param name A String indicating this Operator's name.
	 * @param symbol A String indicating this Operator's symbol representation.
	 */
	private Operator(String name, String symbol){
		this.name   = name;
		this.symbol = symbol;
	}
	
	/**
	 * Used to obtain the name of this Operator.
	 * @return The String indicating this Operator's name.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Used to obtain the String representation of this Operator's symbol.
	 * @return The String representation of this Operator's symbol.
	 */
	public String getSymbol(){
		return symbol;
	}
	
	/**
	 * Performs the action operand1 operator operand2 for this Operator.
	 * @param operand1 An int. The left operand.
	 * @param operand2 An int. The right operand.
	 * @return An int indicating the result of operand1 operator operand2 for this Operator.
	 */
	public abstract int doAction(int operand1, int operand2);
}