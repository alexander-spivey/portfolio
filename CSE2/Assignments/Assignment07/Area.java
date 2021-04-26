import java.util.*;

public class Area{
	static double Circle(double i){ //code to find the area of circle
		double prod, pi = 3.1415; 
		prod = pi*i*i; //calculation to find area
		System.out.println(prod); //print data
		return prod;
	}
	static double Square(double i){ //code to find the area of a square
		double prod;
		prod = i*i; //area of square
		System.out.println(prod);
		return prod;
	}
	static double Triangle(double i, double j){ //find area of triangle
		double prod;
		prod = 0.5*i*j; //area of a triangle
		System.out.println(prod);
		return prod;
	}
	static double Input(){ //checks input and calls the correct method
		String input;
		double x, y;
		Scanner myScanner = new Scanner(System.in);
		System.out.print("What shape would you like to calculate the area of? [Circle, Triangle, Square]: ");
		input = myScanner.nextLine();
		if (input.equals("Circle")){ //if response is "Circle"
			System.out.print("What is the radius?: ");
			x = myScanner.nextDouble();
			Circle(x);
			return 1;
		}
		if (input.equals("Square")){ //if response is "square"
			System.out.print("What is the length of one side?: ");
			x = myScanner.nextDouble();
			Square(x);
			return 1;
		}
		if (input.equals("Triangle")){ //if response is "triangle"
			System.out.print("What is the length of the base?: ");
			x = myScanner.nextDouble();
			System.out.print("What is the length of the height?: ");
			y = myScanner.nextDouble();
			Triangle(x,y);
			return 1;
		}
		else
		{
			System.out.println("Invalid shape"); //if response is none of the above
			return 0;
		}
	}
	public static void main (String [] args){
		while(true)
		{
			if(Input() == 1) //have the input method return a value of 1 or 0, if 0, it repeats
			{
				break;
			}
		}
		System.out.println("Thank you for using this useless program instead of a calculator or Google :D")
	}
}