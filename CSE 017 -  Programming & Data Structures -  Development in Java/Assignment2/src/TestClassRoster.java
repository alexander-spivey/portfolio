import java.util.*;

/**
 * Program to find, modify, and display roster
 * @author Alexander Spivey
 */
public class TestClassRoster {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		ClassRoster roster = new ClassRoster("CSE017-010", "Programming and Data Structure", "Houria Oudghiri", 3);
		roster.addStudent(new Student("Claire Montana", 1111));
		roster.addStudent(new Student("Jospeh Serin", 2222));
		roster.addStudent(new Student("Abigail Clark", 3333));
		roster.addStudent(new Student("Sarah Verdana", 4444));
		roster.addStudent(new Student("Nicholas Banker", 5555));
		System.out.println("Course Number: " + roster.getCNumber() + "\nCourse Title: " + roster.getCTitle()
				+ "\nCourse Instructor: " + roster.getCInstructor() + "\nCourse Credits: " + roster.getCredits());
		printRoster(roster);

		int operation = 0;
		do {
			operation = getOperation(input);
			switch (operation) {
			case 1:
				System.out.println("Enter student ID: ");
				int tempID = input.nextInt();
				if (roster.findStudentID(tempID) == -1) {
					System.out.println("ID not found!");
				} else {
					roster.getStudent(roster.findStudentID(tempID));
				}
				break;
			case 2:
				System.out.println("Enter student name and ID: ");
				String addName = input.next() + " " + input.next();
				int addID = input.nextInt();
				roster.addStudent(new Student(addName, addID));
				break;
			case 3:
				System.out.println("Enter ID of student that is being removed: ");
				int removeID = input.nextInt();
				roster.removeStudent(removeID);
				break;
			case 4:
				System.out.println("Enter the ID of the student and the grade they are receiving: ");
				int sID = input.nextInt();
				String sGrade = input.nextLine();
				roster.updateStudent(sID, sGrade);
				break;
			case 5:
				printRoster(roster);
				break;
			case 6:
				System.out.println("Exiting program... Goodbye!");
				break;
			}
		} while (operation != 6);
	}

	/**
	 * displays the menu and return the selected operation
	 * 
	 * @param input: Scanner object to read the selected operation
	 * @return value entered by the user
	 */
	public static int getOperation(Scanner input) {
		int op = 0;
		do {
			System.out.println("\nSelect an operation: ");
			System.out.println(" 1: Find a student");
			System.out.println(" 2: Add a new student");
			System.out.println(" 3: Remove an existing student");
			System.out.println(" 4: Update the grade of a student");
			System.out.println(" 5: Print roster");
			System.out.println(" 6: Exit the program");
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
	 * prints off the whole roster in structure
	 * 
	 * @param roster: Reference to ClassRoster no return value
	 */
	public static void printRoster(ClassRoster roster) {
		System.out.println("List of registered students: ");
		for (int i = 0; i < roster.size; i++) {
			System.out.println(roster.studentList[i].getName() + "\t" + roster.studentList[i].getID() + "\t"
					+ roster.studentList[i].getGrade());
		}
	}

}
