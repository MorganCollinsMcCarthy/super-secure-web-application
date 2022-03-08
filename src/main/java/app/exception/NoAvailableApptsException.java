package app.exception;

public class NoAvailableApptsException extends RuntimeException {
    public NoAvailableApptsException(String date) {
        super(String.format("Unfortunately, there are no available times on %s. Please select a different date for your vaccination.", date));
    }
}
