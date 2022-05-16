package app.controller;

import app.exception.InvalidUserInputException;
import app.exception.UserAgeException;
import app.exception.UserAlreadyExistException;
import app.exception.WeakPasswordException;
import app.service.IUserService;
import app.persistence.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@Controller
public class RegistrationController {
    @Autowired
    private IUserService userService;

    private final Logger LOG = LogManager.getLogger(RegistrationController.class);

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "registration.html";
    }

    @PostMapping("/registration")
    public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid final User user, final HttpServletRequest request, final Errors errors) throws UnsupportedEncodingException {

        try {
            final User registered = userService.registerNewUserAccount(user);
            LOG.trace("User \"" + user.getUserName() + "\" registered");
            ModelAndView mav = new ModelAndView("registration", "user", user);
            mav.addObject("success", userService.generateQRUrl(registered));
            return mav;
        } catch (final UserAlreadyExistException uaeEx) {
            ModelAndView mav = new ModelAndView("registration", "user", user);
            mav.addObject("error", "An account for that username/email already exists.");
            return mav;
        } catch (final UserAgeException | WeakPasswordException | InvalidUserInputException uaeEx) {
            ModelAndView mav = new ModelAndView("registration", "user", user);
            mav.addObject("error", uaeEx.getMessage());
            return mav;
        }
    }
}
