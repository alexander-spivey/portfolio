public class Student {
	// data members
	private String name;
	private int id;
	private String grade;

	// Constructors
	Student() {
		name = "none";
		id = 0;
		grade = "none";
	}

	Student(String n, int i) {
		name = n;
		id = i;
		grade = "none";
	}

	// member methods
	/**
	 * returns value of name
	 * 
	 * @return name: the name of the specific id's
	 */
	public String getName() {
		return name;
	}

	/**
	 * returns value of id
	 * 
	 * @return id: the id associated with the array[i]
	 */
	public int getID() {
		return id;
	}

	/**
	 * returns value of grade
	 * 
	 * @return grade: the grade associated with the array[i]
	 */
	public String getGrade() {
		return grade;
	}

	/**
	 * sets name
	 * 
	 * @param n: the name you want to replace the current arrays[i] with no return
	 *           value
	 */
	public void setName(String n) {
		name = n;
	}

	/**
	 * sets id
	 * 
	 * @param i: the id you want to replace the current arrays[i] with no return
	 *           value
	 */
	public void setID(int i) {
		id = i;
	}

	/**
	 * sets grade
	 * 
	 * @param g: the grade you want to replace the current arrays[i] with no return
	 *           value
	 */
	public void setGrade(String g) {
		grade = g;
	}

	/**
	 * converts all info to string format
	 * 
	 * @param name:  name of student
	 * @param id:    id of student
	 * @param grade: grade of student
	 * @return result: a string that has been mushed together with the previous
	 *         parameters
	 */
	public String toString(String name, int id, String grade) {
		String result = name + "\t" + id + "\t" + grade;
		return result;
	}
}
