/**
 * Program to determine unsafe banks after transactions
 * @author Tyler Hagmann
 * @version 2020-06 (4.16.0)
 * Date: September 2, 2020
 */
import java.util.Scanner;
public class Assignment1 {
	/** fills in bank transaction info
	 * @param banks: total number of banks inputted
	 * @param bankbal: initial balance of each bank
	 * @param bankinfo: info about each banks' transactions
	 * no return value
	 */
	public static void readBankInfo(int banks, int[] bankbal, double[][] bankinfo) {
		Scanner in = new Scanner(System.in);
		int transactions;
		for(int i=0;i<banks;i++) {
			System.out.println("Enter bank " + i + " balance and borrowers:");
			bankbal[i] = in.nextInt();//set first value to bank balance
			transactions = in.nextInt();//get value of how many transaction		
			bankinfo[i] = new double[(2*transactions)+1];//each transaction adds 2 columns, the value itself is a column as well
			bankinfo[i][0] = transactions;//set first column to be number of transactions, will use later
			for(int j=1;j<(2*transactions)+1;j+=2) {//cute little loop to fill all columns in 2d array
				bankinfo[i][j] = in.nextInt();//first number is always int, also avoids problems when converting back to int later on
				bankinfo[i][j+1] = in.nextDouble();//possibly a double, so cast it as one
			}
		}
		in.close();//close scanner because eclipse yelled at me
	}
	/** calculate bank assets after initial transactions
	 * @param assets: bank assets after transactions
	 * @param bankbal: initial balance of each bank
	 * @param bankinfo: info about each banks' transactions
	 * no return value
	 */
	public static void calculateAssets(double[] assets, int[] bankbal, double[][] bankinfo) {
		for(int i=0;i<bankinfo.length;i++) {
			assets[i] = bankbal[i];//assets initially just balance of bank
			for(int j=2;j<bankinfo[i].length;j+=2) {//in array, additional assets come from index 2,4,6,etc
				assets[i] += bankinfo[i][j];
			}
		}
	}
	/** update assets of each bank if a bank is unsafe, label unsafe banks
	 * @param minval: minimum balance for a bank to be safe
	 * @param assets: bank assets after transactions
	 * @param bankinfo: info about each banks' transactions
	 * @param unsafe: boolean true if a bank is below minval
	 * no return value
	 */
	public static void updateBorrowers(int minval, double[] assets, double[][] bankinfo, Boolean[] unsafe) {
		for(int i=0;i<bankinfo.length;i++) {
			if((int)assets[i] < minval) {//mark unsafe
				unsafe[i] = true;
			}
			else {
				unsafe[i] = false;
			}
		}
		for(int u=0;u<unsafe.length;u++) {
			if(unsafe[u]) {//change assets of other banks if a bank is unsafe
				for (int i=0; i<bankinfo.length; i++) { 
					for (int j=1; j<bankinfo[i].length; j+=2) {//in array, banks referenced come from index 1,3,5,etc
						if((int)bankinfo[i][j] == u) {//if bank that is unsafe is found to be lending another bank money,
							assets[i] -= bankinfo[i][j+1]; //subtract amount bank is lending from total assets of borrowing bank
						}
					}
				}
			}
		}
		for(int i=0;i<bankinfo.length;i++) {
			if((int)assets[i] < minval) {//recheck safe/unsafe now that assets were updated based on initial safe/unsafe
				unsafe[i] = true;
			}
			else {
				unsafe[i] = false;
			}
		}
	}
	/** print which banks are unsafe
	 * @param unsafe: boolean true if a bank is below minval
	 * no return value
	 */
	public static void printUnsafeBanks(Boolean[] unsafe) {
		System.out.print("Unsafe banks are ");
		for(int i=0;i<unsafe.length;i++) {
			if(unsafe[i]) {//if bank is unsafe, print index
				System.out.print(i + " ");
			}
		}
	}
	//Main method
	public static void main(String[]args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter the number of banks: ");
		int banks = in.nextInt();//get number of banks
		int[] bankbal = new int[banks];//for initial balance of each bank
		double[][] bankinfo = new double[banks][];//for number of and amount per transaction
		double[] assets = new double[banks];//for updating balance of each bank
		Boolean[] unsafe = new Boolean[banks];//for checking which banks are unsafe
		System.out.print("Enter the minimum value for the assets: ");
		int minval = in.nextInt();//unsafe if updated is below this
		readBankInfo(banks, bankbal, bankinfo);
		calculateAssets(assets, bankbal, bankinfo);
		updateBorrowers(minval, assets, bankinfo, unsafe);
		printUnsafeBanks(unsafe);
		in.close();//close scanner because eclipse yelled at me
	}
}