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

    @Override
    public void updateApptVaccineReceived(int appointmentID, String vaccineReceived) throws CentreNotFoundException {
        appointmentRepository.updateVaccineReceived(appointmentID, vaccineReceived);
        appointmentRepository.updateReceived(appointmentID, !vaccineReceived.equals(""));

        // Auto-book a second appointment if admin updates a Dose 1 to received
        if (appointmentRepository.findById(appointmentID).getDose() == 1 && !vaccineReceived.equals(""))
            autobookSecondDose(appointmentID);
    }

    private void autobookSecondDose(int appointmentID) throws CentreNotFoundException {
        Appointment appointment = appointmentRepository.findById(appointmentID);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate firstDoseDate = LocalDate.parse(appointment.getDate(), formatter);
        LocalDate secondDoseDate = firstDoseDate.plusDays(21);

        List<Appointment> availableAppts = bookingService.getAvailableAppts(appointment.getCentre().getId(), secondDoseDate.toString());

        while (availableAppts.isEmpty()) {
            secondDoseDate = secondDoseDate.plusDays(1);
            availableAppts = bookingService.getAvailableAppts(appointment.getCentre().getId(), secondDoseDate.toString());
        }

        Appointment secondDoseAppt = availableAppts.get(0);
        secondDoseAppt.setDose(2);
        secondDoseAppt.setUser(appointment.getUser());
        appointmentRepository.save(secondDoseAppt);
    }
}
