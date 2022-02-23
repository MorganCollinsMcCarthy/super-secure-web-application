package app.service;

import app.persistence.model.Appointment;
import app.persistence.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ActivityService implements IActivityService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private IUserService userService;

    @Override
    public void cancelAppointmentForUser() {
        for (Appointment appointment : userService.getAuthenticatedUser().getAppointments()) {
            if (!appointment.isReceived())
                appointmentRepository.delete(appointment);
        }
    }
}
