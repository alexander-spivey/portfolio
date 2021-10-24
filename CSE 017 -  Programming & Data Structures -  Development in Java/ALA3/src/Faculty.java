
public class Faculty extends Employee{
	//data members
	private String rank;
	
	//constructors
	Faculty(){
		super();
		rank = "none";
	}
	
	Faculty(String n, String pN, String a, String e, String t, int i, double as, java.util.Date d, String r){
		super(n, pN, a, e, t, i, as, d);
		rank = r;
	}
	
	//main methods
	public String getRank() {
		return rank;
	}
	
	public void setRank(String r) {
		rank = r;
	}
	
	public String toString() {
		String output = super.toString();
		String faculty = String.format("Rank: %s\n", rank);
		return output+faculty;
	}
}
