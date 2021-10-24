import java.util.Scanner;
public class TestStudent {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("Enter the number of students: ");
		int students = input.nextInt();
		Student[] studentList = new Student[students];
		for (int i = 0; i < students; i++) {
			System.out.println("Enter student information (name id gpa): ");
			String name = input.next();
			int id = input.nextInt();
			double gpa = input.nextDouble();
			studentList[i] = new Student(name, id, gpa);
		}
		int operation = 0;
		do {
			operation = getOperation(input);
			switch (operation) {
			case 1:
				System.out.printf("\nEnter student ID: ");
				int id = input.nextInt();
				findStudentID(studentList, id, students);
				break;
			case 2:
				displayAll(studentList, students);
				break;
			case 3:
				double avg = avgGPA(studentList, students);
				System.out.printf("Average GPA: %.2f", avg);
				break;
			case 4:
				sortGPA(studentList);
				displayAll(studentList, students);
				break;
			case 5:
				System.out.println("Exiting the program...");
				break;
			}
		} while (operation != 5);

	}
	
	/** displays the menu and return the selected operation
	* @param input: Scanner object to read the selected operation
	* @return value entered by the user
	*/
	public static int getOperation(Scanner input) {
		int op = 0;
		do {
			System.out.println("\nSelect an operation: ");
			System.out.println(" 1: Find student");
			System.out.println(" 2: Display all students");
			System.out.println(" 3: Average GPA");
			System.out.println(" 4: Sort GPAs");
			System.out.println(" 5: Exit");

			if (input.hasNextInt()) {
				op = input.nextInt();
				if (op >= 1 && op <= 5)
					break;
				else
					System.out.println("Invalid operation. Must be an integer from 1 to 9.");
			} else {
				input.nextLine();
				System.out.println("Invalid operation. Must be an integer.");
			}
		} while (true);
		return op;
	}
	
	/** array to display a singular student's id
	* @param studentList: a Student[] array
	* @param i: student value in array that is being displayed
	* no return value
	*/
	public static void displaySingle(Student[] studentList, int i) {
		int ID = studentList[i].getID();
		double GPA = studentList[i].getGPA();
		String name = studentList[i].getName();
		System.out.println(name + "\t" + ID + "\t" + GPA);
		return;
	}

	/** uses linear search to find a single student based on id
	* @param studentList: a Student[] array
	* @param students: number of students
	* @param id: student id
	* no return value
	*/
	public static void findStudentID(Student[] studentList, int id, int students) {
		for (int i = 0; i < students; i++) {
			int temp = studentList[i].getID();
			if (temp == id) {
				System.out.println("ID " + id + " was found");
				displaySingle(studentList, i);
				return;
			}
		}
		System.out.println("ID " + id + " was not found");
	}

	/** a print all to the student array
	* @param studentList: a Student[] array
	* @param students: number of students
	* no return value
	*/
	public static void displayAll(Student[] studentList, int students) {
		for (int i = 0; i < students; i++) {
			displaySingle(studentList, i);
		}
		return;
	}

	/** fills in the input arrays with data entered by the user
	* @param studentList: a Student[] array
	* @param students: number of students
	* @return avg: all students gpa added and then averaged out
	*/
	public static double avgGPA(Student[] studentList, int students) {
		double sumGPA = 0;
		for (int i = 0; i < students; i++) {
			sumGPA += studentList[i].getGPA();
		}
		double avg = sumGPA / students;
		return avg;
	}

	/** sorts GPA by ascending
	* @param studentList: a Student[] array
	* no return value
	*/
	public static void sortGPA(Student[] studentList) {
		for(int i = 1; i < studentList.length; i++)
		{
			Student iStudent = studentList[i];
			while(i>0 && iStudent.getGPA() < (studentList[i-1].getGPA()))
			{
				studentList[i] = studentList[i-1];
				i--;
			}
			studentList[i] = iStudent;
		}
	}
}
