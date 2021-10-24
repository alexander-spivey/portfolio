
public class TestShapes {

	public static void main(String[] args) {
		Shape[] shapes = new Shape[8];
		shapes[0] = new Circle("Black", 2.5);
		shapes[1] = new Triangle("Green", 6, 6, 6);
		shapes[2] = new Rectangle("Red", 5, 3);
		shapes[3] = new Hexagon("Yellow", 7);
		shapes[4] = (Circle) (shapes[0].clone());
		shapes[4].scale(2);
		shapes[5] = (Triangle) (shapes[1].clone());
		shapes[6] = (Rectangle) (shapes[2].clone());
		shapes[7] = (Hexagon) (shapes[3].clone());
		shapes[5].setColor("Orange");
		shapes[6] = new Rectangle("Red",10,3);
		shapes[7].scale(1.5);

		// main methods
		System.out.println("Before Sorting");
		System.out.printf("%s\t\t%s\t\t\t\t\t%s\t%s\n", "Type", "Color", "Area", "Perimeter");
		for (int i = 0; i < shapes.length; i++) {
			System.out.println((i+1) + "." + shapes[i].toString());
		}
		//sorts the shapes
		java.util.Arrays.sort(shapes);
		
		System.out.println("\n" + "After Sorting [By Area]");
		System.out.printf("%s\t\t%s\t\t\t\t\t%s\t%s\n", "Type", "Color", "Area", "Perimeter");
		for (int i = 0; i < shapes.length; i++) {
			System.out.println((i+1) + "." + shapes[i].toString());
		}
		
		System.out.printf("Average Perimeter = %-5.2f\t", getAveragePerimeter(shapes));
	}
	
	/**
	 * returns the average perimeter of everything in the array
	 * @param list: call back to array of Shape
	 * @return: the average perimiter of all objects in the array list
	 */
	public static double getAveragePerimeter(Shape[] list) {
		double sum = 0;
		for(int i = 0; i < list.length; i++) {
			sum += list[i].getPerimeter();
		}
		return sum/list.length;
	}
	
}
