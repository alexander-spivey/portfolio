import java.util.*;

@SuppressWarnings("serial")
public class InvalidIDException extends InputMismatchException {
	// Constructors
	InvalidIDException(){
		super();
	}
	InvalidIDException(String message){
		super(message);
	}
}