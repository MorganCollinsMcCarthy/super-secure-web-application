package app.service;

import app.persistence.model.Appointment;
import app.persistence.model.Centre;
import app.persistence.repository.AppointmentRepository;
import app.persistence.repository.CentreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class BookingService implements IBookingService {
    @Autowired
    private CentreRepository centreRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private IUserService userService;

    @Override
    public List<Centre> findAllCentres() {
        List<Centre> centres = centreRepository.findAll();
        return centres;
    }

    @Override
    public List<Appointment> findAllAppointmentsForCentre(int centreId) {
        Centre centre = centreRepository.findById(centreId);
        return appointmentRepository.findAllByCentreAndUser(centre, null);
    }

    @Override
    public void assignApptToAuthenticatedUser(int apptId) {
        Appointment appointment = appointmentRepository.findById(apptId);
        if (appointment != null && appointment.getUser() == null) {
            appointment.setUser(userService.getAuthenticatedUser());
            appointmentRepository.save(appointment);
        }
    }
}
