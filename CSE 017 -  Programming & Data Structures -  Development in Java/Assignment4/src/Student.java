
public class Student extends Person{
	private int sID;
	private String major;
	private double GPA;

	//Constructor
	Student() {
		super();
		sID = 0;
		major = "";
		GPA = 0.0;
	}
	Student(String n, int a, int sid, String m, double gpa) {
		super(n,a);
		this.sID = sid;
		this.major = m;
		this.GPA = gpa;
	}
	
	//Main methods
	/**
	 * Returns student id
	 * @return sID: student id in double format
	 */
	public double getSID() { return sID; }
	
	/**
	 * Returns student gpa
	 * @return GPA: student gpa in double format
	 */
	public double getGPA() { return GPA; }
	
	/**
	 * Returns a string with major of student
	 * @return major: a string with major of student
	 */
	public String getMajor() { return major; }
	
	/**
	 * Sets the ID of student to sid
	 * @param sid: new student ids
	 */
	public void setSID(int sid) { this.sID = sid; }
	
	/**
	 * Sets the GPA of student to gpa
	 * @param gpa: new student GPA
	 */
	public void setGPA(double gpa) { this.GPA = gpa; }
	
	/**
	 * Sets the major of student to m
	 * @param m: new student major
	 */
	public void setMajor(String m) { this.major = m; }
	
	/**
	 * Returns a string with super class info
	 * @return output + student: new string format with name, age, sid, major, and gpas
	 */
	public String toString() {
		String output = super.toString();
		String student = String.format("Student ID: %-10s\tMajor: %-10s\tGPA: %5.2f\t", sID, major, GPA);
		return output + student;
	}
	
	/**
	 * Method to compare which student has higher gpa
	 * @param p: object reference to person
	 * @return: either 0, 1, -1 based on comparison
	 */
	public int compareTo(Person p) {
		if (getGPA() == ((Student) p).getGPA()) {
			return 0;
		} else if (getGPA() > ((Student) p).getGPA()) {
			return 1;
		} else {
			return -1;
		}
	}
}
