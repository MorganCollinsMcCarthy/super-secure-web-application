package app.controller;

import app.persistence.model.Appointment;
import app.persistence.model.Centre;
import app.persistence.repository.AppointmentRepository;
import app.persistence.repository.CentreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class BookingController {
    @Autowired
    CentreRepository centreRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @RequestMapping("/bookAppointment")
    public String bookAppointment(Model model) {
        List<Centre> centres = centreRepository.findAll();
        model.addAttribute("centres", centres);
        return "centre";
    }

    @RequestMapping("/bookAppointment/{id}")
    public String listAvailableAppointments(@PathVariable("id") int id, Model model) {
        Centre centre = centreRepository.findById(id);
        if (centre != null) {
            List<Appointment> appointments = appointmentRepository.findAllByCentre(centre);
            model.addAttribute("appointments", appointments);
        }
        else {
            model.addAttribute("appointments", null);
        }
        return "appointments";
    }
}
