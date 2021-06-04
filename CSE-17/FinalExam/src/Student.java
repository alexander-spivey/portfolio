
public class Student extends Person implements Comparable<Student> {
	private int id;
	private int totalCredits;
	private BST<Course> courses;

	Student(String name, int id) {
		super(name);
		this.id = id;
		courses = new BST<Course>();
	}

	public int getID() {
		return this.id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public int getTotalCredits() {
		return totalCredits;
	}

	public boolean addCourse(String number, int cr) {
		Course temp = new Course(number, cr);
		totalCredits += cr;
		return courses.insert(temp);
	}

	public Course findCourse(String number) {
		Course temp = new Course(number, 0);
		return courses.search(temp);
	}

	public boolean dropCourse(String number) {
		Course temp = new Course(number, 0);
		return courses.delete(temp);
	}

	public String courseList() {
		return courses.toString();
	}

	public String toString() {
		String output = String.format("%-20s%-10d%-10d%s\n", super.toString(), id, totalCredits, courseList());
		return output;
	}

	public int compareTo(Student o) {
		if (getTotalCredits() == o.getTotalCredits()) {
			return 0;
		} else if (getTotalCredits() > o.getTotalCredits()) {
			return 1;
		} else {
			return -1;
		}
	}

	public boolean equals(Object o) {
		if (getID() ==  ((Student) o).getID()) {
			return true;
		} else {
			return false;
		}
	}
}
