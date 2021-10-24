import java.util.InputMismatchException;
import java.util.Scanner;

public class LibraryManager {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		Catalog myLibrary = new Catalog();
		myLibrary.loadFromFile("titles.txt");
		
		//Switch case
		int operation = 0;
		do {
			//try case there to catch any exceptions
			try {
				operation = getOperation(input);
				switch (operation) {
				case 1: //Find a title with a given call Number
					System.out.println("Please enter call number in format [B/P-ddd-ddd-ddd]: ");
					input.nextLine();
					String cN = input.nextLine();
					checkCallNumber(cN);
					if(myLibrary.searchCallNumber(cN) != null) {
						System.out.println(myLibrary.searchCallNumber(cN).formattedToString());
					} else {
						System.out.println("Error, call number not found");
					}
					break;
				case 2: //Find titles with a given title
					System.out.println("Please enter a title: ");
					input.nextLine();
					String t = input.nextLine();
					Title[] tempT = myLibrary.searchTitle(t);
					System.out.println("List of titles found: ");
					int check = 0;
					while(tempT[check] != null) {
						System.out.println(tempT[check].formattedToString());
						check++;
					}
					if(check == 0) {
						System.out.println("No titles were found with the name: " + t);
					}
					break;
				case 3: 
					System.out.println("Please enter a year: ");
					int y = input.nextInt();
					checkDate(y);
					Title[] tempY = myLibrary.searchYear(y);
					System.out.println("List of titles found: ");
					int checkY = 0;
					while(tempY[checkY] != null) {
						System.out.println(tempY[checkY].formattedToString());
						checkY++;
					}
					if(checkY == 0) {
						System.out.println("No titles were found during the year: "+y);
					}
					break;
				case 4: //made with help from Tyler Hugman
                    System.out.println("Enter the title:");
                    input.nextLine();
                    String a4 = input.nextLine();
                    System.out.println("Enter the publisher:");
                    String b4 = input.nextLine();
                    System.out.println("Enter the year of publication");
                    int c4 = input.nextInt();
                    checkDate(c4);
                    System.out.println("Enter the number of copies:");
                    int d4 = input.nextInt();
                    System.out.println("Enter type of title (book/periodical):");
                    input.nextLine();
                    String e4 = input.nextLine();
                    if(e4.equals("book")) {
                        System.out.println("Enter the call number (B-ddd-ddd-ddd):");
                        String f4 = input.nextLine();
                        checkCallNumber(f4);
                        System.out.println("Enter the author");
                        String g4 = input.nextLine();
                        System.out.println("Enter ISBN");
                        String h4 = input.nextLine();
                        Book tempB = new Book(f4,a4,b4,c4,d4,g4,h4);
                        myLibrary.addItem(tempB);
                    } else if(e4.equals("periodical")) {
                        System.out.println("Enter the call number (P-ddd-ddd-ddd):");
                        String f4 = input.nextLine();
                        checkCallNumber(f4);
                        System.out.println("Enter the issue:");
                        int g4 = input.nextInt();
                        System.out.println("Enter the month");
                        int h4 = input.nextInt();
                        if(!Integer.toString(h4).matches("[1-12]")) {
                            throw new InvalidDate("Invalid Month. Must be between 1 and 12.");
                        }
                        Periodical tempP = new Periodical(f4,a4,b4,c4,d4,g4,h4);
                        myLibrary.addItem(tempP);
                    }
                    else {
                        System.out.println("Enter either book or periodical");
                    }
                    break;
				case 5: //Remove a title with a given call number
					System.out.println("Please enter call number in format [B/P-ddd-ddd-ddd]: ");
					input.nextLine();
					cN = input.nextLine();
					checkCallNumber(cN);
					myLibrary.removeItem(cN);
					break;
				case 6: //View the list of titles in the library
					System.out.println("All titles: ");
					myLibrary.viewAll();
					break;
				case 7: 
					myLibrary.saveToFile("titles.txt");
					System.out.println("Goodbye~!");
					System.exit(0);
					break;
				}
			} catch (InvalidDate e) {
				System.out.println(e.getMessage());
			} catch (InvalidCallNumber e) {
				System.out.println(e.getMessage());
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
			}
		} while (operation != 7);
		
	}
	
	/**
	 * get digits for switch case
	 * @param input: scanner input
	 * @return op: case number
	 */
	public static int getOperation(Scanner input) {
		int op = 0;
		do {
			System.out.println("\nSelect an operation: ");
			System.out.println(" 1: Find a title with call number");
			System.out.println(" 2: Find titles with a given title");
			System.out.println(" 3: Find titles with a given year");
			System.out.println(" 4: Add a new title");
			System.out.println(" 5: Remove a title with a given call number");
			System.out.println(" 6: View the list of titles in the library");
			System.out.println(" 7: Exit");

			if (input.hasNextInt()) {
				op = input.nextInt();
				if (op >= 1 && op <= 7)
					break;
				else
					System.out.println("Invalid operation. Must be an integer from 1 to 7.");
			} else {
				input.nextLine();
				System.out.println("Invalid operation. Must be an integer.");
			}
		} while (true);
		return op;
	}
	
	/**
	 * checks to see if CN is in proper format
	 * @param cN: string that is being checked
	 * @throws InvalidCallNumber: whill throw if format is not B\P-\\d{3}-\\d{3}-\\d{3}
	 */
	public static void checkCallNumber(String cN) throws InvalidCallNumber {
		if(cN.matches("B-\\d{3}-\\d{3}-\\d{3}") || cN.matches("P-\\d{3}-\\d{3}-\\d{3}")) {
			return;
		} else {
			throw new InvalidCallNumber("Invalid call number – Must be B-ddd-ddd-ddd or P-ddd-ddd-ddd");
		}
	}
	
	/**
	 * Checks to see if date is between 1900-2020
	 * @param y: int that is being checked
	 * @throws InvalidDate: will throw if time is not in between period
	 */
	public static void checkDate(int y) throws InvalidDate{
		if(y > 2020 || y < 1900) {
			throw new InvalidDate("Invalid year – must be from 1900 to 2020");
		}
	}
}
