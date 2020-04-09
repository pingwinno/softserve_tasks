package com.softserveinc.webapp.service;

import com.softserveinc.webapp.exception.UserAlreadyExistsException;
import com.softserveinc.webapp.exception.UserNotFoundException;
import com.softserveinc.webapp.exception.WrongParamsException;
import com.softserveinc.webapp.model.User;


public interface UserService {
    User getUser(long id) throws UserNotFoundException;

    void addUser(User user) throws UserAlreadyExistsException, WrongParamsException;

    void updateUser(long id, User user) throws UserNotFoundException, WrongParamsException;

    void deleteUser(long id) throws UserNotFoundException;

}
