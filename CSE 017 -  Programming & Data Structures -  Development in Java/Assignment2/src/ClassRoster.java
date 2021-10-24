public class ClassRoster extends Student {
	// data members
	private String courseNumber;
	private String courseTitle;
	private String courseInstructor;
	private int credits;
	
	public int size;
	public Student[] studentList = new Student[10];

	// Constructor
	ClassRoster() {
		courseNumber = "none";
		courseTitle = "none";
		courseInstructor = "none";
		credits = 0;
		size = 0;
	}

	ClassRoster(String cNumber, String cTitle, String cInstructor, int cr) {
		courseNumber = cNumber;
		courseTitle = cTitle;
		courseInstructor = cInstructor;
		credits = cr;
		size = 0;
	}

	// member methods
	/**
	 * returns course number
	 * 
	 * @return courseNumber: course number
	 */
	public String getCNumber() {
		return courseNumber;
	}

	/**
	 * returns course title
	 * 
	 * @return courseTitle: course title
	 */
	public String getCTitle() {
		return courseTitle;
	}

	/**
	 * returns course instructor
	 * 
	 * @return courseInstructor: course instructor
	 */
	public String getCInstructor() {
		return courseInstructor;
	}

	/**
	 * returns course credits
	 * 
	 * @return credits: credits for class
	 */
	public int getCredits() {
		return credits;
	}

	/**
	 * returns course size
	 * 
	 * @return size: size of class
	 */
	public int getSize() {
		return size;
	}

	/**
	 * sets the course number
	 * 
	 * @param n: value we are modifying it to
	 */
	public void setCNumber(String n) {
		courseNumber = n;
	}

	/**
	 * sets the course title
	 * 
	 * @param t: value we are modifying it to
	 */
	public void setCTitle(String t) {
		courseTitle = t;
	}

	/**
	 * sets the course instructor
	 * 
	 * @param i: value we are modifying it to
	 */
	public void setCInstructor(String i) {
		courseInstructor = i;
	}

	/**
	 * sets the course credits
	 * 
	 * @param c: value we are modifying it to
	 */
	public void setCredits(int c) {
		credits = c;
	}

	/**
	 * linear search for specific id, if found, it returns its location in array if
	 * not, it returns -1
	 * 
	 * @param id: value we are searching for
	 * @return i: array index in studentList or -1 if not found
	 */
	public int findStudentID(int id) {
		for (int i = 0; i < size; i++) {
			int temp = studentList[i].getID();
			if (temp == id) {
				return i;
			}
		}
		int i = -1;
		return i;
	}

	/**
	 * prints off the specific student, if not found, prints out error
	 * 
	 * @param i: index in array studentList
	 */
	public void getStudent(int i) {
		if (i > size || i < 0) {
			System.out.println("Error, index value nto found");
		} else {
			String result = toString(studentList[i].getName(), studentList[i].getID(), studentList[i].getGrade());
			System.out.println(result);
		}
	}

	/**
	 * adds student to studentList
	 * 
	 * @param s: is an object reference to the constructor in class student
	 */
	public void addStudent(Student s) {
		if (size + 1 >= studentList.length) {
			System.out.println("Error: Size greater than Array");
			return;
		}
		studentList[size] = s;
		size++;
	}

	/**
	 * removes student from the array
	 * 
	 * @param id: id of student we are searching for and deleting
	 */
	public void removeStudent(int id) { // worked with Tyler Hugmann to create this method
		Student[] tempArray = new Student[10];
		if (findStudentID(id) == -1) {
			System.out.println("Error, ID was not found");
			return;
		} else {
			int foo = 0;
			// create temporary array
			for (int i = 0; i < size + 1; i++) {
				int ID = studentList[i].getID();
				if (id != ID) {
					tempArray[foo] = studentList[i];
					// String result = toString(tempArray[foo].getName(), tempArray[foo].getID(),
					// tempArray[foo].getGrade());
					// System.out.println(result);
					foo++;
				} else {
					size--;
				}
			}
			// hard-coding array into standard
			for (int j = 0; j < tempArray.length; j++) {
				studentList[j] = tempArray[j];
			}
			studentList[size] = new Student();
		}
	}

	/**
	 * changes the grade of student at specific id
	 * 
	 * @param id:    id of student we are modifying
	 * @param grade: the string that is replacing the student's current grade
	 */
	public void updateStudent(int id, String grade) {
		int sID = findStudentID(id);
		if (sID == -1) {
			System.out.println("Error, ID was not found");
			return;
		} else {
			studentList[sID].setGrade(grade);
		}
	}
}
