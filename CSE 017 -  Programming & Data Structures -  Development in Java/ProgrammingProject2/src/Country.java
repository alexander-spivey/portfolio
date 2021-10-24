
public class Country implements Comparable<Country> {
	String code;
	String name;
	double area;

	Country(String c, String n, double a) {
		this.code = c;
		this.name = n;
		this.area = a;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public double getArea() {
		return area;
	}

	public void setCode(String c) {
		this.code = c;
	}

	public void setName(String n) {
		this.name = n;
	}

	public void setArea(double a) {
		this.area = a;
	}

	public boolean equals(Object o) {
		if (((Country) o).getName().equals(this.getName())) {
			return true;
		}
		return false;
	}

	public int compareTo(Country c) {
		if (this.getArea() > c.getArea()) {
			return 1;
		} else if (this.getArea() < c.getArea()) {
			return -1;
		} else {
			return 0;
		}

	}

	public String toString() {
		String output = String.format("%-10s%-55s%.1f", code, name, area);
		return output;
	}

}
