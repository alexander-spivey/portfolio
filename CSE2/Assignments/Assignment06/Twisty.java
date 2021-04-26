import java.util.*; //imports library

public class Twisty{
	public static void main (String [] args){
		Scanner myScan = new Scanner(System.in);
		int height = 0;
		int width = 0;
		int foo = 1;
		int hoo = 0;
		int ct = 0;
		int woo = 0;
		int xoo = 0;
		int bt = 0;
		
		do{ //make sure the length is a number between 1-80
			System.out.print("Enter a valid number between 1-80 for length: ");
			while(!myScan.hasNextInt()){
				System.out.print("Enter a valid number between 1-80 for length: ");
				myScan.next();
			}
			width = myScan.nextInt();
		}while(width < 1 || width >80);
		
		do{ //make sure the height is a number between 1-width
			System.out.print("Enter a valid number between 1-"+width+" for height: ");
			while(!myScan.hasNextInt()){
				System.out.print("Enter a valid number between 1-"+width+" for height: ");
				myScan.next();
			}
			height = myScan.nextInt();
		}while(height < 1 || height >width);
		
			
		foo = height;
		for(int i = 1; i < height+1; i++) //repeeat for each column
		{
			for(int j = 1; j < width+1; j++) //repeat for each row
			{
				if(j == hoo+1 || j == woo+2*height) //conditions to get the #
				{
					System.out.print("#");
					woo = j;
					ct = ct + 1;
				}
				if(j == 2*ct*height-hoo)
				{
					System.out.print("#");
				}
				
				if(j == foo || j == xoo+2*height) //conditions to get the / and \
				{
					System.out.print("/");
					xoo = j;
					bt = bt + 1;
				}
				if(j == bt*height+i && bt % 2 == 1)
				{
					System.out.print("\\");
					bt = bt + 1;
				}
				
				else
				{
					System.out.print(" ");
				}
			}
			ct = 0;	//reset values
			bt = 0;
			hoo = hoo + 1;
			foo = foo - 1;
			System.out.println(""); //skip to next line
		}
	}
}