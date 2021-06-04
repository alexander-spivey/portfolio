
public class Rectangle extends Shape {
	private double length, width;

	// Constructor
	Rectangle() {
		super();
		length = 1;
		width = 1;
	}

	Rectangle(String c, double l, double w) {
		super(c);
		this.length = l;
		this.width = w;
	}

	// main methods
	/**
	 * returns the length of rectangle
	 * @return length: the length of rectangle
	 */
	public double getLength() {
		return length;
	}

	/**
	 * returns the width of rectangle
	 * @return width: the width of the rectangle
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * sets the length of rectangle to l
	 * @param l: new length of rectangle
	 */
	public void setLength(double l) {
		this.length = l;
	}

	/**
	 * sets the width of rectangle to w
	 * @param w: new width of rectangle
	 */
	public void setWidth(double w) {
		this.width = w;
	}

	/**
	 * returns the area of the rectangle
	 */
	public double getArea() {
		return length * width;
	}

	/**
	 * returns the perimeter of the rectangle
	 */
	public double getPerimeter() {
		return (length * 2) + (width * 2);
	}

	/**
	 * increase the dimensions of the object by a factor
	 * @param factor: how much the object will be increased by
	 */
	public void scale(double factor) {
		length *= factor;
		width *= factor;
	}

	/**
	 * clones an exact copy of the same dimmension of the rectangle
	 */
	public Object clone() {
		return new Rectangle(getColor(), length, width);
	}

	/**
	 * returns a string with all specifications of the rectangle
	 */
	public String toString() {
		return String.format("%-10s\t%-10s\t%-5.2f\t%-5.2f\t\t%-5.2f\t%-5.2f\t", "Rectangle", getColor(), length, width,
				getArea(), getPerimeter());
	}
}
