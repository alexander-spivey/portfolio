import java.util.ArrayList;

public class GenericMatrix<E extends Arithmetic<E>> {
	private ArrayList<ArrayList<E>> matrix;

	// Constructor
	public GenericMatrix(E[][] input) {
		matrix = new ArrayList<>();
		for (int i = 0; i < input.length; i++) {
			matrix.add(new ArrayList<>());
			for (int j = 0; j < input[i].length; j++) {
				matrix.get(i).add(input[i][j]);
			}
		}
	}
	public GenericMatrix(ArrayList<ArrayList<E>> input) {
		matrix = new ArrayList<>();
		for (int i = 0; i < input.size(); i++) {
			matrix.add(new ArrayList<>());
			for (int j = 0; j < input.get(i).size(); j++) {
				matrix.get(i).add(input.get(i).get(j));
			}
		}
	}
	//getter for item in pos [r][c]
	public E get(int r, int c) {
		return matrix.get(r).get(c);
	}
	//setter for item in pos [r][c]
	public E set(int r, int c, E value) {
		E temp = matrix.get(r).get(c);
		matrix.get(r).remove(c);
		matrix.get(r).add(c, value);
		return temp;
	}
	//number of rows in matrix
	public int rows() {
		return matrix.size();
	}
	//number of columns in matrix
	public int columns() {
		return matrix.get(0).size();
	}
	//method to use generic matrix to add together
	public GenericMatrix<E> subtract(GenericMatrix<E> gm) {
		ArrayList<ArrayList<E>> subtraction = new ArrayList<>();
		for (int i = 0; i < matrix.size(); i++) {
			ArrayList<E> row = new ArrayList<>();
			for (int j = 0; j < matrix.get(0).size(); j++) {
				E sum = matrix.get(i).get(j).subtract(matrix.get(i).get(j));
				row.add(sum);
			}
			subtraction.add(row);
		}
		return new GenericMatrix<>(subtraction);
	}
	//method to use generic matrix to subtract together
	public GenericMatrix<E> add(GenericMatrix<E> gm) {
		ArrayList<ArrayList<E>> addition = new ArrayList<>();
		for (int i = 0; i < matrix.size(); i++) {
			ArrayList<E> row = new ArrayList<>();
			for (int j = 0; j < matrix.get(0).size(); j++) {
				E sum = matrix.get(i).get(j).add(matrix.get(i).get(j));
				row.add(sum);
			}
			addition.add(row);
		}
		return new GenericMatrix<>(addition);
	}
	//method to use generic matrix to multiply together
	public GenericMatrix<E> multiply(GenericMatrix<E> gm) {
		ArrayList<ArrayList<E>> product = new ArrayList<>(); 
		for (int i = 0; i < matrix.size(); i++) {
			ArrayList<E> row = new ArrayList<>(); 
			for (int j = 0; j < matrix.get(0).size(); j++) {
				E sumProduct = matrix.get(i).get(0).multiply(gm.matrix.get(0).get(j));
				for (int k = 1; k < matrix.get(0).size(); k++) {
					sumProduct = sumProduct.add(matrix.get(i).get(k).multiply(gm.matrix.get(k).get(j))); 
				}
				row.add(sumProduct); 
			}
			product.add(row);
		}
		return new GenericMatrix<>(product); 
	}
}
