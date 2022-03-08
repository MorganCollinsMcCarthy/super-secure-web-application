package app.exception;

public class SecondDoseAfter3WeeksException extends RuntimeException {
    public SecondDoseAfter3WeeksException(String date) {
        super(String.format("Second dose must be administered at least 21 days after your first dose. You received your first dose on %s.", date));
    }
}
