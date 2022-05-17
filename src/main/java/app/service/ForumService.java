package app.service;

import app.exception.InvalidUserInputException;
import app.persistence.model.Post;
import app.persistence.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class ForumService implements IForumService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private IUserService userService;

    @Override
    public Post createNewPost(String content){
        validateInput(content);
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

    private void validateInput(String content) {
        // Check that inputted question only contains characters, numbers, ', ., ?, and is between 2 and 240 characters
        Pattern pattern = Pattern.compile("^([A-Za-z0-9'.,? ]){2,340}$");
        Matcher matcher = pattern.matcher(content);
        if (!matcher.matches()) {
            throw new InvalidUserInputException("Invalid input. Please ensure your question is between 2 and 340 characters and only contains letters, numbers and the symbols ', ., ? and ,.");
        }
    }
}
