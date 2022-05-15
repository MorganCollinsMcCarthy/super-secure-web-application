package app.service;

import app.exception.InvalidUserInputException;
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
import java.util.Optional;
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
        validateInputs(user);
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
        String username = ((Optional<User>) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).get().getUserName();
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

    private void validateInputs(User user) {
        // Check Firstname
        Pattern patternName = Pattern.compile("^([A-Za-z]){2,64}$");
        Matcher matcherFirstname = patternName.matcher(user.getFirst_name());
        if (!matcherFirstname.matches()) {
            throw new InvalidUserInputException("Firstname must only contain letters and have a length between 2 and 64.");
        }
        // Check Surname
        Matcher matcherSurname = patternName.matcher(user.getSurname());
        if (!matcherSurname.matches()) {
            throw new InvalidUserInputException("Surname must only contain letters and have a length between 2 and 64.");
        }
        // Check DOB
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate newDate = LocalDate.parse(user.getDate_of_birth(), formatter);
        }
        catch (Exception e) {
            throw new InvalidUserInputException("Invalid Date of Birth.");
        }
        // Check Gender
        if (!(user.getGender().equalsIgnoreCase("female") || user.getGender().equalsIgnoreCase("male"))) {
            throw new InvalidUserInputException("Invalid gender.");
        }
        // Check Phone Number
        Pattern patternNumber = Pattern.compile("^([0-9]){10}$");
        Matcher matcherNumber = patternNumber.matcher(user.getPhone_number());
        if (!matcherNumber.matches()) {
            throw new InvalidUserInputException("Phone number must contain 10 digits.");
        }
        // Check Email
        Pattern patternEmail = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcherEmail = patternEmail.matcher(user.getUserName());
        if (!matcherEmail.matches()) {
            throw new InvalidUserInputException("Invalid email.");
        }
        // Check Nationality
        Pattern patternNationality = Pattern.compile("^([A-Za-z]){2,64}$");
        Matcher matcherNationality = patternNationality.matcher(user.getNationality());
        if (!matcherNationality.matches()) {
            throw new InvalidUserInputException("Nationality must only contain letters and have a length between 2 and 64.");
        }
        // Check PPSN
        Pattern patternPPSN = Pattern.compile("^([0-9]{7})([A-Z]{1,2})$");
        Matcher matcherPPSN = patternPPSN.matcher(user.getPpsn());
        if (!matcherPPSN.matches()) {
            throw new InvalidUserInputException("Invalid PPSN.");
        }
    }
}