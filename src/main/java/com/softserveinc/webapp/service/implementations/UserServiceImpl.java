package com.softserveinc.webapp.service.implementations;

import com.softserveinc.webapp.exception.NotFoundException;
import com.softserveinc.webapp.exception.WrongParamsException;
import com.softserveinc.webapp.model.User;
import com.softserveinc.webapp.repository.UserRepository;
import com.softserveinc.webapp.service.DataValidator;
import com.softserveinc.webapp.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private DataValidator dataValidator;
    @Autowired
    private UserRepository userRepository;

    public User get(UUID id) throws NotFoundException {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new
                        NotFoundException(String.format("User with id=%s not found", id)));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getBy(Map<String, String> request) throws WrongParamsException {
        if (request.containsKey("name")) {
            return userRepository.findByName(request.get("name"));
        }
        if (request.containsKey("phone")) {
            return userRepository.findByPhoneNumber(Long.parseLong(request.get("phone")));
        }
        throw new WrongParamsException(String.format("key %s not found", request));
    }

    public User add(User user) throws WrongParamsException {
        dataValidator.validate(user);
        return userRepository.save(user);
    }

    public void update(UUID id, User user) throws NotFoundException, WrongParamsException {
        dataValidator.validate(user);
        userRepository
                .findById(id)
                .orElseThrow(
                        () -> new
                                NotFoundException(String.format("User with id=%s not found", id)));
        userRepository.save(user);
    }

    public void delete(UUID id) throws NotFoundException {
        userRepository.delete(userRepository
                .findById(id)
                .orElseThrow(
                        () -> new
                                NotFoundException(String.format("User with id=%s not found", id))));
    }
}
