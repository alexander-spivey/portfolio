import java.util.Scanner;
public class Convert{
	public static void main(String args[])
	{
		Scanner myScanner = new Scanner( System.in );
		System.out.print("Enter the dsitance in meters: ");
		double meters = myScanner.nextDouble();
		double inches;
		inches = meters * 39.37;
		System.out.println(meters+" meters is "+ inches+" inches");
	}
}