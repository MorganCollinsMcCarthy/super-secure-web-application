package app.service;

import app.persistence.model.User;

public interface IUserService {
    User registerNewUserAccount(User user);
}
