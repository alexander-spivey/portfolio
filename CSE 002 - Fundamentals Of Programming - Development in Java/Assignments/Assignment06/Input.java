import java.util.*;

public class Input{
	public static void main (String [] args){
		Scanner myScan = new Scanner(System.in);
		int x = 0;
		int foo = 0;
		int hoo = 0;
		
		do{
			System.out.print("Enter a valid number between 1-9: ");
			while(!myScan.hasNextInt()){
				System.out.print("Enter a valid number between 1-9: ");
				myScan.next();
			}
			x = myScan.nextInt();
			if (x < 0)
			{
				System.out.println("ERROR");
			}
		}while(x< 1 || x >9);
		
		for(int i = 1; i < x+1; i++)
		{
			for(int j = 1; j < x+1; j++)
			{
				if (i < j)
				{
					foo = i;
				}
				else
				{
					foo = j;
				}
				System.out.print(x - foo + 1); 
			}
			for(int j = x - 1; j > 0; j--)
			{
				if (i < j)
				{
					foo = i;
				}
				else
				{
					foo = j;
				}
				System.out.print(x - foo + 1); 
			}	
			System.out.println("");
		}
		for(int i = x - 1; i > 0; i--)
		{
			for(int j = 1; j < x+1; j++)
			{
				if (i < j)
				{
					foo = i;
				}
				else
				{
					foo = j;
				}
				System.out.print(x - foo + 1); 
			}
			for(int j = x - 1; j > 0; j--)
			{
				if (i < j)
				{
					foo = i;
				}
				else
				{
					foo = j;
				}
				System.out.print(x - foo + 1); 
			}	
			System.out.println("");
		}
		
	}
}