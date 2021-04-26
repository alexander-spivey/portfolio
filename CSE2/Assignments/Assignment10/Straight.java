import java.util.*;

public class Straight{
	public static int[] hand()
	{
		int[] cardID = new int[52];
		for(int i = 0; i < 52; i++)
		{
			cardID[i] = i;
		}
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
		return cardID;
	}
	public static int[] draw(int[] cardID)
	{
		int[] playerOne = new int[5];
		int counter = 0;
		for(int k = 0; k < 5; k++)
		{
			playerOne[k] = cardID[k]%13;
		}
		return playerOne;
	}
	public static int search(int[] cardID, int k)
	{
		Arrays.sort(cardID);
		int val = cardID[k];
		return val;
	}
	public static boolean straight()
	{
		int[] cards = hand();
		int[] drawn = draw(cards);
		for(int i = 0; i < 4; i++)
		{
			if(search(drawn,i)+1==search(drawn,i+1))
			{
				continue;
			}
			else
			{
				return false;
			}
		}
		return true;
	}
	public static void main (String[] args)
	{
		int counter = 0;
		for(int i = 0; i < 1000000; i++)
		{
			if(straight()==true)
			{
				counter++;
			}
		}
		System.out.println(counter+"/1000000");
	}
}