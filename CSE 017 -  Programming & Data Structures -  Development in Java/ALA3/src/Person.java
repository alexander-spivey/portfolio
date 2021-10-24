
public class Person {
	// data members
	private String name;
	private String phoneNumber;
	private String address;
	private String email;

	// Constructor
	Person() {
		name = "none";
		phoneNumber = "none";
		address = "none";
		email = "none";
	}

	Person(String n, String pN, String a, String e) {
		this.name = n;
		this.phoneNumber = pN;
		this.address = a;
		this.email = e;
	}

	// main methods
	public String getName() {
		return name;
	}

	public String getPN() {
		return phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public String getEmail() {
		return email;
	}

	public void setName(String n) {
		name = n;
	}

	public void setPN(String PN) {
		phoneNumber = PN;
	}

	public void setAddress(String a) {
		address = a;
	}

	public void setEmail(String e) {
		email = e;
	}

	public String toString() {
		String output;
		output = String.format("Name: %s\nAddress: %s\nPhone:  %s\nEmail:  %s\n", name, address, phoneNumber, email);
		return output;
	}
}
