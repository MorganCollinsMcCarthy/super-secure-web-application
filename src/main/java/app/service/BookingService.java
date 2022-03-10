package app.service;

import app.exception.CentreNotFoundException;
import app.persistence.model.Appointment;
import app.persistence.model.Centre;
import app.persistence.repository.AppointmentRepository;
import app.persistence.repository.CentreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@Transactional
public class BookingService implements IBookingService {
    // Used for validating if a user can book an appointment
    public enum BookingStatus {
        FIRST, SECOND, APPT_PENDING, FULLY_VACCINATED
    }

    @Autowired
    private CentreRepository centreRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private IUserService userService;

    // The potential available times on any day
    private static final String[] SLOT_TIMES = {"09:00", "09:15", "09:30", "09:45",
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
        // Create an appointment with the selected centre, data and time
        Appointment appointment = new Appointment(centre, date, time, checkBookingStatus().ordinal()+1);
        appointment.setUser(userService.getAuthenticatedUser()); // Create relationship between the authenticated user and appt
        appointmentRepository.save(appointment); // Insert the appointment in the database
    }

    @Override
    public List<Appointment> getAvailableAppts(int centreId, String date) throws CentreNotFoundException {
        checkIfCentreExists(centreId);
        availableAppts.clear();
        Centre centre = centreRepository.findById(centreId);

        // Iterate through potential available times and check if each is actually available
        for (String time : SLOT_TIMES) {
            // Check if an appointment already exists in this centre for the date and time
            Appointment appointment = appointmentRepository.findByCentreAndDateAndTime(centre, date, time);
            // If it is null then this time is available
            if (appointment == null)
                availableAppts.add(new Appointment(centre, date, time, checkBookingStatus().ordinal()+1));
        }
        return availableAppts;
    }

    @Override
    public void checkIfCentreExists(int centreId) throws CentreNotFoundException {
        if (centreRepository.findById(centreId) == null) throw new CentreNotFoundException(centreId);
    }

    @Override
    public BookingStatus checkBookingStatus() {
        // Check how many doses the user has received and how many appointments are on record for this user
        int numberDosesReceived = userService.getAuthenticatedUser().getNumberOfDoses();
        List<Appointment> userAppointments = userService.getAuthenticatedUser().getAppointments();

        // If unvaccinated AND no appts then user can book 1st dose
        if (numberDosesReceived == 0 && userAppointments.isEmpty())
            return BookingStatus.FIRST;
        // If 1 dose received AND user only has 1 appt recorded (must be appt for 1st dose) then user can book 2nd dose
        else if (numberDosesReceived == 1 && userAppointments.size() == 1)
            return BookingStatus.SECOND;
        // If 2 doses received then user is fully vaccinated so cannot book anymore
        else if (numberDosesReceived == 2)
            return BookingStatus.FULLY_VACCINATED;

        // If none of the conditions above are satisfied, then user has an appt pending so cannot book anymore
        return BookingStatus.APPT_PENDING;
    }

    @Override
    public boolean isFutureDate(String date) {
        // Ensure user has selected future date for vaccination
        LocalDate dateFormatted = convertStringDate(date);
        return LocalDate.now().isBefore(dateFormatted);
    }

    @Override
    public boolean is21DaysBetweenDoses(String date) {
        // If user is booking 2nd dose, ensure that it is at least 21 days after 1st dose
        if (checkBookingStatus().equals(BookingStatus.SECOND)) {
            String firstDoseDate = userService.getAuthenticatedUser().getAppointments().get(0).getDate();
            return DAYS.between(convertStringDate(firstDoseDate), convertStringDate(date)) >= 21;
        }
        return true;
    }

    private LocalDate convertStringDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }
}
