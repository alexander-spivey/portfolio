public class Student {
	// data members
	private String name;
	private int id;
	private double gpa;

	// Constructors
	Student() {
		name = "";
		id = 0;
		gpa = 0.0;
	}

	Student(String n, int i, double g) {
		name = n;
		id = i;
		gpa = g;
	}

	// member methods
	/** returns value of name
	* @return name: the name of the specific id's
	*/
	public String getName() {
		return name;
	}

	/** returns value of id
	* @return id: the id associated with the array[i]
	*/
	public int getID() {
		return id;
	}

	/** returns value of gpa
	* @return gpa: the gpa associated with the array[i]
	*/
	public double getGPA() {
		return gpa;
	}

	/** sets name
	* @param n: the name you want to replace the current arrays[i] with
	* no return value
	*/
	public void setName(String n) {
		name = n;
	}

	/** sets id
	* @param i: the id you want to replace the current arrays[i] with
	* no return value
	*/
	public void setID(int i) {
		id = i;
	}

	/** sets gpa
	* @param g: the gpa you want to replace the current arrays[i] with
	* no return value
	*/
	public void setGPA(double g) {
		gpa = g;
	}

	/** converts all info to string format
	* @param name: name of student
	* @param id: id of student
	* @param gpa: gpa of student
	* @return result: a string that has been mushed together with the previous parameters
	*/
	public String toString(String name, int id, double gpa) {
		String result = name + "\t" + id + "\t" + gpa;
		return result;
	}
}
