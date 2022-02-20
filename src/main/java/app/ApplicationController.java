package app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {

    @GetMapping("/")
    public String index() {
        return "index page";
    }

    @GetMapping("/testloggedin")
    public String testLoggedIn() {
        return "I am logged in";
    }
}
