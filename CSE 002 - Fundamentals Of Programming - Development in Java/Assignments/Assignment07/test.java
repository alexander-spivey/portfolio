import java.util.*;
public class test{
	public static void main(String[] args)
	{
		for(int i = 1; i < 5; i ++)
		{
			for(int j = 5; j > i; j--)
			{
				System.out.print(" ");
			}
			for(int j = 0; j < i; j++)
			{
				System.out.print(i);
			}
			for(int j = 0; j < i; j++)
			{
				System.out.print(i);
			}
			System.out.println(" ");
		}
		for(int i = 5; i > 0; i --)
		{
			for(int j = 5; j > i; j--)
			{
				System.out.print(" ");
			}
			for(int j = 0; j < i; j++)
			{
				System.out.print(i);
			}
			for(int j = 0; j < i; j++)
			{
				System.out.print(i);
			}
			System.out.println(" ");
		}
		
// 		int counter = 0;
// 		for(int i = 1; i <= 10; i++)
// 		{
// 			for(int k = 10; k > i; k--)
// 			{
// 				System.out.print(" ");
// 			}
// 			for(int l = i; l > 0; l--)
// 			{
// 				if(i%3 == 0 && counter == 0)
// 				{
// 					System.out.print(" ");
// 					counter = 1;
// 				}System.out.print("*");
// 			}
// 			counter = 0;
// 			for(int j = 0; j < i; j++)
// 			{
// 				if(i%3 == 0 && counter == 0)
// 				{
// 					j++;
// 					j++;
// 					counter = 1;
// 				}System.out.print("*");
// 			}
// 			counter = 0;
// 			System.out.println("");
// 		}
		
// 		for(int i = 9 ; i > 0; i--)
// 		{
// 			for(int j = i; j > 1; j--)
// 			{
// 				System.out.print(" ");
// 			}
// 			System.out.println(i);
// 		}
	}
// 	public static void isPrime(int x)
// 	{
// 		double sqrt = Math.sqrt(x);
// 		if((int)sqrt * (int)sqrt == x)
// 		{
// 			System.out.println("Prime!");
// 		} else {
// 			System.out.println("Not prime");
// 		}
// 	}
// 	public static void solve(double b, double a)
// 	{
// 		double val = (-1*b)/(2*a);
// 		System.out.println("Discriminate is zero");
// 		System.out.println("Solution is "+val);
// 	}
// 	public static void solve(double b, double a, double c)
// 	{
// 		double val1 = ((-1*b)+Math.sqrt((b * b)-(4*a*c)))/(2*a);
// 		double val2 = ((-1*b)-Math.sqrt((b * b)-(4*a*c)))/(2*a);
// 		System.out.println("Discriminate is positive");
// 		System.out.println("Solutions are "+val1+" and "+val2);
// 	}
// 	public static void solve()
// 	{
// 		System.out.println("Discriminate is negative");
// 		System.out.println("Solution is imaginary");
// 	}
// 	public static int sumsq(int x)
// 	{
// 		int sum = 0;
// 		for(int i = 1; i <= x; i++)
// 		{
// 			sum += i*i;
// 		}
// 		return sum;
// 	}
// 	public static int sumPow(int x)
// 	{
// 		int sum = 0;
// 		int foo = 1;
// 		for(int i = 1; i <= x; i++)
// 		{
// 			for(int j = 0; j <=i-1; j++)
// 			{
// 				foo*=i;
// 			}
// 			sum += foo;
// 			foo = 1;
// 		}
// 		return sum;
// 	}
// 	public static void main( String args[] )
// 	{
// 		Scanner myScan = new Scanner(System.in);
// 		int val, input = -1;
// 		do
// 		{
// 			System.out.print("Enter an int: ");
// 			input = myScan.nextInt();
// 			if(input <= 0)
// 			{
// 				break;
// 			}
// 			val = sumPow(input);
// 			System.out.println("The sume of 1+2*2+3*3*3...+n^n for n = "+input+" is "+val);
// 		}while(input > 0);
			
// 		Scanner myScan = new Scanner(System.in);
// 		double d, a, b, c;
// 		System.out.print("a = ");
// 		a = myScan.nextDouble();
// 		System.out.print(", b = ");
// 		b = myScan.nextDouble();
// 		System.out.print(", c = ");
// 		c = myScan.nextDouble();
// 		d = (b * b)-(4*a*c);
		
// 		if(d==0)
// 		{
// 			solve(b,a);
// 		} else if (d>0) {
// 			solve(b,a,c);
// 		} else {
// 			solve();
// 		}
		
// 		Scanner myScan = new Scanner(System.in);
// 		int input = -1;
// 		String foo;
// 		do
// 		{
// 			System.out.print("Input a positive integer (0 to exit): ");
// 			while(!myScan.hasNextInt())
// 			{
// 				System.out.println("Invalid");
// 				System.out.print("Input a positive integer (0 to exit): ");
// 				myScan.next();
// 			}
// 			input = myScan.nextInt();
// 			if(input == 0)
// 			{
// 				System.out.println("Goodbye");
// 				break;
// 			} else if (input > 0) {
// 				isPrime(input);
// 				input = -1;
// 			} else if (input < 0) {
// 				System.out.println("Invalid");
// 				continue;
// 			}
// 		}while(input < 0);
		
// 		int foo = 1;
// 		for(int i = 2; i < 6; i++)
// 		{
// 			for (int j = 1; j < i; j++)
// 			{
// 				for(int k = -1; k < j; k++)
// 				{
// 					System.out.print(foo);
// 				}
// 				System.out.println("");
// 			}
// 			foo *= 3;
// 		}

// 		Scanner myScan = new Scanner(System.in);
// 		int input = 0; 
// 		String foo;
// 		do
// 		{
// 			System.out.print("Please enter a number between 0-10: ");
// 			while(!myScan.hasNextInt())
// 			{
// 				System.out.print("Please enter a number between 0-10: ");
// 				myScan.next();
// 			}
// 			input = myScan.nextInt();
// 		}while(input < 0 || input > 10);

// 		for(int i = 6; i > 0; i--)
// 		{
// 			int val = i - 2;
// 			if(val <= 0)
// 			{
// 				System.out.println(i);
// 				continue;
// 			}
// 			for(int j = 0; j<val; j++)
// 			{
// 				for(int k = 0; k<val-j; k++)
// 				{
// 					System.out.print(i);
// 				}
// 				System.out.println("");
// 			}
// 		}
// 	}
}