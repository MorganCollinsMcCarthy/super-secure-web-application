package app.controller;

import app.exception.CentreNotFoundException;
import app.exception.IllegalBookingException;
import app.persistence.model.Appointment;
import app.persistence.model.Centre;
import app.service.BookingService;
import app.service.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class BookingController {
    @Autowired
    private IBookingService bookingService;

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
        bookingService.checkIfCentreExists(centreId);
        model.addAttribute("centreId", centreId);
        return "dateSelector";
    }

    @PostMapping("/bookAppointment/{centreId}/dateSelected")
    public String listAvailableAppointments(@PathVariable("centreId") int centreId,
                                            @RequestParam("date") String date, Model model) throws CentreNotFoundException {
        model.addAttribute("bookingStatus", bookingService.checkBookingStatus());
        List<Appointment> appointments = bookingService.getAvailableAppts(centreId, date);
        model.addAttribute("centreId", centreId);
        model.addAttribute("appointments", appointments);

        return "appointments";
    }

    @PostMapping("/bookAppointment/{centreId}")
    public String bookAppointment(@PathVariable("centreId") int centreId, @RequestParam(value="date") String date,
                                  @RequestParam(value="time") String time, HttpServletResponse response)
            throws CentreNotFoundException, IllegalBookingException {
        BookingService.BookingStatus bookingStatus = bookingService.checkBookingStatus();

        if (bookingStatus.equals(BookingService.BookingStatus.FULLY_VACCINATED) || bookingStatus.equals(BookingService.BookingStatus.APPT_PENDING))
            throw new IllegalBookingException(bookingStatus);

        bookingService.assignApptToAuthenticatedUser(centreId, date, time);
        try {
            response.sendRedirect("/bookAppointment");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
