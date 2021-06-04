import java.util.Scanner;

public class BankManager {
	//main method
	public static void main(String[] args) {
		Bank.monthlyFee = 20.0;
		Scanner input = new Scanner(System.in);
		Bank myBank = new Bank("LEHIGH UNIVERSITY BANK", "Bethlehem");
		myBank.addAccount(new BankAccount("John Blake", 1200));
		myBank.addAccount(new BankAccount("Sarah Browm", 2045));
		myBank.addAccount(new BankAccount("Emma Johnson", 450));
		myBank.addAccount(new BankAccount("Susan Jones", 23000));
		myBank.addAccount(new BankAccount("Mary Chen", 550));
		
		System.out.println("Welcome to LEHIGH UNIVERSITY BANK");
		int operation = 0;
		do {
			int manage = 0;
			operation = getOperation(input);
			switch (operation) {
			case 1: //manage existing
				System.out.println("Enter 6 digit account number that you would like to modify: ");
				if(input.hasNextInt()) {
					int accN = input.nextInt();
					if(myBank.getAccount(accN) != null)
					{
						BankAccount currentAccount = myBank.getAccount(accN);
						do {
							manage = manageExisting(input);
							switch (manage) {
								case 1: //view balance 
									System.out.println("Current balance: "+currentAccount.getBalance());
									break;
								case 2: //deposit
									System.out.println("Enter amount deposited: ");
									if(input.hasNextDouble()) {
										double a = input.nextDouble();
										System.out.println("Enter description: ");
										input.nextLine();
										String d = input.nextLine();
										currentAccount.deposit(a, d);
										System.out.println("New balance: "+currentAccount.getBalance());
									} else {
										input.nextLine();
										System.out.println("Error: Input not accepted.");
										input.nextLine();
									}
									break;
								case 3: //withdraw
									System.out.println("Enter amount withdrawed: ");
									if(input.hasNextDouble()) {
										double a = input.nextDouble();
										System.out.println("Enter description: ");
										input.nextLine();
										String d = input.next();
										currentAccount.withdraw(a, d);
										System.out.println("New balance: "+currentAccount.getBalance());
									} else {
										input.nextLine();
										System.out.println("Error: Input not accepted.");
										input.nextLine();
									}
									break;
								case 4: //view statement
									System.out.println(currentAccount.toString());
									break;
								case 5: //exit manage existing
									System.out.println("Exiting account.");
									break;
							}
						} while (manage != 5);
					} else {
						System.out.println("Account number " + accN + " was not found.");
					}
				} else {
					input.nextLine();
					System.out.println("Error: Input not accepted.");
					input.nextLine();
				}
				break;
			case 2: //adding
				System.out.println("Enter account name and balance: ");
				String addName = input.next() + " " + input.next();
				double addBal = input.nextDouble();
				myBank.addAccount(new BankAccount(addName, addBal));
				System.out.printf("Account with name of %s and balance of $%.2f has been added \n", addName, addBal);
				break;
			case 3: //remove
				System.out.println("Enter 6 digit account number that you would like to remove: ");
				if(input.hasNextInt()) {
					int rACC = input.nextInt();
					myBank.removeAccount(rACC);
				} else {
					input.nextLine();
					System.out.println("Error: Input not accepted.");
					input.nextLine();
				}
				break;
			case 4: //apply monthly fee
				myBank.applyMonthlyFee();
				System.out.println("Monthly fee applied to every account");
				break;
			case 5: //printing all
				System.out.printf("\n"+myBank.toString());
				break;
			case 6: //exiting
				System.out.println("Thank you for using BANK OF LEHIGH UNIVERSITY.");
				System.out.println("Exiting program...");
				break;
			}
		} while (operation != 6);
	}
	
	//do while methods
	/**
	 * displays the menu and return the selected operation
	 * @param input: Scanner object to read the selected operation
	 * @return value entered by the user
	 */
	public static int getOperation(Scanner input) {
		int op = 0;
		do {
			System.out.println("Select an operation: ");
			System.out.println(" 1: Manage existing account");
			System.out.println(" 2: Add new account");
			System.out.println(" 3: Remove an existing account");
			System.out.println(" 4: Apply monthly fee");
			System.out.println(" 5: View all accounts and statements");
			System.out.println(" 6: Exit program");
			if (input.hasNextInt()) {
				op = input.nextInt();
				if (op >= 1 && op <= 6)
					break;
				else
					System.out.println("Invalid operation. Must be an integer from 1 to 6.");
			} else {
				input.nextLine();
				System.out.println("Invalid operation. Must be an integer.");
			}
		} while (true);
		return op;
	}
	
	/**
	 * displays the menu and return the selected operation for case 1
	 * @param input: Scanner object to read the selected operation
	 * @return value entered by the user
	 */
	public static int manageExisting(Scanner input) {
		int op = 0;
		do {
			System.out.println("\nSelect an operation: ");
			System.out.println(" 1: View balance");
			System.out.println(" 2: Deposit fund(s)");
			System.out.println(" 3: Withdraw fund(s)");
			System.out.println(" 4: View statement");
			System.out.println(" 5: Exit specific account view");
			if (input.hasNextInt()) {
				op = input.nextInt();
				if (op >= 1 && op <= 5)
					break;
				else
					System.out.println("Invalid operation. Must be an integer from 1 to 5.");
			} else {
				input.nextLine();
				System.out.println("Invalid operation. Must be an integer.");
			}
		} while (true);
		return op;
	}

}
