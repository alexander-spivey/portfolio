//This program was worked on together with Tyler Hagmann, both working equally on it.
//Also a fair amount of the methods was supplied on Piazza by Prof.
public class Test {
	public static void main(String[] args) {
		Rational[][] A = { { new Rational("1/2"), new Rational("1/2"), new Rational("1/2") },
				{ new Rational("1/2"), new Rational("1/2"), new Rational("1/2") },
				{ new Rational("1/2"), new Rational("1/2"), new Rational("1/2") } };

		GenericMatrix<Rational> rationalMatrix1 = new GenericMatrix<>(A);
		GenericMatrix<Rational> rationalMatrix2 = new GenericMatrix<>(A);
		GenericMatrix<Rational> rationalMatrix3;

		System.out.println("Rational Matrices");
		rationalMatrix3 = rationalMatrix1.add(rationalMatrix2);
		System.out.println("Addition");
		printMatrixOperation(rationalMatrix1, rationalMatrix2, '+', rationalMatrix3);

		rationalMatrix3 = rationalMatrix1.subtract(rationalMatrix2);
		System.out.println("Subtraction");
		printMatrixOperation(rationalMatrix1, rationalMatrix2, '-', rationalMatrix3);

		rationalMatrix3 = rationalMatrix1.multiply(rationalMatrix2);
		System.out.println("Multiplication");
		printMatrixOperation(rationalMatrix1, rationalMatrix2, '*', rationalMatrix3);

		Complex[][] AC = { { new Complex(1, 2), new Complex(1, 2), new Complex(1, 2) },
				{ new Complex(1, 2), new Complex(1, 2), new Complex(1, 2) },
				{ new Complex(1, 2), new Complex(1, 2), new Complex(1, 2) } };

		GenericMatrix<Complex> complexMatrix1 = new GenericMatrix<>(AC);
		GenericMatrix<Complex> complexMatrix2 = new GenericMatrix<>(AC);
		GenericMatrix<Complex> complexMatrix3;

		System.out.println("Complex Matrices");
		complexMatrix3 = complexMatrix1.add(complexMatrix2);
		System.out.println("Addition");
		printMatrixOperation(complexMatrix1, complexMatrix2, '+', complexMatrix3);

		complexMatrix3 = complexMatrix1.subtract(complexMatrix2);
		System.out.println("Subtraction");
		printMatrixOperation(complexMatrix1, complexMatrix2, '-', complexMatrix3);

		complexMatrix3 = complexMatrix1.multiply(complexMatrix2);
		System.out.println("Multiplication");
		printMatrixOperation(complexMatrix1, complexMatrix2, '*', complexMatrix3);
	}

	/**
	 * Method to print matrix
	 * 
	 * @param <E>:       generic reference for object
	 * @param m1:        matrix 1
	 * @param m2:        matrix 3
	 * @param operation: what symbol for character
	 * @param result:    final matrix with the result
	 */
	public static <E extends Arithmetic<E>> void printMatrixOperation(GenericMatrix<E> m1, GenericMatrix<E> m2,
			char operation, GenericMatrix<E> result) {
		for (int i = 0; i < m1.rows(); i++) {
			System.out.print("| ");
			for (int j = 0; j < m1.columns(); j++) {
				System.out.print(m1.get(i, j) + " ");
			}
			System.out.print("|");
			if (i == 1) {
				System.out.print(" " + operation + " ");
			} else {
				System.out.print("   ");
			}
			System.out.print("| ");
			for (int j = 0; j < m2.columns(); j++) {
				System.out.print(m2.get(i, j) + " ");
			}
			System.out.print("|");
			if (i == 1) {
				System.out.print(" = ");
			} else {
				System.out.print("   ");
			}
			System.out.print("| ");
			for (int j = 0; j < result.columns(); j++) {
				System.out.print(result.get(i, j) + " ");
			}
			System.out.print("|");
			System.out.println();
		}
	}
}
