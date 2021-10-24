import java.util.Scanner;
public class Pyramid{
	public static void main(String args[])
	{
		Scanner myScanner = new Scanner( System.in );
		System.out.print("The square side of the pyramid is (input length): ");
		double sqside = myScanner.nextDouble();
		System.out.print("The height of the pyramid is (input height): ");
		double height = myScanner.nextDouble();
		double vol;
		vol = sqside * sqside * height / 3;
		System.out.println("The volume inside the pyramid is: "+vol);
	}
}