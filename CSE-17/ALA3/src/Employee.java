
import java.util.*;

public class Employee extends Person {
	//data members
	private int id;
	private String title;
	private double as;
	private java.util.Date date;
	
	//constructor
	Employee() {
		super();
		id = 0;
		title = "none";
		as = 0;
		date = new Date();
	}

	Employee(String n, String pN, String a, String e, String t, int i, double as, java.util.Date d) {
		super(n, pN, a, e);
		this.title = t;
		this.id = i;
		this.as = as;
		this.date = d;
	}

	//main methods
	public int getID() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public double getAS() {
		return as;
	}
	
	public java.util.Date getDate(){
		return date;
	}
	
	public void setID(int i) {
		id = i;
	}
	
	public void setTitle(String t) {
		title = t;
	}
	
	public void setAS(double a) {
		as = a;
	}
	
	public void setDate(java.util.Date d)
	{
		date = d;
	}
	
	public String toString() {
		String output = super.toString();
		String employee = String.format("ID: %s\nTitle: %s\nAnnual Salary:  $%.2f\nHire Date: %s\n", id, title, as, date);
		return output+employee;
	}
}
