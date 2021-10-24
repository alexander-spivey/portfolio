
public abstract class Employee extends Person{
	private int ID;
	private String date;
	
	//Constructor
	Employee() {
		super();
		ID = 0;
		date = "";
	}
	Employee(String n, int a, int id, String d) {
		super(n,a);
		this.ID = id;
		this.date = d;
	}
	
	//Main methods
	/**
	 * Returns the id
	 * @return ID: id of person
	 */
	public double getID() { return ID; }
	
	/**
	 * Return the date of hire
	 * @return date: String of date hired
	 */
	public String getDate() { return date; }
	
	/**
	 * sets the id to new id
	 * @param id: new id int
	 */
	public void setID(int id) { this.ID = id; }
	
	/**
	 * Sets the date to a new string
	 * @param d: new string for date
	 */
	public void setDate(String d) { this.date = d; }
	
	/**
	 * abstract class to get salary
	 * @return: some varaible of double that returns with object salary
	 */
	public abstract double getSalary();
	
	/**
	 * Returns a string with super class info
	 * @return output + employee: new string format with name, age, id, and date
	 */
	public String toString() {
		String output = super.toString();
		String employee = String.format("Employee ID: %s\tHire Date: %s\t", ID, date);
		return output + employee;
	}
	
	/**
	 * Method to compare which person has more salary
	 * @param p: object reference to person
	 * @return: either 0, 1, -1 based on comparison
	 */
	public int compareTo(Person p) {
		if (getSalary() == ((Employee) p).getSalary()) {
			return 0;
		} else if (getSalary() > ((Employee) p).getSalary()) {
			return 1;
		} else {
			return -1;
		}
	}
}
