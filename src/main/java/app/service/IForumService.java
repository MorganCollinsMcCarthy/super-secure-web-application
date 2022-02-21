package app.service;

import app.persistence.model.Post;

import java.util.List;

public interface IForumService {
    Post createNewPost(String content);
    List<Post> findAll();
}
