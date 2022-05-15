package app.security.google2fa;

import app.persistence.model.User;
import app.persistence.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Optional;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    private final Logger LOG = LogManager.getLogger(CustomAuthenticationProvider.class);

    @Override
    public Authentication authenticate(Authentication auth)
            throws AuthenticationException {
        LOG.trace("Attempted login to account: \"" + auth.getName() + "\"");
        System.out.println(auth.getName());
        String verificationCode
                = ((CustomWebAuthenticationDetails) auth.getDetails())
                .getVerificationCode();
        Optional<User> user = userRepository.findByUserName(auth.getName());

        if ((!user.isPresent())) {
            LOG.trace("Authentication failed: Account does not exist");
            throw new BadCredentialsException("Invalid username or password");
        }
        if (user.get().isUsing2FA()) {
            Totp totp = new Totp(user.get().getSecret());
            if (!isValidLong(verificationCode) || !totp.verify(verificationCode)) {
                LOG.trace("Authentication failed: Invalid verification code");
                throw new BadCredentialsException("Invalid verification code");
            }
        }

        Authentication result = super.authenticate(auth);
        if (result.isAuthenticated()) {
            LOG.trace("Successful login to account: \"" + user.get().getUserName() + "\"");
        }

        return new UsernamePasswordAuthenticationToken(
                user, result.getCredentials(), result.getAuthorities());
    }

    private boolean isValidLong(String code) {
        try {
            Long.parseLong(code);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
