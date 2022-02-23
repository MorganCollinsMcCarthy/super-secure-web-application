package app.exception;

import app.service.BookingService;

public class IllegalBookingException extends Exception {
    public IllegalBookingException(BookingService.BookingStatus bookingStatus) {
        super(String.format("You cannot book a COVID-19 vaccination due to : '%s'", bookingStatus));
    }
}