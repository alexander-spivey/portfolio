import java.util.InputMismatchException;

@SuppressWarnings("serial")
public class InvalidNumberException extends InputMismatchException {
	InvalidNumberException() {
		super();
	}

	InvalidNumberException(String message) {
		super(message);
	}
}
