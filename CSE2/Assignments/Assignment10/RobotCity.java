import java.util.*;

public class RobotCity{
	public static int[][] buildCity()
	{
		Random rand = new Random();
		int citySize = 0;
		while(citySize < 10 || citySize > 15)
		{
			citySize = rand.nextInt(15);
		}
		int[][] cityArray = new int[citySize][citySize];
		for(int base = 0; base < citySize; base++)
		{
			for(int height = 0; height < citySize; height++)
			{
				int pop = 0;
				while(pop < 100 || pop > 999)
				{
					pop = rand.nextInt(999);
				}
				cityArray[base][height] = pop;
			}
		}
		System.out.println("");
		System.out.println("City size is: "+citySize+"x"+citySize);
		return cityArray;
	}
	public static void display(int[][] dArray)
	{
		System.out.println("");
		for(int height = dArray.length - 1; height > -1; height--)
		{
			for(int base = 0; base < dArray.length; base++)
			{
				if(dArray[base][height] < 0)
				{
					System.out.printf(ANSI_RED+"[%4d] "+ANSI_RESET, dArray[base][height]);
				} else if (dArray[base][height] > 0) {
					System.out.printf("[%4d] ", dArray[base][height]);
				}
			}
			System.out.println("");
			System.out.println("");
		}
	}
	public static int[][] test(int[][] tArray)
	{
		for(int base = 0; base < tArray.length; base++)
		{
			for(int height = 0; height <  tArray.length; height++)
			{
				tArray[base][height] = height;
			}
		}
		return tArray;
	}
	public static int[][] invade(int[][] iArray, int k)
	{
		//k is number of robots
		Random rand = new Random();
		int base, height;
		for(int i = 0; i < k; i++)
		{
			base = rand.nextInt(iArray.length);
			height = rand.nextInt(iArray.length);
			if(iArray[base][height] > 0)
			{
				iArray[base][height] = -1*(iArray[base][height]);
			} else if (iArray[base][height] < 0)
			{
				i--;
				continue;
			}
		}
		return iArray;
	}
	public static void update(int[][] uArray)
	{
		for(int base = uArray.length - 1; base > -1; base--)
		{
			for(int height = 0; height <  uArray.length; height++)
			{
				if(uArray[base][height] < 0)
				{
// 					System.out.println("b"+base+" h"+height); spot checker :D
					if(base + 1 > uArray.length || base + 1 == uArray.length)
					{
						continue;
					} 
					else if (base + 1 < uArray.length)
					{
						if(uArray[base+1][height] > 0)
						{
							uArray[base+1][height] = -1*(uArray[base+1][height]);
						} else {continue;}
					}
				}
				else {continue;}
			}
		}
		display(uArray);
	}
	public static final String ANSI_RED = "\u001B[31m"; //get to see neg numbers in red, make life easier
	public static final String ANSI_RESET =  "\u001B[0m";
	public static void main (String[] args)
	{
		Random rand = new Random();
		int k = rand.nextInt(10);
		int[][] cityArray = buildCity();
		display(cityArray);
		invade(cityArray, k);
		System.out.println(k+" Robots have landed");
		display(cityArray);
		for(int i = 1; i < 6; i++)
		{
			System.out.println("Move #: "+i);
			update(cityArray);
		}	
	}
}