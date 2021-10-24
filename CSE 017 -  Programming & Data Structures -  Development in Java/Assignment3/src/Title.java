
public class Title {
	private String callNumber, title, publisher;
	private int year, copies;
	//Constructor
	Title() {
		callNumber = "";
		title = "";
		publisher = "";
		year = 0;
		copies = 0;
	}
	Title(String cN, String t, String p, int y, int c) {
		callNumber = cN;
		title = t;
		publisher = p;
		year = y;
		copies = c;
	}
	//Methods
	/**
	 * returns call number of index
	 * @return callNumber: book's call number
	 */
	public String getCallNumber() {
		return callNumber;
	}
	/**
	 * returns title of index
	 * @return title: title of book
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * returns publisher of index
	 * @return publisher: book's publisher
	 */
	public String getPublisher() {
		return publisher;
	}
	/**
	 * returns year of index
	 * @return year: book's year
	 */
	public int getYear() {
		return year;
	}
	/**
	 * returns copies of index
	 * @return copies: how many copies are available of the book
	 */
	public int getCopies() {
		return copies;
	}
	/**
	 * sets the call number
	 * @param cN: call number of book
	 */
	public void setCallNumber(String cN) {
		callNumber = cN;
	}
	/**
	 * sets the title
	 * @param t: title of book
	 */
	public void setTitle(String t) {
		title = t;
	}
	/**
	 * set the publisher
	 * @param p: publisher of book
	 */
	public void setPublisher(String p) {
		publisher = p;
	}
	/**
	 * set the year
	 * @param y: year of the book
	 */
	public void setYear(int y) {
		year = y;
	}
	/**
	 * set the copies
	 * @param c: copies of book available
	 */
	public void setCopies(int c) {
		copies = c;
	}
	/**
	 * method to print out title
	 * return output: string with all call number, title, publisher, year, and copies
	 */
	public String toString() {
		String output = String.format("%s, \n%s, \n%s, \n%s, \n%s \n", callNumber, title, publisher, year, copies);
		return output;
	}
	/**
	 * method to return formatted title
	 * @return output: formatted strign with all call number, title, publisher, year, and copies
	 */
	public String formattedToString() {
		String output = String.format("Call Number: %s\nTitle: %s\nPublisher %s\nYear: %s\nCopies %s\n", callNumber, title, publisher, year, copies);
		return output;
	}
}
