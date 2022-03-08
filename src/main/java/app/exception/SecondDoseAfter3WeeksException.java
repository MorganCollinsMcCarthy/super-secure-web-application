package app.exception;

public class SecondDoseAfter3WeeksException extends RuntimeException {
    public SecondDoseAfter3WeeksException() {
        super("Second dose must be administered at least 21 days after your first dose. Go to localhost:8080/activity to view the date of your first dose.");
    }
}
