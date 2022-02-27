package app.exception;

public class DateMustBeFutureException extends RuntimeException {
    public DateMustBeFutureException(String date) {
        super(String.format("%s is in the past. Please select a future date for your vaccination.", date));
    }
}
