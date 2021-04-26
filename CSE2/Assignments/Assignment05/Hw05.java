//Input thingy
//2/29/2020
import java.util.*;

public class Hw05{
	public static void main (String [] args){
		Scanner myScan = new Scanner(System.in);
		int input = 0;
		String holder = "";
		int mem = 0;
		
		System.out.print("Input Department Name: ");
		while (true){ //string
			if (myScan.hasNextLine() == true){
				holder = myScan.next();
				break; 
			}
			if (myScan.hasNextInt() == true) {
				input = myScan.nextInt();
				System.out.println("Unaccepted input");	
			}
			System.out.print("Input Department Name: ");
		}
		
		System.out.print("Input Course Number: ");
		while (true){ //int
			if (myScan.hasNextInt() == true){
				input = myScan.nextInt();
				break;
			}
			if (myScan.hasNextLine() == true) {
				System.out.println("Unaccepted input");	
				holder = myScan.next();
			}
			System.out.print("Input Course Number: ");
		}
		
		System.out.print("The # Of Meeting Times: ");
		while (true){ //int
			if (myScan.hasNextInt() == true){
				input = myScan.nextInt();
				break;
			}
			if (myScan.hasNextLine() == true) {
				System.out.println("Unaccepted input");	
				holder = myScan.next();
			}
			System.out.print("The # Of Meeting Times: ");
		}
		
		System.out.print("Class Start Time: ");
		while (true){ //string
			if (myScan.hasNextLine() == true){
				holder = myScan.next();
				break; 
			}
			if (myScan.hasNextInt() == true) {
				input = myScan.nextInt();
				System.out.println("Unaccepted input");	
			}
			System.out.print("Class Start Time: ");
		}	
		
		System.out.print("Instructor Name: ");
		while (true){ //string
			if (myScan.hasNextLine() == true){
				holder = myScan.next();
				break; 
			}
			if (myScan.hasNextInt() == true) {
				input = myScan.nextInt();
				System.out.println("Unaccepted input");	
			}
			System.out.print("Instructor Name: ");
		}		
		
		System.out.print("# Of Students: ");
		while (true){ //int
			if (myScan.hasNextInt() == true ){
				input = myScan.nextInt();
				break;
			}
			if (myScan.hasNextLine() == true) {
				System.out.println("Unaccepted input");	
				holder = myScan.next();
			}
			System.out.print("# Of Students: ");
		}
	}
}