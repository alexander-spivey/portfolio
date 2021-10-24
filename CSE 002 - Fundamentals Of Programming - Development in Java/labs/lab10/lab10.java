	import java.util.*;

public class lab10{
	public static int[][] increasingMatrix(int height, int width, boolean format)
	{
		int[][] a1 = new int[height][width];
		int foo = 1;
		if(format == true)
		{
			for(int i = 0; i < height; i++)
			{
				for(int j = 0; j < width; j++)
				{
					a1[i][j] = foo;
					foo ++;
				}
			}
			return a1;
		} 
		else
		{
			for(int j = 0; j < width; j++)
			{
				for(int i = 0; i < height; i++)
				{
					a1[i][j] = foo;
					foo ++;
				}
			}
			return a1;
		}
	}
	public static void printMatrix(int[][] printArray)
	{
		if (printArray == null)
		{
			System.out.println("Array was null/empty");
			return;
		}
		for(int i = 0; i < printArray.length; i++)
		{
			System.out.print("[");
			for(int j = 0; j < printArray[0].length; j++)
			{
				System.out.print(" ");
				System.out.print(printArray[i][j]);
				System.out.print(" ");
			}
			System.out.println("]");
		}
		return;
	}
	public static int[][] translate(int[][] tArray)
	{
		int[][] copyArray = new int[tArray.length][tArray[0].length]; //height, width
		int foo = 0; //width
		int hoo = 0; //height
		for(int i = 0; i < copyArray.length; i++)
			{
				for(int j = 0; j < copyArray[0].length; j++)
				{
					if(hoo == tArray.length - 1)
					{
						copyArray[i][j] = tArray[hoo][foo];
						foo++;
						hoo = 0;
						continue;
					}
					copyArray[i][j] = tArray[hoo][foo];
					hoo++;
				}
			}
		return copyArray;
	}
	public static int[][] addMatrix(int[][] a, int[][] b)
	{
		int[][] c = new int[a.length][a[0].length];
		if(a.length == b.length && a[0].length == b[0].length)
		{
			for(int i = 0; i < a.length; i++)
			{
				for(int j = 0; j < a[0].length; j++)
				{
					c[i][j] = a[i][j] + b[i][j];
				}
			}
			return c;
		} else { System.out.println("Not same length, could not add arrays together"); return b; }
	}
	public static void main(String[] args)
	{
		Random rand = new Random();
		int randHeight1 = 0, randWidth1 = 0, randHeight2 = 0, randWidth2 = 0;
		do
		{
			randHeight1 = rand.nextInt(7);
			randWidth1 = rand.nextInt(7);
			randHeight2 = rand.nextInt(7);
			randWidth2 = rand.nextInt(7);
		}while(randHeight1 <= 0 || randHeight2 <= 0 || randWidth1 <= 0 || randWidth2 <= 0);
		System.out.println("Array A with "+randHeight1+" height, "+randWidth1+" width, and row-major matrix");
		int[][] a = increasingMatrix(randHeight1, randWidth1, true);
		printMatrix(a);
		System.out.println("Array B with "+randHeight1+" height, "+randWidth1+" width, and column-major matrix");
		int[][] b = increasingMatrix(randHeight1, randWidth1, false);
		printMatrix(b);
		System.out.println("Array C with "+randHeight2+" height, "+randWidth2+" width, and row-major matrix");
		int[][] c = increasingMatrix(randHeight2, randWidth2, true);
		printMatrix(c);
		System.out.println("Array AB is the addition of A and B");
		int[][] ab = addMatrix(a,b);
		printMatrix(ab);
		System.out.println("Array AC is the addition of A and C");
		int[][] ac = addMatrix(a,c);
		printMatrix(ac);
		System.out.println("Array B2 is the conversion of B to row-major matrix");
		int[][] b2 = translate(b);
		printMatrix(b2);
	}
}