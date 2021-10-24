import java.util.*;

@SuppressWarnings("serial")
public class InvalidDate extends InputMismatchException{
	InvalidDate() {
		super();
	}
	InvalidDate(String message) {
		super(message);
	}
}
