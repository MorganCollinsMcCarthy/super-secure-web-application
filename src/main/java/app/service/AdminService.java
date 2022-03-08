package app.service;

import app.exception.CentreNotFoundException;
import app.persistence.model.Appointment;
import app.persistence.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class AdminService implements IAdminService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private IBookingService bookingService;

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // Method to allow HSE staff to update vaccination info about user
    @Override
    public void updateApptVaccineReceived(int appointmentID, String vaccineReceived) throws CentreNotFoundException {
        // Set the type of vaccine received (moderna or pfizer)
        appointmentRepository.updateVaccineReceived(appointmentID, vaccineReceived);
        // If the type of vaccine received is not empty then update that the dose has been received
        appointmentRepository.updateReceived(appointmentID, !vaccineReceived.equals(""));

        // Auto-book a second appointment if admin updates a Dose 1 to received
        if (appointmentRepository.findById(appointmentID).getDose() == 1 && !vaccineReceived.equals(""))
            autobookSecondDose(appointmentID);
    }

    // Method to automatically book a 2nd appt 21 days after 1st dose (ID of 1st dose is passed in)
    private void autobookSecondDose(int appointmentID) throws CentreNotFoundException {
        Appointment appointment = appointmentRepository.findById(appointmentID); // Find 1st dose appointment
        // Convert the date to LocalDate and add 21 days to get 2nd dose date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate firstDoseDate = LocalDate.parse(appointment.getDate(), formatter);
        LocalDate secondDoseDate = firstDoseDate.plusDays(21);

        // Find all available appts in the same centre as the 1st dose but 21 days later
        List<Appointment> availableAppts = bookingService.getAvailableAppts(appointment.getCentre().getId(), secondDoseDate.toString());

        // If no available appts 21 days after 1st dose, try 22 days, 23 days etc
        while (availableAppts.isEmpty()) {
            secondDoseDate = secondDoseDate.plusDays(1);
            availableAppts = bookingService.getAvailableAppts(appointment.getCentre().getId(), secondDoseDate.toString());
        }

        // Book the user for their 2nd dose at the first time available on the date
        Appointment secondDoseAppt = availableAppts.get(0);
        secondDoseAppt.setDose(2);
        secondDoseAppt.setUser(appointment.getUser());
        appointmentRepository.save(secondDoseAppt);
    }
}
