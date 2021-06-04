/**
 * Name: Alexander Spivey
 * Date: 9/26
 */
import java.util.*;
import java.io.*;

public class Test {
	//main method
	public static void main(String[] args) {
		//creating array and testing if file exists
		Student[] studentList = new Student[100];
		Scanner input = new Scanner(System.in);
		File file = new File("students.txt");
		Scanner readFile = null;
		try {
			readFile = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
			System.exit(0);
		}
		
		//copying files information into studentList
		String name = "";
		int id = 0, studentCount = 0;
		double gpa = 0;
		System.out.println("Students being added: ");
		while (readFile.hasNextLine()) {
			name = readFile.next() + " " + readFile.next();
			id = readFile.nextInt();
			checkID(id);
			gpa = readFile.nextDouble();
			checkGPA(gpa);
			Student temp = new Student(name, id, gpa);
			studentList[studentCount++] = temp;
			System.out.println((studentCount)+"\t" + studentList[studentCount-1].toString(studentList[studentCount-1].getName(),
					studentList[studentCount-1].getID(), studentList[studentCount-1].getGPA()));
		}
		readFile.close();

		//menu
		int operation = 0;
		do {
			//try case there to catch any exceptions
			try {
				operation = getOperation(input);
				switch (operation) {
				case 1: // search id
					System.out.println("Enter ID: ");
					id = input.nextInt();
					checkID(id);
					int index = findID(studentList, id, studentCount);
					if (index == -1) {
						System.out.println("Student ID was not found.");
					} else {
						System.out.println("Student found: " + studentList[index].toString());
					}
					break;
				case 2: // add student
					System.out.println("Enter student's name, id, gpa: ");
					name = input.next() + " " + input.next();
					id = input.nextInt();
					checkID(id);
					gpa = input.nextDouble();
					checkGPA(gpa);
					studentList[studentCount] = new Student(name, id, gpa);
					studentCount++;
					break;
				case 3: // remove student
					System.out.println("Enter id of student that is being removed: ");
					id = input.nextInt();
					checkID(id);
					removeStudent(studentList, id, studentCount);
					studentCount--;
					System.out.println("Student has been removed.");
					break;
				case 4: // view all
					for (int i = 0; i < studentCount; i++) {
						System.out.println((i+1)+"\t"+studentList[i].toString(studentList[i].getName(), studentList[i].getID(),
								studentList[i].getGPA()));
					}
					break;
				case 5: // exit
					saveData(studentList, studentCount, file);
					System.out.println("Exiting the program...");
					break;
				}
			} catch (InvalidIDException e) {
				System.out.println(e.getMessage());
			} catch (InvalidGPAException e) {
				System.out.println(e.getMessage());
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
			}
		} while (operation != 5);
	}

	/**
	 * Method to find id, if found returns array spot, or -1
	 * 
	 * @param studentList:  an array of Student[]
	 * @param id:           value we are searching for
	 * @param studentCount: number of ids we should look for
	 * @return i: location in array or (-1) if not found
	 */
	public static int findID(Student[] studentList, int id, int studentCount) {
		for (int i = 0; i < studentCount; i++) {
			if (studentList[i].getID() == id) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Method to print menu and in take operation
	 * 
	 * @param input: A scanner object to in take input
	 * @return op: a value associated with a case
	 */
	public static int getOperation(Scanner input) {
		int op = 0;
		do {
			System.out.println("\nSelect an operation: ");
			System.out.println(" 1: Find a student ID");
			System.out.println(" 2: Add a new student");
			System.out.println(" 3: Remove a student");
			System.out.println(" 4: View all students");
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

	/**
	 * Check if ID is in proper format
	 * 
	 * @param id: id we are testing
	 * @throws InvalidIDException: error message if id is not in proper format
	 */
	public static void checkID(int id) throws InvalidIDException {
		Integer ID = id;
		if (!ID.toString().matches("\\d{4}")) {
			throw new InvalidIDException("Input ERROR: Invalid ID. ID is only 4 digits long");
		}
	}

	/**
	 * Check if gpa is in proper format
	 * 
	 * @param gpa: id we are testing
	 * @throws InvalidGPAException: error message if gpa is not in proper format or
	 *                              value
	 */
	public static void checkGPA(double gpa) throws InvalidGPAException {
		if (gpa < 0.0 || gpa > 4.0) {
			throw new InvalidGPAException("Input ERROR: Invalid GPA. Value must be between 0.00 and 4.00");
		}
	}

	/**
	 * Modifies student.txt w/ the modifications in program
	 * 
	 * @param studentList:  array of object Student[]
	 * @param studentCount: total number of students
	 * @param file:         reference to student.txt
	 */
	public static void saveData(Student[] studentList, int studentCount, File file) {
		PrintWriter writeFile = null;
		try {
			writeFile = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		for (int i = 0; i < studentCount; i++) {
			writeFile.println(
					studentList[i].toString(studentList[i].getName(), studentList[i].getID(), studentList[i].getGPA()));
		}
		writeFile.close();
	}

	/**
	 * Removes student from studentList
	 * 
	 * @param studentList:  array of object Student[]
	 * @param id:           id number that is being searched for and being removed
	 *                      if found
	 * @param studentCount: total number of students
	 */
	public static void removeStudent(Student[] studentList, int id, int studentCount) {
		Student[] tempArray = new Student[100];
		if (findID(studentList, id, studentCount) == -1) {
			System.out.println("Error, ID was not found");
			return;
		} else {
			int foo = 0;
			// create temporary array
			for (int i = 0; i < studentCount + 1; i++) {
				int ID = studentList[i].getID();
				if (id != ID) {
					tempArray[foo] = studentList[i];
					foo++;
				} else {
					studentCount--;
				}
			}
			// hard-coding array into standard
			for (int j = 0; j < tempArray.length; j++) {
				studentList[j] = tempArray[j];
			}
			studentList[studentCount] = new Student();
		}
	}
}
