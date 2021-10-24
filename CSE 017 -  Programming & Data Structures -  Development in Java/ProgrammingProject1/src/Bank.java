public class Bank extends BankAccount{
	//data members
	private String name;
	private String branch;
	BankAccount[] bankAccounts = new BankAccount[10];
	public static double monthlyFee;
	private int accounts;
	
	//Constructors
	/**
	 * base constructor, sets everything to blank
	 */
	Bank() {
		name = "none";
		branch = "none";
		accounts = 0;
	}
	
	/**
	 * main constructor
	 * @param n: name
	 * @param b: branch
	 */
	Bank(String n, String b) {
		name = n;
		branch = b;
		accounts = 0;
	}
	
	//main methods
	/**
	 * tries to find account
	 * @param number: account number we are trying to find
	 * @return bankAccounts[i]: returns specific reference to the account linked at i
	 */
	public BankAccount getAccount(int number) { //accessor
		for(int i = 0; i < accounts; i ++) {
			if(bankAccounts[i].getAccount() == number) {
				return bankAccounts[i];
			}
		}
		return null;
	}
	
	/**
	 * creates account and increase number of accounts
	 * @param ba: adding a BankAccount(String n, double b)
	 */
	public void addAccount(BankAccount ba) { //mutator
		if (accounts + 1 >= bankAccounts.length) {
			System.out.println("Error: Size greater than Array");
			return;
		}
		bankAccounts[accounts] = ba;
		accounts++;
	}
	
	/**
	 * removes account if account was found, and lowers value of account
	 * @param accountNumber: the account number that we are trying to find and remove
	 */
	public void removeAccount(int accountNumber) { //mutator
		BankAccount[] tempArray = new BankAccount[10];
		if (getAccount(accountNumber) == null) {
			System.out.println("Error, ID was not found");
			return;
		} else {
			int foo = 0;
			// create temporary array
			for (int i = 0; i < accounts + 1; i++) {
				int ID = bankAccounts[i].getAccount();
				if (accountNumber != ID) {
					tempArray[foo] = bankAccounts[i];
					foo++;
				} else {
					accounts--;
				}
			}
			// hard-coding array into standard
			for (int j = 0; j < tempArray.length; j++) {
				bankAccounts[j] = tempArray[j];
			}
			bankAccounts[accounts] = new BankAccount(); //removing the last copy --> [1,2,3,4,4,0...] -->  [1,2,3,4,0,0...]
			System.out.println("Account "+accountNumber+" has been removed.");
		}
	}
	
	/**
	 * applies a monthly fee to each account, labeled 'w' and gives a description
	 */
	public void applyMonthlyFee() { //mutator
		for(int i = 0; i < accounts; i++)
		{
			bankAccounts[i].withdraw(monthlyFee, "Monthly Maintenance Fee");
		}
	}
	
	/**
	 * converts information into a string
	 * @return: a string with contained info of all bank accounts and name and branch of bank
	 */
	public String toString() { //accessor
		String output = "";
		for(int i = 0; i < accounts; i++) {
			output += bankAccounts[i].toString();
		}
		String bank =  String.format("Name: %s\tBranch:  %s\n \n", name, branch);
		return bank + output;
	}
}
