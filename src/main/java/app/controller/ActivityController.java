package app.controller;

import app.persistence.model.User;
import app.service.IActivityService;
import app.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ActivityController {
    @Autowired
    private IActivityService activityService;

    @Autowired
    private IUserService userService;

    @GetMapping("/activity")
    public String viewActivity(Model model) {
        User authenticatedUser = userService.getAuthenticatedUser();
        model.addAttribute("user", authenticatedUser);
        return "activity";
    }

    @PostMapping("/activity/cancelAppointment")
    public String cancelAppointment() {
        activityService.cancelAppointmentForUser();
        return "cancelledAppointment";
    }
}
