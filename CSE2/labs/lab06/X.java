import java.util.*;

public class X{
	public static void main (String [] args){
		Scanner myScan = new Scanner(System.in);
		int height = 0;
		int foo = 1;
		int hoo = 0;
		
		do{
			System.out.print("Enter a valid number between 1-100: ");
			while(!myScan.hasNextInt()){
				System.out.print("Enter a valid number between 1-100: ");
				myScan.next();
			}
			height = myScan.nextInt();
		}while(height < 1 || height >100);
		foo = height - 1;
		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < height; j++)
			{
				if(j == hoo || j == foo)
				{
					System.out.print("#");
				}
// 				if(j == foo)
// 				{
// 					System.out.print(" ");
// 				}
				else
				{
					System.out.print('.');
				}
			}
			hoo = hoo + 1;
			foo = foo - 1;
			System.out.println("");
		}
	}
}