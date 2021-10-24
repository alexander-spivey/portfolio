
public class PartTimeEmployee extends Employee{
	private double wage;
	private double hours;
	
	//Constructors
	PartTimeEmployee() {
		super();
		wage = 0;
		hours = 0;
	}
	PartTimeEmployee(String n, int a, int id, String d, double w, double h) {
		super(n, a, id, d);
		wage = w;
		hours = h;
	}
	
	//Main methods
	/**
	 * returns the wage
	 * @return wage: double amount of wage of object
	 */
	public double getWage() { return wage; }
	/**
	 * returns the hours
	 * @return wage: double amount of hours of object
	 */
	public double getHours() { return hours; }
	//setter methods
	public void setWage(double w) { wage = w; }
	public void setHours(double h) { hours = h; }
	/**
	 * returns the salary of part time
	 * @return: wage*hours
	 */
	public double getSalary() { return wage * hours; }
	
	//tostring method
	public String toString() {
		String output = super.toString();
		String ptemployee = String.format("PartT  Salary: $%-5.2f/year \tWage:  %5.2f\tHours Worked: %5.2f\t", getSalary(), wage, hours);
		return output + ptemployee;
	}
	
}

