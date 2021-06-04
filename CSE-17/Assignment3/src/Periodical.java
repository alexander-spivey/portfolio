
public class Periodical extends Title {
	private int issue, month;
	//Constructor
	Periodical() {
		super();
		issue = 0;
		month = 0;
	}
	Periodical(String cN, String t, String p, int y, int c, int i, int m) {
		super(cN, t, p, y, c);
		issue = i;
		month = m;
	}
	//Main Methods
	/**
	 * returns issue
	 * @return issue: issue of periodical
	 */
	public int getIssue() {
		return issue;
	}
	/**
	 * returns month
	 * @return month: month of periodical
	 */
	public int getMonth() {
		return month;
	}
	/**
	 * sets issue int
	 * @param i: int of new issue value
	 */
	public void setIssue(int i) {
		issue = i;
	}
	/**
	 * sets month
	 * @param m: int of month we are setting it to
	 */
	public void setMonth(int m) {
		month = m;
	}
	/**
	 * method to print periodical
	 */
	public String toString() {
		String output = super.toString();
		String periodical = String.format("%s, \n%s", issue, month);
		return output + periodical;
	}
	/**
	 * method to print formatted version of periodical
	 */
	public String formattedToString() {
		String output = super.formattedToString();
		String periodical = String.format("Issue: %s\nMonth: %s\n ", issue, month);
		return output + periodical;
	}
}
