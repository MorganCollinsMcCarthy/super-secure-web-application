package app.controller;

import app.persistence.model.User;
import app.service.IActivityService;
import app.service.IUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private final Logger LOG = LogManager.getLogger(ActivityController.class);

    @GetMapping("/activity")
    public String viewActivity(Model model) {
        User authenticatedUser = userService.getAuthenticatedUser();
        model.addAttribute("user", authenticatedUser);
        LOG.trace("User \"" + authenticatedUser.getUserName() + "\" requested to view their activity");
        return "activity";
    }

    @PostMapping("/activity/cancelAppointment")
    public String cancelAppointment() {
        activityService.cancelAppointmentForUser();
        LOG.trace("User \"" + userService.getAuthenticatedUser().getUserName() + "\" cancelled upcoming appointment");
        return "cancelledAppointment";
    }
}
