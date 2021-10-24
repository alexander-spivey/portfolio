
public class Test {

	public static void main(String[] args) {
		Person[] personList = new Person[10];
		personList[0] = new Student("Lucy Treston", 20, 12345, "CSE", 3.75);
		personList[1] = new Student("Mark Brown", 18, 12344, "ISE", 3.50);
		personList[2] = new FullTimeEmployee("Jerry Zurcker", 25, 3333333, "03/10/2017", 500000);
		personList[3] = new PartTimeEmployee("Sharon Luft", 22, 6666666, "01/01/2010", 32.0, 100);
		personList[4] = new Student("Emma Packard", 19, 12355, "CSB", 3.0);
		personList[5] = new Student("Felix Hirpara", 22, 55123, "CSE", 2.75);
		personList[6] = new PartTimeEmployee("Jade Farrar ", 29, 1111111, "07/22/2012", 22.0, 45);
		personList[7] = new Student("Junita Stoltzman", 21, 44123, "ISE", 2.5);
		personList[8] = new PartTimeEmployee("Brian Lin", 31, 7777777, "02/01/2014", 35.0, 31);
		personList[9] = new FullTimeEmployee("Alicia Bubash", 35, 5555555, "08/01/2018", 125000);
		
		//Displaying all items of array personList
		for(int i = 0; i < personList.length; i++) {
			System.out.println(personList[i].toString());
		}
		printSortedStudents(personList);
		printSortedEmployees(personList);
		//System.out.println("Working");
	}
	
	//ALL THE SORT AND PRINT METHODS WERE MADE TOGETHER WITH TYLER HAGMANN

	/**
	 * Method to sort list
	 * @param list: reference to array list
	 * @param start: where to start
	 * @param end: where to end
	 */
	public static void sort(Person[] list, int start, int end) {
        Student[] students = new Student[5];
        Employee[] employees = new Employee[5];
        if (list[0] instanceof Student) {
            students = (Student[]) list;
        } else {
            employees = (Employee[]) list;
        }
        for (int i = 0; i < end - 1; i++) {
            int min = i;
            if (list[i] instanceof Student) {
                for (int j = i + 1; j < end; j++) {
                    if (students[j].getGPA() < students[min].getGPA()) {
                        min = j;
                    }
                }
                Student temp = students[min];
                Person temp1 = list[min];
                students[min] = students[i];
                list[min] = list[i];
                students[i] = temp;
                list[i] = temp1;
            } else {
                for (int j = i + 1; j < end; j++) {
                    if (employees[j].getSalary() < employees[min].getSalary()) {
                        min = j;
                    }
                }
                Employee temp = employees[min];
                Person temp1 = list[min];
                employees[min] = employees[i];
                list[min] = list[i];
                employees[i] = temp;
                list[i] = temp1;
            }
        }
    }
	
	/**
	 * Prints a list of all sorted students
	 * @param list: reference to array list
	 */
	public static void printSortedStudents(Person[] list) {
        Student[] students = new Student[5];
        int ctr = 0;
        for (int i = 0; i < list.length; i++) {
            if (list[i] instanceof Student) {
                students[ctr] = (Student) list[i];
                ctr++;
            }
        }
        sort(students, 0, students.length);
        System.out.println("\nList of students:");
        for(int i = 0; i < students.length; i++) {
            System.out.println(students[i].toString());
        }
    }

	/**
	 * Prints a list of all sorted employees
	 * @param list: reference to array list
	 */
    public static void printSortedEmployees(Person[] list) {
        Employee[] employees = new Employee[5];
        int ctr = 0;
        for (int i = 0; i < list.length; i++) {
            if (list[i] instanceof Employee) {
                employees[ctr] = (Employee) list[i];
                ctr++;
            }
        }
        sort(employees, 0, employees.length);
        System.out.println("\nList of employees:");
        for(int i = 0; i < employees.length; i++) {
            System.out.println(employees[i].toString());
        }
    }
}
