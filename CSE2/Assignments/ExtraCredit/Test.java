import java.util.*;

public class Test{
	public static int[] getBiggest(int[][] ragged)
	{
		int[] totalSum = new int[ragged.length];
		for(int i = 0; i < ragged.length; i++)
		{
			for(int j = 0; j < ragged[i].length; j++)
			{
				totalSum[i] += ragged[i][j];
			}
		}
		
		//find max in totalSum
		int foo = 0;
		for(int i = 0; i < totalSum.length; i++)
		{
			if(foo < totalSum[i])
			{
				foo = totalSum[i];
			}
		}
		
		//find the array associated to the max
		int hoo = 0;
		for(hoo = 0; hoo < totalSum.length; hoo++)
		{
			if(foo == totalSum[hoo])
			{
				break;
			}
		}
		
		int[] finalResult = ragged[hoo];
		System.out.println(Arrays.toString(finalResult));
		return finalResult;
	}
	public static void main(String[] args)
	{
		int[][] a1 = {
			{1,2,3}, //0 = 6
			{4,5,6}, //1 = 15
			{7,0,0} //2 = 7
		};
		getBiggest(a1);
	}
}