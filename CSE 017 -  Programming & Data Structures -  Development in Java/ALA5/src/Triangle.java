
public class Triangle extends Shape {
	private double side1, side2, side3;

	// Constructor
	Triangle() {
		super();
		side1 = 1;
		side2 = 1;
		side3 = 1;
	}

	Triangle(String c, double s1, double s2, double s3) {
		super(c);
		this.side1 = s1;
		this.side2 = s2;
		this.side3 = s3;
	}

	/**
	 * returns the length of side1
	 * @return side1: side1 of triangle length
	 */
	public double getSide1() {
		return side1;
	}

	/**
	 * returns the length of side2
	 * @return side2 side2 of triangle length
	 */
	public double getSide2() {
		return side2;
	}

	/**
	 * returns the length of side3
	 * @return side3: side3 of triangle length
	 */
	public double getSide3() {
		return side3;
	}

	/**
	 * sets the length of side to s1
	 * @param s1: new length of side
	 */
	public void setSide1(double s1) {
		this.side1 = s1;
	}

	/**
	 * sets the length of side to s2
	 * @param s1: new length of side
	 */
	public void setSide2(double s2) {
		this.side1 = s2;
	}

	/**
	 * sets the length of side to s3
	 * @param s1: new length of side
	 */
	public void setSide3(double s3) {
		this.side1 = s3;
	}

	/**
	 * returns the area of the triangle
	 */
	public double getArea() {
		double p = (side1 + side2 + side3) / 2;
		double area = Math.sqrt( p * (p - side1) * (p - side2) * (p - side3));
		return area;
	}

	/**
	 * returns the perimeter of the triangle
	 */
	public double getPerimeter() {
		return side1 + side2 + side3;
	}

	/**
	 * scales the dimensions of the triangle
	 * @param factor: how much we want to increase the traingle sides by
	 */
	public void scale(double factor) {
		side1 *= factor;
		side2 *= factor;
		side3 *= factor;
	}

	/**
	 * creates a clone of the triangle with the same specs
	 */
	public Object clone() {
		return new Triangle(getColor(), side1, side2, side3);
	}

	/**
	 * returns a string of all dimenions and specs of triangle
	 */
	public String toString() {
		return String.format("%-10s\t%-10s\t%-5.2f\t%-5.2f\t%-5.2f\t%-5.2f\t%-5.2f\t", "Triangle", getColor(), side1,
				side2, side3, getArea(), getPerimeter());
	}
}
