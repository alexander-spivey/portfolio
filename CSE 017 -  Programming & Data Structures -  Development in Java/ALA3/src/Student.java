
public class Student extends Person{
	//data members
	private int id;
	private String major;
	private double gpa;
	
	//Constructors
	Student(){
		super(); // default constructor in Person
		id = 0;
		major = "none";
		gpa = 0.0;
	}
	
	Student(String n, String pn, String a, String e, String m, int i, double g){
		super(n, pn, a, e); // 2nd constructor in Person
		this.id = i;
		this.major = m;
		this.gpa = g;
	}
	
	//methods
	public int getID() {
		return id;
	}
	
	public String getMajor() {
		return major;
	}
	
	public double getGPA() {
		return gpa;
	}
	
	public void setID(int i) {
		id = i;
	}
	
	public void setMajor(String m) {
		major = m;
	}
	
	public void setGPA(double g) {
		gpa = g;
	}
	
	public String toString() {
		String output = super.toString();
		String student = String.format("ID: %s\nMajor: %s\nGPA: %.2f\n", id, major, gpa);
		return output + student;
	}
}
