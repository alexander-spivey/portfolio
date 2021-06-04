import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Registration {
	public static void main(String[] args) {
		LinkedList<Student> studentList = new LinkedList<>();
		File file = new File("registration.txt");
		Scanner readFile = null;
		try {
			readFile = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			System.exit(0);
		}
		while (readFile.hasNextLine()) {
			String line = readFile.nextLine();
			Scanner lineReader = new Scanner(line);
			String name = lineReader.next();
			int id = lineReader.nextInt();
			Student currentStudent = new Student(name, id);
			while (lineReader.hasNext()) {
				String number = lineReader.next();
				int cr = lineReader.nextInt();
				currentStudent.addCourse(number, cr);
			}
			lineReader.close();
			studentList.add(currentStudent);
		}
		readFile.close();
		Scanner input = new Scanner(System.in);
		
		// menu
		int operation = 0;
		do {
			// try case there to catch any exceptions
			try {
				operation = getOperation(input);
				switch (operation) {
				case 1: //View all student
					System.out.println("List of all students:");
					System.out.print(" " + studentList.toString());
					break;
				case 2: //Add a new student
					System.out.println("Please enter student name: ");
					String name = input.next();
					System.out.println("Please enter student ID: ");
					int id = input.nextInt();
					Student currentStudent = new Student(name, id);
					studentList.add(currentStudent);
					break;
				case 3: //Add or drop a class
					System.out.println("Please enter student ID: ");
					id = input.nextInt();
					Student temp = new Student("", id);
					if(studentList.contains(temp) != null)
					{
						Student foo = studentList.contains(temp);
						System.out.println(foo.toString());
						int op2 = 0;
						do {
							// try case there to catch any exceptions
							try {
								op2 = getOperation2(input);
								switch (op2) {
								case 1: //Add class
									System.out.println("Please enter course number: ");
									String number = input.next();
									checkCourseNumber(number);
									System.out.println("Please enter course credit");
									int cr = input.nextInt();
									boolean ho1 = foo.addCourse(number, cr);
									if(ho1) {
										System.out.println("Class added!");
									} else {
										System.out.println("Error. Class was not added.");
									}
									break;
								case 2: //Drop class
									//For some reason, even though all boolean says true if deleted, it actually doesn't delete. Think there is issue with class BST delete method.
									System.out.println("Please enter course number: ");
									number = input.next();
									checkCourseNumber(number);
									boolean ho2 = foo.dropCourse(number);
									if(ho2) {
										System.out.println("Class deleted!");
									} else {
										System.out.println("Error. Class was not deleted.");
									}
									break;
								case 3: //Exit
									System.out.println("Exiting minor menu.");
									break;
								}
							} catch (InvalidNumberException e) {
								System.out.println(e.getMessage());
							} catch (InputMismatchException e) {
								System.out.println(e.getMessage());
							}
						} while (op2 != 3);
					} else {
						System.out.println("Error. Student was not found.");
					}
					break;
				case 4: //Sort student list
					mergeSort.mergesort(studentList);
					System.out.println("Sorted!");
					break;
				case 5: //exit
					System.out.println("Bye!");
					System.exit(0);
					break;
				}
			} catch (InvalidNumberException e) {
				System.out.println(e.getMessage());
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
			}
		} while (operation != 5);
	}
	
	private static int getOperation2(Scanner input) {
		int op = 0;
		do {
			System.out.println("\nSelect an operation: ");
			System.out.println(" 1: Add a class");
			System.out.println(" 2: Drop a class");
			System.out.println(" 3: Exit minor menu");

			if (input.hasNextInt()) {
				op = input.nextInt();
				if (op >= 1 && op <= 3)
					break;
				else
					System.out.println("Invalid operation. Must be an integer from 1 to 3.");
			} else {
				input.nextLine();
				System.out.println("Invalid operation. Must be an integer.");
			}
		} while (true);
		return op;
	}

	public static int getOperation(Scanner input) {
		int op = 0;
		do {
			System.out.println("\nSelect an operation: ");
			System.out.println(" 1: View all student");
			System.out.println(" 2: Add a new student");
			System.out.println(" 3: Add or drop a class");
			System.out.println(" 4: Sort student list");
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
	
	public static void checkCourseNumber(String number) throws InvalidNumberException {
		if(number.matches("[A-Z]{2,3}\\d{3}")) {
			return;
		} else {
			throw new InvalidNumberException("Invalid course number.");
		}
	}
}
