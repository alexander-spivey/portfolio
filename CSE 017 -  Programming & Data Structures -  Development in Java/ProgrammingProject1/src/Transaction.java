import java.util.*;

public class Transaction {
	// data members
	private java.util.Date date;
	private char type;
	private double amount;
	private double balance;
	private String description;

	// constructor
	/**
	 * base constructor, sets all values to 0 or N
	 */
	Transaction() { 
		date = new Date();
		type = 'N';
		amount = 0;
		balance = 0;
		description = "N/A";
	}

	/**
	 * main transaction constructor
	 * @param t: type of transaction: w or d
	 * @param a: amount
	 * @param b: balance
	 * @param desc: description of transaction
	 */
	Transaction(char t, double a, double b, String desc) { 
		date = new Date();
		type = t;
		amount = a;
		balance = b;
		description = desc;
	}

	// main methods
	/**
	 * gets current date
	 * @return date: current date, day, and time
	 */
	public java.util.Date getDate() { //accessor
		return date;
	}
	
	/**
	 * gets current transaction type
	 * @return type: 'W'ithdraw or 'D'eposit
	 */
	public char getType() { //accessor
		return type;
	}

	/**
	 * gets amount of transaction
	 * @return amount: amount being withdrawn or deposited
	 */
	public double getAmount() { //accessor
		return amount;
	}

	/**
	 * gets current balance of account
	 * @return balance: amount of money in account
	 */
	public double getBalance() { //accessor
		return balance;
	}

	/**
	 * converts information into a string
	 * @return: a string with list of the single transaction with date, type, amount, balance, and description
	 */
	public String toString() { //accessor
		String output;
		output = String.format("\tDate: %s\tType: %s\tAmount: $%.2f\tBalance: $%.2f\tDescription: %s\n", date, type,
				amount, balance, description);
		return output;
	}
}
