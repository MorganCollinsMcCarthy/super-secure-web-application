package app.exception;

public final class UserAgeException extends RuntimeException {

    public UserAgeException() {
        super();
    }

    public UserAgeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserAgeException(final String message) {
        super(message);
    }

    public UserAgeException(final Throwable cause) {
        super(cause);
    }

}