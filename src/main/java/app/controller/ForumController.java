package app.controller;

import app.persistence.repository.PostRepository;
import app.service.IUserService;
import app.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class ForumController {


    @Autowired
    private PostRepository postRepository;

    @GetMapping("/forum")
    public String showForum(Model model) {
        System.out.println(postRepository.findAll());
        model.addAttribute("posts",postRepository.findAll());
        return "forum.html";
    }
}

