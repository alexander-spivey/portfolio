
public class Book extends Title{
	private String author, ISBN;
	//Constructor
	Book() {
		super();
		author = "";
		ISBN = "";
	}
	Book(String cN, String t, String p, int y, int c, String a, String isbn) {
		super(cN, t, p, y, c);
		author = a;
		ISBN = isbn;
	}
	//Methods
	/**
	 * returns author
	 * @return author: author name
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * returns ISBN string
	 * @return ISBN: isbn associated with the placement
	 */
	public String getISBN() {
		return ISBN;
	}
	
	/**
	 * sets author
	 * @param a: author name
	 */
	public void setAuthor(String a) {
		author = a;
	}
	/**
	 * sets isbn
	 * @param isbn: book's isbn string
	 */
	public void setISBN(String isbn) {
		ISBN = isbn;
	}
	
	/**
	 * method to print books
	 */
	public String toString() {
		String output = super.toString();
		String book = String.format("%s, \n%s", author, ISBN);
		return output + book;
	}
	
	/**
	 * formatted version of toString for books
	 */
	public String formattedToString() {
		String output = super.formattedToString();
		String book = String.format("Author: %s\nISBN: %s\n ", author, ISBN);
		return output + book;
	}
}
