import java.util.*;
import java.io.*;
public class CheckMovie {

	public static void main(String[] args) {
		Movie[] movieList = new Movie[100];
		int count = 0;
		Scanner input = new Scanner(System.in);
		File file = new File("movie.txt");
		Scanner read = null;
		try {
			read = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
			System.exit(0);
		}
		String title = "", type = "", rating = "";
		boolean error = false;
		while (read.hasNextLine()) {
            type = read.nextLine();
            title = read.nextLine();
            rating = read.nextLine();
            if(type.equals("Action")) {
                Action temp = new Action(rating, title);
                movieList[count] = temp;
                count++;
            } else if(type.equals("Comedy")) {
                Comedy temp = new Comedy(rating, title);
                movieList[count] = temp;
                count++;
            } else if(type.equals("Drama")) {
                Drama temp = new Drama(rating, title);
                movieList[count] = temp;
                count++;
            } else {
            	System.out.println("Error, title was not added: "+title);
            	error = true;
            }
        } 
		if(!error) {
        	System.out.println("All titles was added succesfully.");
        }
		read.close();
		int operation = 0;
		do {
			//try case there to catch any exceptions
			try {
				operation = getOperation(input);
				switch (operation) {
				case 1:
                    System.out.println("Enter the movie title: ");
                    @SuppressWarnings("unused") String foo = input.nextLine();
                    String t = input.nextLine();
                    boolean found = false;
                    for(int i = 0; i < count; i++) {
                        if(t.equals(movieList[i].getTitle())) {
                            System.out.println("Movie found: " + movieList[i].toString());
                            found = true;
                        }
                    }
                    if(!found) {
                        System.out.println("Error: Movie was not found");
                    }
                    break;
                case 2:
                    System.out.println("Enter the rating: ");
                    String r = input.next();
                    checkRating(r);
                    System.out.println("Movies with rating: " + r);
                    for(int i = 0; i < count; i++) {
                        if(r.equals(movieList[i].getRating())) {
                            System.out.println(movieList[i].toString());
                        }
                    }
                    break;
				case 3: // list all movies
					for(int i = 0; i < count; i++) {
                        System.out.println(movieList[i].toString());
                    }
					break;
				case 4: // calc late fee
					System.out.println("Enter movie title: ");
					foo = input.nextLine();
					t = input.nextLine();
					boolean check = false;
					int index = 0;
                    for(int i = 0; i < count; i++) {
                        if(t.equals(movieList[i].getTitle())) {
                            System.out.print("Movie found: " + movieList[i].toString());
                            check = true;
                            index = 0;
                        }
                    }
                    if(check) {
                    	System.out.println("Enter the number of late days: ");
                    	int days = input.nextInt();
                    	double cost = movieList[index].getLateFee(days);
                    	if (cost == 0) {
                    		System.out.println("No fee!");
                    	} else {
                    		System.out.println("You owe: $" + cost);
                    	}
                    }
					break;
				case 5: // exit
					System.out.println("Goodbye~!");
					System.exit(0);
					break;
				}
			} catch (InvalidRatingException e) {
				System.out.println(e.getMessage());
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
			}
		} while (operation != 5);
	}
	public static int getOperation(Scanner input) {
		int op = 0;
		do {
			System.out.println("\nSelect an operation: ");
			System.out.println(" 1: Find a movie");
			System.out.println(" 2: Find movies with a given rating");
			System.out.println(" 3: List all movies");
			System.out.println(" 4: Calculate Late fee");
			System.out.println(" 5: Exit");

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
	public static void checkRating(String r) throws InvalidRatingException { //cant get !r to work???
		if (r.equals("P") || r.equals("G") || r.equals("PG") || r.equals("PG-13") || r.equals("R")) {
			return;
		} else {
			throw new InvalidRatingException("Invalid Rating. Rating must be P/G/PG/PG-13/R");
		}
	}


}
