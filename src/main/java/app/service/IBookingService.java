package app.service;

import app.persistence.model.Appointment;
import app.persistence.model.Centre;

import java.util.List;

public interface IBookingService {
    List<Centre> findAllCentres();
    List<Appointment> findAllAppointmentsForCentre(int centreId);
    void assignApptToAuthenticatedUser(int apptId);
}
