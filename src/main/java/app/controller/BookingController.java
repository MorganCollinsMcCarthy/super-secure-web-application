package app.controller;

import app.persistence.model.Appointment;
import app.persistence.model.Centre;
import app.service.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class BookingController {
    @Autowired
    private IBookingService bookingService;

    @GetMapping("/bookAppointment")
    public String listVaccinationCentres(Model model) {
        List<Centre> centres = bookingService.findAllCentres();
        model.addAttribute("centres", centres);
        return "centre";
    }

    @GetMapping("/bookAppointment/{id}")
    public String listAvailableAppointments(@PathVariable("id") int id, Model model) {
        List<Appointment> appointments = bookingService.findAllAppointmentsForCentre(id);
        model.addAttribute("appointments", appointments);

        return "appointments";
    }

    @PostMapping("/bookAppointment/{id}")
    public String bookAppointment(@PathVariable("id") int id, HttpServletResponse response) {
        bookingService.assignApptToAuthenticatedUser(id);
        try {
            response.sendRedirect("/bookAppointment");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
