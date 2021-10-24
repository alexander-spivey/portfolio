import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Rational implements Arithmetic<Rational>{
	private int numerator;
	private int denominator;
	//Constructor
	Rational() {
		this.numerator = 0;
		this.denominator = 1;
	}
	Rational(int n, int d) {
		this.numerator = n;
		this.denominator = d;
	}
	Rational(String s) {
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(s);
		String[] a1 = new String[2];
		int counter = 0;
		while(m.find()) {
			a1[counter] = m.group();
			counter++;
		}
		this.numerator = Integer.parseInt(a1[0]);
		this.denominator = Integer.parseInt(a1[1]);
	}
	//add rationals together
	public Rational add(Rational f) {
		  int num = numerator * f.denominator + denominator * f.numerator; 
	      int denom = denominator * f.denominator; 
	      Rational sum = new Rational(num, denom);
	      sum.reduce(); 
	      return sum;
	}
	//subtract rationals
	public Rational subtract(Rational f) {
		  int num = numerator * f.denominator - denominator * f.numerator; 
	      int denom = denominator * f.denominator; 
	      Rational sum = new Rational(num, denom);
	      sum.reduce(); 
	      return sum;
	}
	//multiplies ratuibak together
	public Rational multiply(Rational f) {
		 int num = numerator * f.numerator; 
	     int denom = denominator * f.denominator; 
	     Rational sum = new Rational(num, denom); 
	     sum.reduce(); 
	     return sum; 
	}
	//divides rational aka fractions (inverse multipication)
	public Rational divide(Rational f) {
		int num = numerator * f.denominator; 
	     int denom = denominator * f.numerator; 
	     Rational sum = new Rational(num, denom); 
	     sum.reduce(); 
	     return sum; 
	}
	//takes fractions into simplest form
	private void reduce() {
		int gcd = gcd(this.numerator,this.denominator);
		this.numerator /= gcd;
		this.denominator /= gcd;
	}
	//recursion method to find GCD
	static int gcd(int a, int b)
	{
	  if(b == 0)
	  {
	    return a;
	  }
	  return gcd(b, a % b);
	}
	//string format for information 
	public String toString() {
		String output = "";
		if(this.denominator == 1) {
			output = Integer.toString(this.numerator);
		} else if (this.numerator == 0) {
			output = "0";
		} else {
			output = this.numerator + "/" + this.denominator;
		}
		return output;
	}
	
}
