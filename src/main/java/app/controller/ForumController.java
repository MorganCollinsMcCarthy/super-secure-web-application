package app.controller;

import app.exception.InvalidUserInputException;
import app.service.IForumService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class ForumController {
    @Autowired
    private IForumService forumService;

    private final Logger LOG = LogManager.getLogger(ForumController.class);

    @GetMapping("/forum")
    public String showForum(Model model) {
        model.addAttribute("posts",forumService.findAll());
        return "forum.html";
    }

    @PostMapping("/forum/add")
    public String addPost(@RequestParam(value="content") String content, Model model) {
        try {
            forumService.createNewPost(content);
        } catch (InvalidUserInputException e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("posts",forumService.findAll());
        return "forum.html";
    }

    @PostMapping("admin/forum/reply")
    public @ResponseBody
    String addReply(@RequestParam int id, @RequestParam String reply){
        LOG.trace("Admin replied to question with ID: " + id);
        return forumService.createNewReply(id,reply);
    }
}

