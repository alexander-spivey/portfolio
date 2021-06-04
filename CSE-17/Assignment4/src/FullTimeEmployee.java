
public class FullTimeEmployee extends Employee {
	private double aS;
	
	//Constructor
	FullTimeEmployee() {
		super();
		aS = 0;
	}
	FullTimeEmployee(String n, int a, int id, String d, double as) {
		super(n, a, id, d);
		this.aS = as;
	}
	
	//Main methods
		//getter methods
	public double getSalary() { return aS; }
		//setter methods
	public void setSalary(int as) { this.aS = as; }  
		//toString method
	public String toString() {
		String output = super.toString();
		String ftemployee = String.format("Annual Salary: $%5.2f/year", aS);
		return output + ftemployee;
	}
}

