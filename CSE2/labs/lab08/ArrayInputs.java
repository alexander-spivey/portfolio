import java.util.*;

public class ArrayInputs{
	static boolean IntCheck(int x)
	{
		if (x > 0){
			return true;
		} else {
			System.out.println("Unaccepted input");	
			return false;
		}
	}
	public static void main (String [] args)
	{
		Scanner myScan = new Scanner(System.in);
		String holder;
		int length = 0;
		boolean status = false;
		while(status == false)
		{
			System.out.print("Enter a valid positve number for the size of the array: ");
			if (!myScan.hasNextInt()) 
			{
				holder = myScan.next();
				System.out.println("Unaccepted input");	
				continue;
			}
			if(myScan.hasNextInt())
			{
				length = myScan.nextInt();
				status = IntCheck(length);
			}
		}
		int a[] = new int[length];
		System.out.println("Enter "+length+" positive values for the array");
		for(int i = 0; i < length; i++)
		{
			if(!myScan.hasNextInt()) 
			{
				holder = myScan.next();
				System.out.println("Unaccepted input");
				i = -1;
				continue;
			}
			a[i] = myScan.nextInt();
			status = IntCheck(a[i]);
			while(status == false)
			{
				System.out.print("Negative number not allowed, try again: ");
				a[i] = myScan.nextInt();
				status = IntCheck(a[i]);
			}
		}
		System.out.print("The array is: ");
		System.out.println(Arrays.toString(a));
	}
}