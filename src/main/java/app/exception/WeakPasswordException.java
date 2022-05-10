package app.exception;

public class WeakPasswordException extends RuntimeException {
    public WeakPasswordException() {
        super();
    }

    public WeakPasswordException(final String message) {
        super(message);
    }
}
