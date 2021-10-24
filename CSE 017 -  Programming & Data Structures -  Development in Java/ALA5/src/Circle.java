
public class Circle extends Shape {
	private double radius;
	
	//Constructors
	Circle() {
		super();
		radius = 1.0;
	}

	Circle(String c, double r) {
		super(c);
		this.radius = r;
	}

	/**
	 * Returns radius
	 * @return radius: length of shape r
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Sets raduys to r
	 * @param r: the value of new radius
	 */
	public void setRadius(double r) {
		this.radius = r;
	}

	/**
	 * returns the area of a circle
	 */
	public double getArea() {
		return Math.PI * radius * radius;
	}

	/**
	 * returns the perimetere of a circle
	 */
	public double getPerimeter() {
		return Math.PI * 2 * radius;
	}

	/**
	 * clones the object and returns an exact copy
	 */
	public Object clone() {
		return new Circle(getColor(), radius);
	}
	
	/**
	 * a fucntion to scale the size of object by a factor
	 * @param factor: how much do we want to multiply the current values
	 */
	public void scale(double factor) {
		radius *= factor;
	}

	/**
	 * returns a string of all the values in object shape
	 */
	public String toString() {
		return String.format("%-10s\t%-10s\t%-5.2f\t\t\t%-5.2f\t%-5.2f\t", "Circle", getColor(), radius, getArea(),
				getPerimeter());
	}
}
