
public class Complex implements Arithmetic<Complex>{
	private int real;
	private int imaginary;
	//Constructor
	Complex() {
		this.imaginary = 0;
		this.real = 0;
	}
	Complex(int r, int im) {
		this.imaginary = im;
		this.real = r;
	}
	//Main Methods
	//method to add complex
	public Complex add(Complex c) {
		int r = real + c.real;
		int im = imaginary + c.imaginary;
		Complex sum = new Complex(r, im);
		return sum;
	}
	//subtract complex together
	public Complex subtract(Complex c) {
		int r = real - c.real;
		int im = imaginary - c.imaginary;
		Complex sum = new Complex(r, im);
		return sum;
	}
	//multiply complex together
	public Complex multiply(Complex c) {
		int im = imaginary * c.imaginary;
	    int r = (real * c.real) - im;
	    Complex sum = new Complex(r, im);
	    return sum;
	}
	//divide complex together
	public Complex divide(Complex c) {
		int r = real / c.real;
		int im = imaginary / c.imaginary;
		Complex sum = new Complex(r, im);
		return sum;
	}
	//it make the info into string, nice...
	public String toString() {
		String output = "";
		if(this.real == 0 && this.imaginary == 0) {
			output = "0";
		} else if(this.real == 0) {
			output = Integer.toString(imaginary)+"i";
		} else if (this.imaginary == 0) {
			output = Integer.toString(real);
		} else if (this.imaginary == 1) {
			output =  Integer.toString(real) + "+" + Integer.toString(imaginary);
		} else {
			output =  Integer.toString(real) + "+" + Integer.toString(imaginary)+"i";
		}
		return output;	
	}

}
