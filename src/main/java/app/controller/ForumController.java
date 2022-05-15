package app.controller;

import app.persistence.model.Post;
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
    public @ResponseBody
    Post addPost(@RequestBody String content) {
        return forumService.createNewPost(content);
    }

    @PostMapping("admin/forum/reply")
    public @ResponseBody
    String addReply(@RequestParam int id, @RequestParam String reply){
        LOG.trace("Admin replied to question with ID: " + id);
        return forumService.createNewReply(id,reply);
    }
}

