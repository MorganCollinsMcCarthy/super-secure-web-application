package app.controller;

import app.exception.CentreNotFoundException;
import app.service.IAdminService;
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

    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        model.addAttribute("appointments", adminService.getAllAppointments());
        return "admin";
    }

    @PostMapping("/admin/update")
    public @ResponseBody
    int updateAppointment(@RequestParam int id, @RequestParam String vaccineReceived) throws CentreNotFoundException {
        adminService.updateApptVaccineReceived(id, vaccineReceived);
        return id;
    }
}
