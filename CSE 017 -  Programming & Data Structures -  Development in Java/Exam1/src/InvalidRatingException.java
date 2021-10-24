@SuppressWarnings("serial")
public class InvalidRatingException extends Exception{
	// Constructors
		InvalidRatingException(){
			super();
		}
		InvalidRatingException(String message){
			super(message);
		}
}
