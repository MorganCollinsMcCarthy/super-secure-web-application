package app.controller;

import app.service.IHomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private IHomeService homeService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("VaccinatedByNationality", homeService.countVaccinatedByNationality());
        model.addAttribute("MaleByAgeGroup", homeService.countMaleByAgeGroup());
        model.addAttribute("FemaleByAgeGroup", homeService.countFemaleByAgeGroup());
        return "index";
    }
}
