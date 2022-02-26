package app.controller;

import app.exception.CentreNotFoundException;
import app.persistence.model.Appointment;
import app.persistence.model.Post;
import app.service.IForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class ForumController {


    @Autowired
    private IForumService forumService;

    @GetMapping("/forum")
    public String showForum(Model model) {
        model.addAttribute("posts",forumService.findAll());
        return "forum.html";
    }

    @PostMapping("/forum/add")
    public @ResponseBody
    Post addPost(@RequestBody String content) {
        return forumService.createNewPost(content);
    }

    @PostMapping("admin/forum/reply")
    public @ResponseBody
    String addReply(@RequestParam int id, @RequestParam String reply){
        return forumService.createNewReply(id,reply);
    }
}

