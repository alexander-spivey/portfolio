import java.util.*;

public class pt{
	public static void topN(int[] A, int k)
	{
		int i = A.length-1;
		A[i] = k;
		while(i > 0 && A[i-1] > A[i])
		{
			int temp = 0;
			temp = A[i-1];
			A[i-1] = A[i];
			A[i] = temp;
			i--;
		}
	}
	public static boolean isSorted(int[] A)
	{
		for(int i = 0; i < A.length - 1; i++)
		{
			if(A[i] > A[i+1])
			{
				return false;
			}
		}
		return true;
	}
	public static int[] getBiggest(int[][] A)
	{
// 		int[][] a1 = {
// 			{1,2,3},
// 			{4,5,6},
// 			{7,0,0}
// 		};
// 		getBiggest(a1);
		//sum into array
		int[] totalSum = new int[A.length];
		for(int i = 0; i < A.length; i++)
		{
			for(int j = 0; j < A[i].length; j++)
			{
				totalSum[i] += A[i][j];
			}
		}
		//find max
		int temp = 0;
		for(int i = 0; i < totalSum.length; i++)
		{
			if(temp < totalSum[i])
			{
				temp = totalSum[i];
			}
		}
		//the array with max
		int foo = 0;
		for(foo = 0; foo < totalSum.length; foo++)
		{
			if(temp == totalSum[foo])
			{
				break;
			}
		}
		int[] finalAnswer = A[foo];
		System.out.println(Arrays.toString(finalAnswer));
		return finalAnswer;
	}
	public static int[] reverse(int[] rArray)
	{
		int[] reverseResult = new int[rArray.length];
		int ct = 0;
		for(int i = rArray.length - 1; i > -1; i--)
		{
			reverseResult[ct] = rArray[i];
			ct++;
		}
		return reverseResult;
	}
	public static int[] swapMax(int[] A, int k)
	{
		int max = -99999;
		int maxIndex = 0;
		for(int i = k; i < A.length; i++)
		{
			if(max < A[i])
			{
				max = A[i];
				maxIndex = i;
			}
		}
		
		int temp = A[maxIndex];
		A[maxIndex] = A[k];
		A[k] = temp;
		
		return A;
	}
	public static int[] sort(int[] A)
	{
		for(int i = 0; i < A.length; i++)
		{
			swapMax(A, i);
		}
		int[] result = reverse(A);
		return result;
	}
	public static void nudgeCircle(int[] A, int k)
	{
		System.out.println(Arrays.toString(A));
		int[] tempVal = new int[A.length];
		for(int i = 0; i < A.length; i++)
		{
			tempVal[(i+k)%A.length]=A[i];
		}
		for(int i = 0; i < A.length; i++)
		{
			A[i] = tempVal[i];
		}
		System.out.println(Arrays.toString(A));
	}
	public static void twoDimensionalCircleNudge(int[][] A, int k, int b)
	{
		int[] temp = new int [A.length];
		for(int i = 0; i < A.length; i++)
		{
			temp[(i+k)%A.length] = A[i][b];
		}
		for(int i = 0; i < A.length; i++)
		{
			A[i][b] = temp[i];
		}
	}
	public static void pad(String[] A)
	{
		int maxStringSize = 0;
		for(int i = 0; i < A.length; i++)
		{
			if(maxStringSize < A[i].length())
			{
				maxStringSize = A[i].length();
			}
		}
		for(int i = 0; i < A.length; i++)
		{
			for(int j = 0; j < maxStringSize; j++)
			{
				if(A[i].length() < maxStringSize)
				{
					A[i]+=" ";
				}
				else if (A[i].length() == maxStringSize)
				{
					break;
				}
			}
		}
// 		String thai[]={"one", "fine", "evening"};
// 		System.out.println(Arrays.toString(thai));
// 		pad(thai);
// 		System.out.println(Arrays.toString(thai));
	}
	public static boolean ragged(int[][][] A)
	{
		int size = A[0].length;
		for(int i = 1; i < A.length; i++)
		{
			if(A[i].length!=size)
			{
				return true;
			}
		}
		
		size = A[0][0].length;
		for(int i = 0; i < A.length; i++)
		{
			for(int j = 0; j < A[i].length; j++)
			{
				if(A[i][j].length!=size)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	public static int[][] clockwise(int[][] A)
	{
		int newWidth = A.length;
		int newHeight = A[0].length;
		int[][] B = new int[newHeight][newWidth];
		
		for(int i = 0; i < newHeight; i++)
		{
			for(int j = 0; j < newWidth; j++)
			{
				B[i][j] = A[A.length-j-1][i];
			}
		}
		return B;
// 		int[][] A = {{0,1,2},
// 								 {3,4,5},
// 								 {6,7,8},
// 								 {9,10,11},
// 		};
// 		int[][] B = clockwise(A);
// 		print2dMatrix(B);
	}
	public static void print2dMatrix(char[][] A)
	{
		for(int i = 0; i < A.length; i++)
		{
			for(int j = 0; j < A[i].length; j++)
			{
				System.out.print(A[i][j]+" ");
			}
			System.out.println("");
		}
	}
	public static void print2dMatrix(int[][] A)
	{
		for(int i = 0; i < A.length; i++)
		{
			for(int j = 0; j < A[i].length; j++)
			{
				System.out.print(A[i][j]+" ");
			}
			System.out.println("");
		}
	}
	public static int[] increasing() //INPUT only int input
	{
		Scanner scan = new Scanner(System.in);
		int A[] = new int[10];
		for(int i = 0; i < A.length; i++)
		{
			do
			{
				System.out.print("Enter a value: ");
				while(!scan.hasNextInt())
				{
					System.out.print("Has to be an integer.");
					scan.next();
				}
				A[i] = scan.nextInt();
				if(i>0 && A[i]<A[i-1])
				{
					System.out.print("Number needs to be bigger");
				}
			}while(i!=0 && A[i]<A[i-1]);
		}
		return A;
	}
	public static boolean triDiagonal(int[][] A) //does dumb pattern exist
	{
		for(int i = 0; i < A.length; i++)
		{
			for(int j = 0; j < A[i].length; j++)
			{
				if(i == j && A[i][j] > 0)
				{
					if(j+1<A[i].length && A[i][j+1] == 0)
					{
						return false;
					}
					if(j-1 > -1 && A[i][j-1] == 0)
					{
						return false;
					}
				}
			}
		}
		System.out.println("True");
		return true;
// 		int[][] A = {{1,1,0,0,0},
// 								 {1,1,1,0,0},
// 								 {0,1,1,1,0},
// 								 {0,0,1,1,1},
// 		};
// 		triDiagonal(A);
	}
	public static boolean contains(int[] B, int[] A) //test if arrays have same values
	{
		int target = 0;
		int ct = 0;
		for(int i = 0; i < A.length; i++)
		{
			target = A[i];
			for(int j = 0; j < B.length; j++)
			{
				if(target == B[j])
				{
					ct++;
				}
			}
			if(ct == 0)
			{
				return false;
			}
			ct = 0;
		}
		return true;
	}
	public static boolean sameContent(int[] B, int[] A)
	{
		if(contains(B,A) == true == contains(A,B))
		{
			return true;
		} else { return false; }
// 		int x[]={1,2,3}, y[]={2,3}, z[]={2,1,3,1}; 
// 		System.out.println(contains(x,z));
// 		System.out.println(sameContent(z,x));
	}
	public static char [] stringToChars(String A) //conver string to char
	{
		int count = 0; //num of actual letters
		for(int i = 0; i < A.length(); i++)
		{
			if(A.charAt(i) != ' ' && A.charAt(i) != '.')
			{
				count++;
			}
		}
		char[] result = new char[count];
		count = 0;
		for(int i = 0; i < result.length; i++)
		{
			if(A.charAt(i) != ' ' && A.charAt(i) != '.')
			{
				result[count] = A.charAt(i);
				count++;
			}
		}
		return result;
	}
	public static char[][] starry(int row) //pattern design and use of charAt
	{
		char[][] result = new char[row][row];
		for(int i = 0; i < row; i++)
		{
			for(int j = 0; j < row; j++)
			{
				if(i == j || j == row - 1 - i)
				{
					result[i][j] = '*';
				} else {
					result[i][j] = (char)('0'+i);
				}
			}
		}
// 		print2dMatrix(starry(10));
		return result;
	}
	public static boolean inArray(int[][] A, int k) //linear and binary search
	{
		int index = 0;
		for(int i = 0; i < A.length; i++)
		{
			if(k < A[i][0])
			{
				index = i - 1;
			}
		} //linear search the first value of each lmao
		
		//THIS IS CODE FOR BINARY SEARCH
		int low = 0;
		int high = A[index].length - 1;
		
		while(high >= low)
		{
			int middle = (low + high)/2;
			if(A[index][middle]==k) { return true; }
			else if (A[index][middle]>k) { high = middle - 1; }
			else if (A[index][middle]<k) { low = middle + 1; }
		}
		return false;
	}
	public static void q6(int j, int k) //low high input integer only do while loop (do-while)
	{
		Scanner scan = new Scanner(System.in);
		if(j>k)
		{
			System.out.println("Range is impossible");
			return;
		}
		int val = 0;
		do
		{
			System.out.print("Enter a value between "+j+" and "+k+": ");
			while(!scan.hasNextInt())
			{
				System.out.println("Invalid input.");
				System.out.print("Enter a value between "+j+" and "+k+": ");
				scan.next();
			}
			val = scan.nextInt();
			if(val >= j && val <= k)
			{
				System.out.println("You entered "+val);
			}
		}while (val<j || val>k);
// 			q6(2,17);
	}
	public static boolean allPos(int g[][]) //for each loop
	{
		for(int[] u: g){  // select each row of g 
			for(int v: u){ //select each element of a row 
				if(v < 0){
					return false;
				}
			}
		}
		return true;
	}
// 	public static boolean allPos(int[][] g) //conver for each loop to a for loop
// 	{
// 		for(int i = 0; i < g.length; i++)
// 		{
// 			for(int j = 0; j < g[i].length; j++)
// 			{
// 				if(g[i][j] < 0)
// 				{
// 					return false;
// 				}
// 			}
// 		}
// 		return true;
// 	}
// 	public static boolean allPos(int g[][]) //for each to a while loop
// 	{
// 		int i = 0;
// 		while(i < g.length)
// 		{
// 			int j = 0;
// 			while(j < g[i].length)
// 			{
// 				if(g[i][j] < 0)
// 				{
// 					return false;
// 				}
// 				j++;
// 			}
// 			i++;
// 		}
// 		return true;
// 	}
	public static boolean ordered(String[][] A)
	{
		for(int i = 0; i < A.length; i++)
		{
			for(int j = 0; j < A[i].length - 1; j++)
			{
				if(A[i][j].length() > A[i][j+1].length())
				{
					System.out.println("False");
					return false;
				}
			}
		}
		for(int i = 0; i < A.length - 1; i++)
		{
			if(A[i][A[i].length-1].length() > A[i+1][0].length())
			{
				System.out.println("False");
				return false;
			}
		}
		System.out.println("True");
		return true;
// 		String z[][]={{"a","b","c","de"}, {"an","two","ton"},{"three","fourth"}};
// 		String u[][]={{"a","b","c","de"}, {"a","an","two","ton"},{"three","fourth"}};
// 		ordered(z);
// 		ordered(u);
	}
	public static int[] doubleUp(int[] A)
	{
		int[] B = new int[A.length * 2];
		for(int i = 0; i < A.length; i++)
		{
			B[i] = A[i];
		}
		return B;
// 		int[] A = {1,2,3,4,5};
// 		System.out.println(Arrays.toString(doubleUp(A)));
	}
	public static int[] squareUp(int n) { //creates {0,0,0,1,0,0,2,1,0,3,2,1,4,3,2,1}
		int length = n*n;
		int[] num = new int[length];
		int zero = 1;
		for(int i = 0; i < num.length; i++)
		{
			if((i)%n == 0 && i!=0)
			{
				num[i-1] = 1;
			}
			if(i + 1 == num.length)
			{
				num[i] = 1;
			}
		}
		int index = length - 1;
		int loop = n;
		for(int i = n; i > 0; i--)
		{
			for(int j = loop; j > 1; j--)
			{
				System.out.println(loop);
				num[index - 1] = num[index]+1;
				index--;
			}
			for(int k = zero; k > 1; k--)
			{
				num[index - 1] = 0;
				index--;
			}
			index--;
			zero++;
			loop--;
		}
		return num;
	}
	public static void moveK(int[] A, int k)
	{
// 		int[] A = {1,2,4,5,6};
// 		moveK(A,3);
// 		System.out.println(Arrays.toString(A));
		A[A.length-1] = k;
		for(int i = A.length - 1; i > -1; i--)
		{
			if(i-1 > -1)
			{
				if(A[i] < A[i-1])
				{
					int temp = A[i-1];
					A[i-1] = A[i];
					A[i] = temp;
				}
			}
		}
	}
	public static void sortArray(int[] A)
	{
		for(int i = 0; i < A.length; i++)
		{
			if(i+1 < A.length)
			{
				for(int j = i + 1; j < A.length; j++)
				{
					if(A[i] > A[j])
					{
						int temp = A[j];
						A[j] = A[i];
						A[i] = temp;
					}
				}
			}
		}
	}
	public static int[] getBiggest2(int[][] A)
	{
		int[] totalSum = new int[A.length];
		for(int i = 0; i < A.length; i++)
		{
			for(int j = 0; j < A[i].length; j++)
			{
				totalSum[i] += A[i][j];
			}
		}
		int max = -1;
		int index = 0;
		for(int i = 0; i < totalSum.length; i++)
		{
			if(max < totalSum[i])
			{
				max = totalSum[i];
				index = i;
			}
		}
		int[] result = A[index];
		return result;
// 		int[][] A = {{100,1,2},
// 								 {3,4,5},
// 								 {5,6,7}
// 		};
// 		int[] result = getBiggest2(A);
// 		System.out.println(Arrays.toString(result));
	}

	public static void main(String[] args)
	{

	}
}