import java.util.*;

public class ArrayGames{
	static int[] generate()
	{
		Random rand = new Random();
		int randVal;
		while(true)
		{
			randVal = rand.nextInt(20); //creats a random value until it is between 20 and 10
			if(randVal <= 20 && randVal >= 10)
			{
				break;
			}
		}
		int[] gArray = new int[randVal]; //rand val is now length of loop
		for(int i = 0; i < gArray.length; i++)
		{
			randVal = rand.nextInt(20); //create a value for each part of array
			gArray[i] = randVal;
		}
		return gArray;
	}
	
	static void print(int[] pArray)
	{
		//System.out.println(Arrays.toString(pArray)); cause apparently we can't use premade stuff
		System.out.print("[");
		for(int i = 0; i < pArray.length; i++)
		{
			System.out.print(pArray[i]);
			if(i == pArray.length - 1) //this is to prevent it to be like [1,2,3,] (no extra comma)
			{
				break;
			}
			System.out.print(", ");
		}
		System.out.println("]");
	}
	
	static int[] insert(int[] a1, int[] a2)
	{
		int length = a1.length + a2.length;
		int[] combArray = new int[length]; //make it the combined length of 1 and 2
		Random rand = new Random();
		int randVal = rand.nextInt(a1.length); //decide random spot in a1
		for(int i = 0; i < randVal; i++) //loop through a1 until the spot
		{
			combArray[i] = a1[i];
		}
		int foo = 0;
		for(int j = 0; j < a2.length; j++) //we hit spot so we go to a2
		{
			combArray[j+randVal] = a2[j];
			foo = j;
		}
		for(int k = 0; k < a1.length - randVal; k++) //finished a2, so back to a1 until finished
		{
			combArray[foo+randVal+k+1] = a1[randVal+k];
		}
		return combArray;
	}
	
	static int[] shorten(int[] a1, int elm)
	{
		if(elm > a1.length-1) //if input is too high, just return norm
		{
			return a1;
		} 
		else
		{
			int[] sArray = new int[a1.length - 1];
			int foo = 0;
			for(int i = 0; i < a1.length; i++) //make every value equal
			{
				if(i == elm) //except for the input, basically just skip and just reloop the for statement
				{
					foo++; //to help keep the i value for sArray accurate (without, it leaves a 0)
					continue;
				}
				sArray[i-foo] = a1[i];
			}
			return sArray;
		}
	}
	
	public static void main (String [] args)
	{
		//predetermine needed arrays and values
		int[] gArray1;
		int[] gArray2;
		int[] combArray;
		int[] sArray;
		gArray1 = generate();
		gArray2 = generate();
		Scanner myScan = new Scanner(System.in);
		String response;
		int input;
		
		//perma loop to make sure i get a response
		while(true)
		{
			System.out.print("Woudl you like to [shorten] or [combine] arrays? ");
			response = myScan.nextLine(); //receive response
			if(response.equals("shorten") || response.equals("Shorten"))//code if shorten
			{
				System.out.print("Input - Generated Array 1: ");
				print(gArray1);
				do{ //loop to make sure i get a int and not a string or double
					System.out.print("Enter an index value: ");
					while(!myScan.hasNextInt()){
						System.out.print("Enter a valid index value: ");
						myScan.next();
					}
					input = myScan.nextInt();
				}while(input < 0);
				sArray = shorten(gArray1, input);
				System.out.print("Output - Array 1: ");
				print(sArray);
				break;
			}
			else if(response.equals("combine") || response.equals("Combine"))//code if combine
			{
				System.out.print("Input - Generated Array 1: ");
				print(gArray1);
				System.out.print("Input - Generated Array 2: ");
				print(gArray2);
				combArray = insert(gArray1,gArray2);
				System.out.print("Output - Combined Array 1 and Array 2: ");
				print(combArray);
				break;
			}
			else //this da loop part
			{
				System.out.println("Invalid response, try again with either [shorten] or [combine]");
				continue;
			}
		}
		
		
	}
}