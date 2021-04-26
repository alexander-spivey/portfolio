//Alexander Spivey
//February 7, 2020
//Check Calculator

import java.util.Scanner;
public class Check{
  public static void main(String[] args) {
		Scanner myScanner = new Scanner( System.in );
		System.out.print("Enter the original cost of the check in the form xx.xx: ");
		double checkCost = myScanner.nextDouble();
		System.out.print("Enter the percentage tip that you wish to pay as a whole number (in the form xx):");
		double tipPercent = myScanner.nextDouble();
		tipPercent /= 100; //We want to convert the percentage into a decimal value
		System.out.print("Enter the number of people who went out to dinner: ");
		int numPeople = myScanner.nextInt();
		double totalCost;
		double costPerPerson;
		int dollars, dimes, pennies;
		totalCost = checkCost * (1 + tipPercent);
		costPerPerson = totalCost / numPeople;
		//get the whole amount, dropping decimal fraction
		dollars=(int)costPerPerson;
		//get dimes amount, e.g., 
		// (int)(6.73 * 10) % 10 -> 67 % 10 -> 7
		//  where the % (mod) operator returns the remainder
		//  after the division:   583%100 -> 83, 27%5 -> 2 
		dimes=(int)(costPerPerson * 10) % 10;
		pennies=(int)(costPerPerson * 100) % 10;
		System.out.println("Each person in the group owes " + dollars + "." + dimes + pennies);
	}  //end of main method   
} //end of class
