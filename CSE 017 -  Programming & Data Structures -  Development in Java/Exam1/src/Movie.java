
public class Movie {
	//variables
	private String title;
	private String rating;
	
	//Constructors
	Movie() {
		title = "";
		rating = "";
	}
	Movie(String t, String r) {
		title = t;
		rating = r;
	}
	public String getTitle() {
		return title;
	}
	public String getRating() {
		return rating;
	}
	public void setTitle(String t) {
		title = t;
	}
	public void setRating(String r) {
		rating = r;
	}
	public double getLateFee(int days) {
		double movie = 2.00;
		double cost = days * movie;
		return cost;
	}
	public String toString() {
		String output;
		output = String.format("Rating: %s\tTitle: %s\n", rating, title);
		return output;
	}
}
