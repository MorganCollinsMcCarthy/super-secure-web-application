package app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {

    @GetMapping("/")
    public String index() {
        return "index page";
    }

    @GetMapping("/user")
    public String user() {
        return "User logged in";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Admin logged in";
    }
}
