package app.service;

import app.exception.CentreNotFoundException;
import app.persistence.model.Appointment;
import app.persistence.model.Centre;

import java.util.List;

public interface IBookingService {
    List<Centre> findAllCentres();
    void assignApptToAuthenticatedUser(int apptId, String date, String time) throws CentreNotFoundException;
    List<Appointment> getAvailableAppts(int centreId, String date) throws CentreNotFoundException;
    void checkIfCentreExists(int centreId) throws CentreNotFoundException;
    BookingService.BookingStatus checkBookingStatus();
    boolean isFutureDate(String date);
    boolean is21DaysBetweenDoses(String date);
}
