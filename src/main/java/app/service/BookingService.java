package app.service;

import app.exception.CentreNotFoundException;
import app.persistence.model.Appointment;
import app.persistence.model.Centre;
import app.persistence.repository.AppointmentRepository;
import app.persistence.repository.CentreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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

    public static final String[] SLOT_TIMES = {"09:00", "09:15", "09:30", "09:45",
            "10:00", "10:15", "10:30", "10:45",
            "11:00", "11:15", "11:30", "11:45",
            "12:00", "12:15", "12:30", "12:45",
            "13:00", "13:15", "13:30", "13:45",
            "14:00", "14:15", "14:30", "14:45",
            "15:00", "15:15", "15:30", "15:45",
            "16:00", "16:15", "16:30", "16:45",
            "17:00", "17:15", "17:30", "17:45"};

    private static final List<Appointment> availableAppts = new ArrayList<>();

    @Override
    public List<Centre> findAllCentres() {
        return centreRepository.findAll();
    }

    @Override
    public void assignApptToAuthenticatedUser(int centreId, String date, String time) throws CentreNotFoundException {
        checkIfCentreExists(centreId);
        Centre centre = centreRepository.findById(centreId);
        Appointment appointment = new Appointment(centre, date, time);
        appointment.setUser(userService.getAuthenticatedUser());
        appointmentRepository.save(appointment);
    }

    @Override
    public List<Appointment> getAvailableAppts(int centreId, String date) throws CentreNotFoundException {
        checkIfCentreExists(centreId);
        availableAppts.clear();
        Centre centre = centreRepository.findById(centreId);

        for (String time : SLOT_TIMES) {
            Appointment appointment = appointmentRepository.findByCentreAndDateAndTime(centre, date, time);
            if (appointment == null || appointment.getUser() == null)
                availableAppts.add(new Appointment(centre, date, time));
        }
        return availableAppts;
    }

    @Override
    public void checkIfCentreExists(int centreId) throws CentreNotFoundException {
        if (centreRepository.findById(centreId) == null) throw new CentreNotFoundException(centreId);
    }
}
