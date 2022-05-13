package app.service;

import app.persistence.model.User;

import java.io.UnsupportedEncodingException;

public interface IUserService {
    User registerNewUserAccount(User user);
    User getAuthenticatedUser();
    String generateQRUrl(User user) throws UnsupportedEncodingException;
}
