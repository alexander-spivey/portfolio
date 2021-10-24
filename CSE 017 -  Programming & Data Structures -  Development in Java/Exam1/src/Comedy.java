
public class Comedy extends Movie{
	Comedy() {
		super();
	}
	Comedy(String t, String r) {
		super(r, t);
	}
	public String toString() {
		String output = super.toString();
		String action = "Comedy \t";
		return action + output;
	}
}
