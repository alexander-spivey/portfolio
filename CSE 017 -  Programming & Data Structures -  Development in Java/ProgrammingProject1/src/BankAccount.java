import java.util.*;
public class BankAccount extends Transaction{
	//data members
	private String name;
	private int account;
	private double balance;
	private int transCount;
	Transaction[] transactions = new Transaction[100];
	Random rand = new Random();
	
	//Constructors
	/**
	 * base bank account constructor
	 */
	BankAccount() { 
		name = "";
		balance = 0.00;
		account = rand.ints(100000,999999).findFirst().getAsInt();
	}
	
	/**
	 * main bank account constructor
	 * @param n: name
	 * @param b: balance
	 */
	BankAccount(String n, double b) {
		name = n;
		balance = b;
		account = rand.ints(100000,999999).findFirst().getAsInt();
	}
	
	//main methods
	/**
	 * gets account name
	 * @return name: name of account
	 */
	public String getName() { //accessor
		return name;
	}
	
	/**
	 * gets account number
	 * @return account: account number 
	 */
	public int getAccount() { //accessor
		return account;
	}
	
	/**
	 * returns account balance
	 * @return balance: account balance
	 */
	public double getBalance() { //accessor
		return balance;
	}
	
	/**
	 * adds a transaction; deposit +++
	 * @param a: amount added
	 * @param d: description of transaction
	 */
	public void deposit(double a, String d) { //mutator
		balance += a;
		transactions[transCount] = new Transaction('D', a, balance, d);
		transCount++;
	}
	
	/**
	 * adds a transaction; withdraw ---
	 * @param a: amount subtracted
	 * @param d: description of transaction
	 * @return boolean: true if end balance is positive, false if end balance negative 
	 */
	public boolean withdraw(double a, String d) { //mutator
		balance -= a;
		transactions[transCount] = new Transaction('W', a, balance, d);
		transCount++;
		if (balance >= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * converts information into a string
	 * @return: account information along with all transactions associated with the account
	 */
	public String toString() { //accessor
		String list = "";
		String bankInfo = String.format("Name: %s\nAccount Number: %s\nBalance: $%.2f\nTransactions: \n", name, account,
				balance);
		for(int i = 0; i < transCount; i++) {
			list += ((i+1)+"."+transactions[i].toString());
		}
		return bankInfo + list + "\n";
	}
	
}
