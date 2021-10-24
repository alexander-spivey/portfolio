public class Course implements Comparable<Course>{
	private String number;
	private int credits;
	Course(String number, int cr){
		this.number = number;
		this.credits = cr;
	}
	public String getNumber() {
		return number;
	}
	public int getCredits() {
		return credits;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public void setCredits(int cr) {
		this.credits = cr;
	}
	public String toString() {
		String output = "{"+ this.number + " " + this.credits + "} ";
		return output;
	}
	
	public int compareTo(Course c) {
        if (getNumber().compareTo(c.getNumber()) == 0) {
            return 0;
        } else if (getNumber().compareTo(c.getNumber()) > 0) {
            return 1;
        } else {
            return -1;
        }
    }
	
}
