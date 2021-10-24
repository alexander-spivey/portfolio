public class Country implements Comparable<Country> {
	//Data memebers
	String code;
	String name;
	double area;

	//Constructor
	Country(String c, String n, double a) { // constructor
		this.code = c;
		this.name = n;
		this.area = a;
	}

	//Getters
	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public double getArea() {
		return area;
	}

	//Setters
	public void setCode(String c) {
		this.code = c;
	}

	public void setName(String n) {
		this.name = n;
	}

	public void setArea(double a) {
		this.area = a;
	}

	/**
	 * Checks to see if object names are equal
	 * @param o: Object reference
	 * @return whether if true or false
	 */
	public boolean equals(Object o) {
		Country a = (Country) o;
		if (a.getName().equals(this.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * Compares country names
	 * @param c: Country reference
	 */
	public int compareTo(Country c) {
		int compare = this.getName().compareTo(c.getName());
		if (compare == 0) {
			return 0;
		} else if (compare > 0) {
			return 1;
		} else {
			return -1;
		}
	}

	/**
	 * toSting for country
	 */
	public String toString() {
		String output = String.format("%-10s%-50s%.1f\n", this.code, this.name, this.area);
		return output;
	}
}
