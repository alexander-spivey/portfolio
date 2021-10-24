
public class Hexagon extends Shape {
	private double side;

	// Constructor
	Hexagon() {
		super();
		side = 1;
	}

	Hexagon(String c, double s) {
		super(c);
		this.side = s;
	}

	/**
	 * Return the side of object
	 * @return side: objects length of its dimmension
	 */
	public double getSide() {
		return side;
	}

	/**
	 * Sets the side of the object to s
	 * @param s: length of side now
	 */
	public void setSide(double s) {
		this.side = s;
	}

	/**
	 * returns area of a hexagon
	 */
	public double getArea() {
		double area = Math.sqrt(3) * 3 / 2 * side * side;
		return area;
	}

	/**
	 * returns the perimeter of a hexagon
	 */
	public double getPerimeter() {
		return 6 * side;
	}

	/**
	 * a function to scale length of side
	 * @param factor: how much we want to increase the side by
	 */
	public void scale(double factor) {
		side *= factor;
	}

	/**
	 * clones the object of hexagon with same color and specs
	 */
	public Object clone() {
		return new Hexagon(getColor(), side);
	}

	/**
	 * returns a string with all dimensions of the hexagon
	 */
	public String toString() {
		return String.format("%-10s\t%-10s\t%-5.2f\t\t\t%-5.2f\t%-5.2f\t", "Hexagon", getColor(), side, getArea(),
				getPerimeter());
	}
}
