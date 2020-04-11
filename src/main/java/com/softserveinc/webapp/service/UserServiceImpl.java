package com.softserveinc.webapp.service;

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
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    Validator validator;
    @Autowired
    private UserRepository userRepository;

    public User getUser(UUID id) throws UserNotFoundException {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new
                        UserNotFoundException(String.format("User with id=%s not found", id)));
    }

    public User addUser(User user) throws WrongParamsException {
        validate(user);
        return userRepository.save(user);
    }

    public void updateUser(UUID id, User user) throws UserNotFoundException, WrongParamsException {
        validate(user);
        userRepository
                .findById(id)
                .orElseThrow(
                        () -> new
                                UserNotFoundException(String.format("User with id=%s not found", id)));
        userRepository.save(user);
    }

    public void deleteUser(UUID id) throws UserNotFoundException {
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
