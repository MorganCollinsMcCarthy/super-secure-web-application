package app.controller;

import app.exception.*;
import app.persistence.model.Appointment;
import app.persistence.model.Centre;
import app.service.BookingService;
import app.service.IBookingService;
import app.service.IUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
public class BookingController {
    @Autowired
    private IBookingService bookingService;

    @Autowired
    private IUserService userService;

    private final Logger LOG = LogManager.getLogger(BookingController.class);

    @GetMapping("/bookAppointment")
    public String listVaccinationCentres(Model model) {
        model.addAttribute("bookingStatus", bookingService.checkBookingStatus());
        List<Centre> centres = bookingService.findAllCentres();
        model.addAttribute("centres", centres);
        return "centres";
    }

    @GetMapping("/bookAppointment/{centreId}")
    public String selectDate(@PathVariable("centreId") int centreId, Model model) throws CentreNotFoundException {
        model.addAttribute("bookingStatus", bookingService.checkBookingStatus());
        model.addAttribute("minDate", LocalDate.now().plusDays(1));
        bookingService.checkIfCentreExists(centreId);
        model.addAttribute("centreId", centreId);
        return "dateSelector";
    }

    @PostMapping("/bookAppointment/{centreId}/dateSelected")
    public String listAvailableAppointments(@PathVariable("centreId") int centreId,
                                            @RequestParam("date") String date, Model model) throws CentreNotFoundException {
        model.addAttribute("bookingStatus", bookingService.checkBookingStatus());
        model.addAttribute("minDate", LocalDate.now().plusDays(1));

        if (!bookingService.isFutureDate(date)) {
            model.addAttribute("error", new DateMustBeFutureException(date).getMessage());
            return "dateSelector";
        }
        else if (!bookingService.is21DaysBetweenDoses(date)) {
            model.addAttribute("error", new SecondDoseAfter3WeeksException().getMessage());
            return "dateSelector";
        }

        List<Appointment> appointments = bookingService.getAvailableAppts(centreId, date);
        if (appointments == null || appointments.isEmpty()) {
            model.addAttribute("error", new NoAvailableApptsException(date).getMessage());
            return "dateSelector";
        }

        model.addAttribute("centreId", centreId);
        model.addAttribute("appointments", appointments);

        return "appointments";
    }

    @PostMapping("/bookAppointment/{centreId}")
    public String bookAppointment(@PathVariable("centreId") int centreId, @RequestParam(value="date") String date,
                                  @RequestParam(value="time") String time, HttpServletResponse response, Model model)
            throws CentreNotFoundException, IllegalBookingException {
        BookingService.BookingStatus bookingStatus = bookingService.checkBookingStatus();

        if (bookingStatus.equals(BookingService.BookingStatus.FULLY_VACCINATED) || bookingStatus.equals(BookingService.BookingStatus.APPT_PENDING)) {
            model.addAttribute("error", new IllegalBookingException(bookingStatus).getMessage());
            return "appointments";
        }

        if(bookingService.checkIfSlotAlreadyBooked(time, date, centreId)) {
            model.addAttribute("error", new SlotAlreadyBookedException(time, date).getMessage());
            return "appointments";
        }

        bookingService.assignApptToAuthenticatedUser(centreId, date, time);
        LOG.trace("User \"" + userService.getAuthenticatedUser().getUserName() + "\" booked appointment");
        try {
            response.sendRedirect("/activity");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
