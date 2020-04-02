package com.softserveinc.webapp.controller;

import com.softserveinc.webapp.exception.UserAlreadyExistsException;
import com.softserveinc.webapp.exception.UserNotFoundException;
import com.softserveinc.webapp.model.User;
import com.softserveinc.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        try {
            return userService.getUser(id);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format("User with id=%s not found", id), e);
        }
    }

    @PostMapping
    public void addUser(@RequestBody User user) {
        try {
            userService.addUser(user);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "User params are wrong", e);
        } catch (UserAlreadyExistsException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, String.format("User with id %s already exists", user.getId()), e);
        }
    }

    @PatchMapping("/{id}")
    public void updateUser(@PathVariable long id, @RequestBody User user) {
        try {
            userService.updateUser(id, user);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "User params are wrong", e);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format("User with id=%s not found", id), e);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        try {
            userService.deleteUser(id);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format("User with id=%s not found", id), e);
        }
    }

}
