package com.softserveinc.webapp.controller;

import com.softserveinc.webapp.model.User;
import com.softserveinc.webapp.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUser() {
        return userService.getAll();
    }

    @GetMapping(path = "search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUser(@RequestParam Map<String, String> request) {
        return userService.getBy(request);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUser(@PathVariable UUID id) {
        return userService.get(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public User addUser(@RequestBody User user) {
        return userService.add(user);
    }

    @PatchMapping("/{id}")
    public void updateUser(@PathVariable UUID id, @RequestBody User user) {
        userService.update(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.delete(id);
    }
}
