package app.service;

import app.exception.UserAgeException;
import app.persistence.model.User;
import app.persistence.repository.UserRepository;
import app.exception.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerNewUserAccount(User user){
        if (userNameExists(user.getUserName())) {
            throw new UserAlreadyExistException("Email address already exists: "
                    + user.getUserName());
        }
        if (userUnderAge(user.getDate_of_birth())) {
            throw new UserAgeException("Must be over 18");
        }

        User newUser = new User();
        newUser.setFirst_name(user.getFirst_name());
        newUser.setSurname(user.getSurname());
        newUser.setDate_of_birth(user.getDate_of_birth());
        newUser.setPhone_number(user.getPhone_number());
        newUser.setUserName(user.getUserName());
        newUser.setNationality(user.getNationality());
        newUser.setPpsn(user.getPpsn());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setActive(true);
        newUser.setRole("ROLE_USER");
        newUser.setGender(user.getGender());

        return userRepository.save(newUser);
    }

    @Override
    public User getAuthenticatedUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(username).get();
        return user;
    }

    private boolean userNameExists(String userName) {
        return userRepository.findByUserName(userName).isPresent();
    }
    private boolean userUnderAge(String dob) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate newDate = LocalDate.parse(dob, formatter);
        return Period.between(newDate, LocalDate.now()).getYears() < 18;
    }

}