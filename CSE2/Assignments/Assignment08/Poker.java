import java.util.*;

public class Poker{
	static int largest(int[] a1) 
	 {
		int max = -1;
		for (int i = 0; i < a1.length; i++) 
		{
			if (a1[i] > max){max = a1[i];}  
		}
		return max; 
	 } 
	static boolean full(int[] a1)
	{
		//copy to a2() card number
		int[] a2 = new int[5];
		for(int i = 0; i < 5; i++)
		{
			a2[i] = a1[i]%13;
		}
		//copy to a2() card suit
		int[] a3 = new int[5];
		for(int i = 0; i < 5; i++)
		{
			a3[i] = a1[i]/13;
		}
		//System.out.println("a2"+Arrays.toString(a2));
		//System.out.println("a3"+Arrays.toString(a3));
		
		//check to see if any match in a2
		int counter3 = 0;
		int counter2 = 0;
		int foo[] = {0,0,0};
		for(int j = 0; j < 5; j++) //check to see card value matches
		{
			for(int k = j + 1; k < a2.length; k++)
			{
				for(int l = k + 1; l < a2.length; l++)
				{
					if(a2[j] == a2[k] && a2[j] == a2[l])
					{
						foo[0] = j;
						foo[1] = k;
						foo[2] = l;
						counter3++;
					}
				}
			}
		}
		
		for(int j = 0; j < 5; j++) //check to see card suit matches
		{
			for(int k = j + 1; k < a2.length; k++)
			{
				for(int l = k + 1; l < a2.length; l++)
				{
					if(a3[j] == a3[k] && a3[j] == a3[l])
					{
						foo[0] = j;
						foo[1] = k;
						foo[2] = l;
						counter3++;
					}
				}
			}
		}
		//break if no counter3
		if(counter3 == 0)
		{
			System.out.print("There is not a full house ");
			return false;
		}
		//check for 2 matches
		int hoo;
		for(int b = 0; b < 3; b++)
		{
			hoo = foo[b];
			a2[hoo] = b + 99;
			//System.out.println(Arrays.toString(a2));
		}
		for(int b = 0; b < 3; b++)
		{
			hoo = foo[b];
			a3[hoo] = b + 99;
			//System.out.println(Arrays.toString(a3));
		}
		
		for(int j = 0; j < 5; j++)
		{
			for(int k = j + 1; k < a2.length; k++)
			{
				if(a2[j] == a2[k])
				{
					counter2++;
				}
			}
		}
		for(int j = 0; j < 5; j++)
		{
			for(int k = j + 1; k < a2.length; k++)
			{
				if(a3[j] == a3[k])
				{
					counter2++;
				}
			}
		}
		if(counter2 > 0 && counter3 > 0){
			System.out.print("There is a full house ");
			return true;}
		else{System.out.print("There is not a full house ");
			return false;}
		
	}
	static boolean flush(int[] a1)
	{
		//copy to a2()
		int[] a2 = new int[5];
		for(int i = 0; i < 5; i++)
		{
			a2[i] = a1[i]/13;
		}
		//checking for a flush
		int counter = 0;
		if(a2[0] == a2[1])
		{ counter++; }
		if(a2[0] == a2[2])
		{ counter++; }
		if(a2[0] == a2[3])
		{ counter++; }
		if(a2[0] == a2[4])
		{ counter++; }
		if(counter == 4)
		{
			System.out.print("There is a flush ("+(a2[0])+") ");
			return true;
		}
		else
		{
			System.out.print("There is no flush ");
			return false;
		}
	}
	static boolean pair(int[] a1)
	{
		//copy to a2()
		int[] a2 = new int[5];
		for(int i = 0; i < 5; i++)
		{
			a2[i] = a1[i]%13;
		}
		//check to see if any match in a2
		for(int j = 0; j < 5; j++)
		{
			for(int k = j + 1; k < a2.length; k++)
			{
				if(a2[j] == a2[k])
				{
					System.out.print("There is a pair ("+(a2[j])+") ");
					return true;
				}
			}
		}
		System.out.print("There is no pair ");
		return false;
	}
	static boolean three(int[] a1)
	{
		//copy to a2()
		int[] a2 = new int[5];
		for(int i = 0; i < 5; i++)
		{
			a2[i] = a1[i]%13;
		}
		//check to see if any matches in a2
		for(int j = 0; j < 5; j++)
		{
			for(int k = j + 1; k < a2.length; k++)
			{
				for(int l = k + 1; l < a2.length; l++)
				{
					if(a2[j] == a2[k] && a2[j] == a2[l])
					{
						System.out.print("There is a three of a kind ("+(a2[j])+") ");
						return true;
					}
				}
			}
		}
		System.out.print("There is no three of a kind ");
		return false;
	}
	public static void main (String [] args)
	{
		//establish cards and their id
		int[] cardID;
		cardID = new int[52];
		for(int i = 0; i < 52; i++)
		{
			cardID[i] = i;
		}
		System.out.println("0-12 diamond suit; 13-25 clubs suit; 26-38 hearts suit; 39-51 spades suit");
		//swap ids
		int foo = 0;
		int randVal = 0;
		Random rand = new Random(); 
		for(int j = 0; j < 52; j++)
		{
			randVal = rand.nextInt(52);
			foo = cardID[j];
			cardID[j] = cardID[randVal];
			cardID[randVal] = foo;
		}
		//give player their cards
		int[] playerOne = new int[5];
		int[] playerTwo = new int[5];
		int counter = 0;
		for(int k = 0; k < 5; k++)
		{
			playerOne[k] = cardID[counter];
			counter++;
			playerTwo[k] = cardID[counter];
			counter++;
		}
		//print out player cards
		System.out.print("Player one cards are: ");
		System.out.println(Arrays.toString(playerOne));
		System.out.print("Player two cards are: ");
		System.out.println(Arrays.toString(playerTwo));
		//pair checking
		int point1 = 0, point2 = 0;
		boolean p1pair, p2pair;
		p1pair = pair(playerOne);
		System.out.println("for player one.");
		p2pair = pair(playerTwo);
		System.out.println("for player two.");
		if(p1pair == true){point1+=2;}
		if(p2pair == true){point2+=2;}
		//three checking
		boolean p1three, p2three;
		p1three = three(playerOne);
		System.out.println("for player one.");
		p2three = three(playerTwo);
		System.out.println("for player two.");
		if(p1three == true){point1+=3;}
		if(p2three == true){point2+=3;}
		//flush checking
		boolean p1flush, p2flush;
		p1flush = flush(playerOne);
		System.out.println("for player one.");
		p2flush = flush(playerTwo);
		System.out.println("for player two.");
		if(p1flush == true){point1+=4;}
		if(p2flush == true){point2+=4;}
		//full house check
		boolean p1full, p2full;
		p1full = full(playerOne);
		System.out.println("for player one.");
		p2full = full(playerTwo);
		System.out.println("for player two.");
		if(p1full == true){point1+=5;}
		if(p2full == true){point2+=5;}
		int p1max, p2max;
		if(point2 == 0 && point1 == 0)
		{
			p1max = largest(playerOne);
			p2max = largest(playerTwo);
			if(p1max>p2max)
			{
				System.out.println("Player one had the higher hand");
				point1++;
			}
			else if(p1max<p2max)
			{
				System.out.println("Player two had the higher hand");
				point2++;
			}
			else {System.out.println("Neither players had the higher hand. Players are tied.");}
		}
		if(point1 > point2)
		{
			System.out.println("Winner is player one!");
		}
		else if(point2>point1)
		{
			System.out.println("Winner is player two!");
		}
		else if(point2==point1)
		{
			System.out.println("Players are tied.");
		}
	}
}