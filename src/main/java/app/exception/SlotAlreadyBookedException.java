package app.exception;

public class SlotAlreadyBookedException extends RuntimeException {
    public SlotAlreadyBookedException(String time, String date) {
        super(String.format("%s on %s in this centre is already booked. Please select a different slot for your vaccination.", time, date));
    }
}
