
public abstract class Shape implements Comparable<Shape>, Scalable, Cloneable {
	String color;

	// Constructor
	Shape() {
		color = "";
	}

	Shape(String c) {
		this.color = c;
	}

	// main methods
	/**
	 * returns the color of the shape
	 * @return color: color of shape
	 */
	public String getColor() {
		return color;
	}

	/**
	 * sets the color of the shape to c
	 * @param c: the new string for the color spec of shape
	 */
	public void setColor(String c) {
		this.color = c;
	}

	/**
	 * returns a string with all aspects of shape
	 */
	public String toString() {
		String output;
		output = String.format("Color: %s\t", color);
		return output;
	}

	//abstract call to area
	public abstract double getArea();

	//abstract call to perimeter
	public abstract double getPerimeter();

	//abstract call to clone
	public abstract Object clone();

	/**
	 * method to compare object
	 * @return: 1,-1,0 based on comparison
	 */
	public int compareTo(Shape s) {
		if (getArea() == s.getArea()) {
			return 0;
		} else if (getArea() > s.getArea()) {
			return 1;
		} else {
			return -1;
		}
	}

}
