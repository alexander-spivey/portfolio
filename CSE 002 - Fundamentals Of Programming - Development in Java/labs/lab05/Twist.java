//Twist
//2/28/2020
import java.util.*;

public class Twist{
	public static void main (String [] args){
		Scanner myScan = new Scanner(System.in);
		int input = 0;
		String holder = "";
		int mem = 0;
		System.out.print("Input a number: ");
		while (true){
			if (myScan.hasNextInt() == true ){
				input = myScan.nextInt();
				break;
			}
			if (myScan.hasNextLine() == true) {
				System.out.println("Unaccepted input");	
				holder = myScan.nextLine();
			}
			System.out.print("Input a number: ");
		}
		mem = input;
		while (input>0)
		{
			if (input>0){
				System.out.print("\\");
				input -= 1;	
			}
			if (input>0){
				System.out.print(" ");
				input -= 1;	
			}
			if (input>0){
				System.out.print("/");
				input -= 1;	
			}
		}
		input = mem;
		System.out.println("");
		while (input>0)
		{
				if (input>0){
				System.out.print(" ");
				input -= 1;	
			}
			if (input>0){
				System.out.print("/");
				input -= 1;	
			}
			if (input>0){
				System.out.print(" ");
				input -= 1;	
			}
		}
		input = mem;
		System.out.println("");
		while (input>0)
		{
				if (input>0){
				System.out.print("/");
				input -= 1;	
			}
			if (input>0){
				System.out.print(" ");
				input -= 1;	
			}
			if (input>0){
				System.out.print("\\");
				input -= 1;	
			}
		}
			
	}
}