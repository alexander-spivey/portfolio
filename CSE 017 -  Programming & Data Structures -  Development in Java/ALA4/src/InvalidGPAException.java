import java.util.*;

@SuppressWarnings("serial")
public class InvalidGPAException extends InputMismatchException {
	// Constructors
	InvalidGPAException(){
		super();
	}
	InvalidGPAException(String message){
		super(message);
	}
}