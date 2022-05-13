package app.service;

import app.exception.UserAgeException;
import app.exception.WeakPasswordException;
import app.persistence.model.User;
import app.persistence.repository.UserRepository;
import app.exception.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static String APP_NAME = "SuperSecureWebApplication";

    public static String QR_PREFIX =
            "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";

    @Override
    public String generateQRUrl(User user) throws UnsupportedEncodingException {
        System.out.println("ss"+user.getSecret());
        return QR_PREFIX + URLEncoder.encode(String.format(
                        "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                        APP_NAME, user.getUserName(), user.getSecret(), APP_NAME),
                "UTF-8");
    }

    @Override
    public User registerNewUserAccount(User user){
        if (userNameExists(user.getUserName())) {
            throw new UserAlreadyExistException("Email address already exists: "
                    + user.getUserName());
        }
        if (userUnderAge(user.getDate_of_birth())) {
            throw new UserAgeException("Must be over 18");
        }
        if (weakPassword(user.getPassword())) {
            throw new WeakPasswordException("Password length must be between 8 and 32 and must contain at least 1 lowercase, 1 uppercase, 1 number and 1 symbol.");
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
        newUser.setUsing2FA(true);

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

    private boolean weakPassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,32}$");
        Matcher matcher = pattern.matcher(password);
        return !matcher.matches();
    }
}