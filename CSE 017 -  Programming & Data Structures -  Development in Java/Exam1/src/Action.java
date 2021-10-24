
public class Action extends Movie{
	Action() {
		super();
	}
	Action(String r, String t) {
		super(t, r);
	}
	public double getLateFee(int days) {
		double action = 3.00;
		double cost = days * action;
		return cost;
	}
	public String toString() {
		String output = super.toString();
		String action = "Action \t";
		return action + output;
	}
}
