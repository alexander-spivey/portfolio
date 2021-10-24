import java.util.InputMismatchException;

@SuppressWarnings("serial")
public class InvalidCallNumber extends InputMismatchException{
	InvalidCallNumber() {
		super();
	}
	InvalidCallNumber(String message) {
		super(message);
	}
}
