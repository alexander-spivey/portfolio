
public class Drama extends Movie{
	Drama() {
		super();
	}
	Drama(String r, String t) {
		super(t, r);
	}
	public double getLateFee(int days) {
		double drama = 2.50;
		double cost = days * drama;
		return cost;
	}
	public String toString() {
		String output = super.toString();
		String drama = "Drama \t";
		return drama + output;
	}
}
