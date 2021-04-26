
public class Patterns
{
	//Diamond Shape Pattern w/ *
	public static void dShape()
	{
		for(int i = 1; i < 6; i++)
		{
			for(int j = 5-i; j > 0; j--)
			{
				System.out.print(" ");
			}
			for(int k = 0; k < i; k++)
			{
				System.out.print("*");
			}
			for(int k = 1; k < i; k++)
			{
				System.out.print("*");
			}
			System.out.println("");
		}
		for(int i = 5; i > 1; i--)
		{
			for(int j = 6-i; j > 0; j--)
			{
				System.out.print(" ");
			}
			for(int k = 0; k < i-1; k++)
			{
				System.out.print("*");
			}
			for(int k = 1; k < i-1; k++)
			{
				System.out.print("*");
			}
			System.out.println("");
		}
	}
	public static void dwnTriangle(int n)
	{
		for(int i = n; i > 0; i--)
		{
			for(int j = 0; j < i; j++)
			{
				System.out.print("* ");
			}
			System.out.println("");
		}
	}
	public static void printNums(int n) 
	{ 
		int i, j,num; 
		for(i=0; i<n; i++) // outer loop for rows
		{ 
			num=1; 
			for(j=0; j<=i; j++) // inner loop for rows
			{ 
				// printing num with a space  
				System.out.print(num+ " "); 
				//incrementing value of num 
				num++; 
			} 
			// ending line after each row 
			System.out.println(); 
		} 
	} 
	public static void dShapeN()
	{
		for (int i = 1; i <= 4; i++)
		{
			int n = 4;
			for (int j = 1; j<= n - i; j++) { System.out.print(" "); } 
			for (int k = i; k >= 1; k--) { System.out.print(k); }
			for (int l = 2; l <= i; l++) { System.out.print(l); } System.out.println(); } 
			for (int i = 3; i >= 1; i--) { int n = 3;

				for (int j = 0; j<= n - i; j++) { System.out.print(" "); } for (int k = i; k >= 1; k--)
				{
						System.out.print(k);
				}
				for (int l = 2; l <= i; l++)
				{
						System.out.print(l);
				}

				System.out.println();
		}
	}
	public static void main(String[] arg)
	{
		dShape();
		System.out.println("dShape();");
		dwnTriangle(5);
		System.out.println("dwnTriangle(5);");
		printNums(5);
		System.out.println("printNums(5);");
		dShapeN();
		System.out.println("dShapeN();");
	}
}