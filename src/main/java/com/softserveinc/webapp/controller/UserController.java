package com.softserveinc.webapp.controller;

import com.softserveinc.webapp.exception.UserAlreadyExistsException;
import com.softserveinc.webapp.exception.UserNotFoundException;
import com.softserveinc.webapp.exception.WrongParamsException;
import com.softserveinc.webapp.model.User;
import com.softserveinc.webapp.service.UserService;
import com.softserveinc.webapp.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) throws UserNotFoundException {
        return userService.getUser(id);
    }

    @PostMapping
    public void addUser(@RequestBody User user) throws UserAlreadyExistsException, WrongParamsException {
        userService.addUser(user);
    }

    @PatchMapping("/{id}")
    public void updateUser(@PathVariable long id, @RequestBody User user) throws UserNotFoundException, WrongParamsException {
        userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) throws UserNotFoundException {
        userService.deleteUser(id);
    }

}
