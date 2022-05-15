package app.exception;

public class InvalidUserInputException extends RuntimeException {
    public InvalidUserInputException() {
        super();
    }

    public InvalidUserInputException(final String message) {
        super(message);
    }
}
