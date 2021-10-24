// Scalable, Cloneable
public abstract class Person implements Comparable<Person>{
	private String name;
	private int age;
	
	//Constructors
	Person() {
		name = "";
		age = 0;
	}
	
	Person(String n, int a) {
		this.name = n;
		this.age = a;
	}
	
	//Main Methods
	/**
	 * Returns name of object
	 * @return name: name of person
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns age of object
	 * @return age: age of person
	 */
	public int getAge() {
		return age;
	}
	
	/**
	 * Set the name to n
	 * @param n: new string name
	 */
	public void setName(String n) {
		this.name = n;
	}
	
	/**
	 * Sets the age to a
	 * @param a: new int age
	 */
	public void setAge(int a) {
		this.age = a;
	}
	
	/**
	 * Returns all variables of people into a string
	 * @return output: String that includes name and age
	 */
	public String toString() {
		String output = String.format("Name: %-10s\tAge: %s\t", name, age);
		return output;
	}
}
