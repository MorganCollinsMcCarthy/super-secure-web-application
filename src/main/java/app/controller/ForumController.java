package app.controller;

import app.persistence.model.Post;
import app.persistence.repository.PostRepository;
import app.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class ForumController {


    @Autowired
    private PostRepository postRepository;

    @Autowired
    private IUserService userService;

    @GetMapping("/forum")
    public String showForum(Model model) {
        System.out.println(postRepository.findAll());
        model.addAttribute("posts",postRepository.findAll());
        return "forum.html";
    }

    @PostMapping("/forum/add")
    public @ResponseBody
    Post addPost(@RequestBody String content) {
        Post post = new Post();
        post.setContent(content);
        post.setUser(userService.getAuthenticatedUser());
        return postRepository.save(post);
    }
}

