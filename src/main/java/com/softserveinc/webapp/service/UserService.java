package com.softserveinc.webapp.service;

import com.softserveinc.webapp.exception.UserNotFoundException;
import com.softserveinc.webapp.exception.WrongParamsException;
import com.softserveinc.webapp.model.User;

import java.util.UUID;


public interface UserService {
    User getUser(UUID id) throws UserNotFoundException;

    User addUser(User user) throws WrongParamsException;

    void updateUser(UUID id, User user) throws UserNotFoundException, WrongParamsException;

    void deleteUser(UUID id) throws UserNotFoundException;
}
