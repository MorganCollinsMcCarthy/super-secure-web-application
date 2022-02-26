package app.service;

import app.persistence.model.Post;
import app.persistence.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ForumService implements IForumService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private IUserService userService;

    @Override
    public Post createNewPost(String content){
        Post post = new Post();
        post.setContent(content);
        if(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)
            return postRepository.save(post);
        post.setUser(userService.getAuthenticatedUser());
        return postRepository.save(post);
    }

    @Override
    public String createNewReply(int id,String reply){
        postRepository.findById(id).get().replies.add(reply);
        return reply;
    }

    @Override
    public List<Post> findAll(){
        return postRepository.findAll();
    }

}
