import java.util.*;

public class lab09{
	static void search(int[] a1, int val)
	{
		for (int i = 0; i < a1.length; i++) 
		{
			if (a1[i] == val)
			{
				System.out.println("Value "+val+" was found in the list");
				return;
			}  
		}
		System.out.println("Value "+val+" was not found in the list");
		int foo = 0;
		int j = a1.length-1;
		while(val > a1[j])
		{
			foo = j;
			j--;
		}
		if(foo == 0)
		{
			System.out.println("The number above the key was none");
		} else if (foo > 0) {
			System.out.println("The number above the key was "+a1[foo-1]);
		}
		if (val < a1[a1.length-1])
		{
			System.out.println("The number below the key was none");
		} else if (val > a1[a1.length-1]) {
			System.out.println("The number below the key was "+a1[foo]);
		}
		return;
	}
	
	static int min(int[] a1) 
	{
		int min = 999999999;
		for (int i = 0; i < a1.length; i++) 
		{
			if (a1[i] < min){min = a1[i];}  
		}
		return min; 
	}
	 
	static int max(int[] a1) 
	{
		int max = -1;
		for (int i = 0; i < a1.length; i++) 
		{
			if (a1[i] > max){max = a1[i];}  
		}
		return max; 
	}
	
	public static void main (String [] args)
	{
		int[] array1 = new int[5000];
		int[] array2 = new int[5000];
		int randVal, min1, max1, min2, max2, input;
		
		Random rand = new Random(); 
		for(int i = 0; i < 5000; i++)
		{
			randVal = rand.nextInt(100000);
			array1[i] = randVal;
		}
		max1 = max(array1);
		min1 = min(array1);
		System.out.println("The max of array1 is: "+max1);
		System.out.println("The min of array1 is: "+min1);
		
		int foo = 100000;
		int hoo = 99980;
		for(int j = 0; j < 5000; j++)
		{
			while (true)
			{
				randVal = rand.nextInt(foo);
				if(randVal > hoo && randVal < foo)
				{
					break;
				}
			}
			foo -= 20;
			hoo -= 20;
			array2[j] = randVal;
		}
		max2 = max(array2);
		min2 = min(array2);
		System.out.println("The max of array2 is: "+max2);
		System.out.println("The min of array2 is: "+min2);
		
		Scanner myScan = new Scanner(System.in);
		do{
			System.out.print("Enter an int to search inside array2: ");
			while(!myScan.hasNextInt()){
				System.out.print("Enter an int to search inside array2: ");
				myScan.next();
			}
			input = myScan.nextInt();
		}while(input < 0 || input >100000);
 		search(array2, input);
	}
}