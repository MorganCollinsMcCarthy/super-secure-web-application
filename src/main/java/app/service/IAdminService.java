package app.service;

import app.exception.CentreNotFoundException;
import app.persistence.model.Appointment;

import java.util.List;

public interface IAdminService {
    List<Appointment> getAllAppointments();
    void updateApptVaccineReceived(int appointmentID, String vaccineReceived) throws CentreNotFoundException;
}
