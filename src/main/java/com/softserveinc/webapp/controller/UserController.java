package com.softserveinc.webapp.controller;

import com.softserveinc.webapp.exception.UserAlreadyExistsException;
import com.softserveinc.webapp.exception.UserNotFoundException;
import com.softserveinc.webapp.exception.WrongParamsException;
import com.softserveinc.webapp.model.User;
import com.softserveinc.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable UUID id) throws UserNotFoundException {
        return userService.getUser(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public User addUser(@RequestBody User user) throws WrongParamsException {
        return userService.addUser(user);
    }

    @PatchMapping("/{id}")
    public void updateUser(@PathVariable UUID id, @RequestBody User user) throws UserNotFoundException, WrongParamsException {
        userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) throws UserNotFoundException {
        userService.deleteUser(id);
    }

}
