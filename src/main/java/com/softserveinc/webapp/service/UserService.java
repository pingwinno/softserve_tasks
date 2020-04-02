package com.softserveinc.webapp.service;

import com.softserveinc.webapp.exception.UserAlreadyExistsException;
import com.softserveinc.webapp.exception.UserNotFoundException;
import com.softserveinc.webapp.model.User;
import com.softserveinc.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.LinkedList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    Validator validator;
    @Autowired
    private UserRepository userRepository;

    public User getUser(long id) throws UserNotFoundException {
        return userRepository
                .findById(id)
                .orElseThrow(
                        UserNotFoundException::new);
    }

    public void addUser(User user) throws UserAlreadyExistsException {
        validate(user);
        if (userRepository.findById(user.getId()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        userRepository.save(user);
    }

    public void updateUser(long id, User user) throws UserNotFoundException {
        if (id != user.getId()) {
            throw new IllegalArgumentException();
        }
        validate(user);
        userRepository
                .findById(id)
                .orElseThrow(
                        UserNotFoundException::new);
        userRepository.save(user);
    }

    public void deleteUser(long id) throws UserNotFoundException {
        userRepository.delete(userRepository
                .findById(id)
                .orElseThrow(
                        UserNotFoundException::new));
    }

    private void validate(User user) {
        List<ConstraintViolation<User>> validations = new LinkedList<>(validator.validate(user));
        if (validations.size() != 0) {
            throw new IllegalArgumentException(validations.get(0).getMessage());
        }
    }
}
