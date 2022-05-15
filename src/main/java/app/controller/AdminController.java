package app.controller;

import app.exception.CentreNotFoundException;
import app.service.IAdminService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AdminController {
    @Autowired
    private IAdminService adminService;

    private final Logger LOG = LogManager.getLogger(AdminController.class);

    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        model.addAttribute("appointments", adminService.getAllAppointments());
        LOG.trace("Admin page accessed");
        return "admin";
    }

    @PostMapping("/admin/update")
    public @ResponseBody
    int updateAppointment(@RequestParam int id, @RequestParam String vaccineReceived) throws CentreNotFoundException {
        adminService.updateApptVaccineReceived(id, vaccineReceived);
        LOG.trace("Admin updated appointment with ID: " + id);
        return id;
    }
}
