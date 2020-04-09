package com.softserveinc.webapp.service;

import com.softserveinc.webapp.exception.UserAlreadyExistsException;
import com.softserveinc.webapp.exception.UserNotFoundException;
import com.softserveinc.webapp.exception.WrongParamsException;
import com.softserveinc.webapp.model.User;
import com.softserveinc.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.LinkedList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    Validator validator;
    @Autowired
    private UserRepository userRepository;

    public User getUser(long id) throws UserNotFoundException {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new
                        UserNotFoundException(String.format("User with id=%s not found", id)));
    }

    public void addUser(User user) throws UserAlreadyExistsException, WrongParamsException {
        validate(user);
        if (userRepository.findById(user.getId()).isPresent()) {
            throw new UserAlreadyExistsException(String.format("User with id %s already exists", user.getId()));
        }
        userRepository.save(user);
    }

    public void updateUser(long id, User user) throws UserNotFoundException, WrongParamsException {
        if (id != user.getId()) {
            throw new WrongParamsException("Id in path and in request body are not equals");
        }
        validate(user);
        userRepository
                .findById(id)
                .orElseThrow(
                        () -> new
                                UserNotFoundException(String.format("User with id=%s not found", id)));
        userRepository.save(user);
    }

    public void deleteUser(long id) throws UserNotFoundException {
        userRepository.delete(userRepository
                .findById(id)
                .orElseThrow(
                        () -> new
                                UserNotFoundException(String.format("User with id=%s not found", id))));
    }

    private void validate(User user) throws WrongParamsException {
        List<ConstraintViolation<User>> validations = new LinkedList<>(validator.validate(user));
        if (validations.size() != 0) {
            throw new WrongParamsException(validations.get(0).getMessage());
        }
    }
}
