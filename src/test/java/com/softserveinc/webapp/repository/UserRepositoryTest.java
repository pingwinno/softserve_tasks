package com.softserveinc.webapp.repository;

import com.softserveinc.webapp.model.Role;
import com.softserveinc.webapp.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private List<User> users = generateData();

    @BeforeEach
    void init() {
        userRepository.deleteAll();
        userRepository.saveAll(users);
    }

    //Should find user with id 0
    @Test
    void shouldBeEqualsWhenCallFindUserById() {
        User user = users.get(0);
        Assertions.assertEquals(user, userRepository.findById(user.getId()).orElseThrow());
    }

    //Test findAll method and compare result with generated set
    @Test
    void shouldBeEqualsWhenCallFindAll() {
        Iterable<User> iterable = users;
        Assertions.assertEquals(iterable, userRepository.findAll());
    }

    //Test saveAll method and try to find each added element
    @Test
    void shouldBeEqualsWhenSaveAll() {
        userRepository.deleteAll();
        userRepository.saveAll(users);
        for (User user : users) {
            Assertions.assertEquals(user, userRepository.findById(user.getId()).orElseThrow());
        }
    }

    //Should successfully save new user
    @Test
    void shouldNotThrowWhenSaveRecord() {
        int i = 30;
        User user = new User();
        user.setId(i);
        user.setName("shiro" + i);
        user.setPassword("somePass" + i);
        user.setDescription("some awesome user" + i);
        user.setRole(Role.ADMIN);
        Assertions.assertDoesNotThrow(() -> userRepository.save(user));
    }

    //Should successfully update old user
    @Test
    void shouldReturnNewValueWhenUpdateRecord() {
        User user = userRepository.findById(0L).orElseThrow();
        int i = 100;
        user.setName("shiro" + i);
        user.setPassword("somePass" + i);
        user.setDescription("some awesome user" + i);
        userRepository.save(user);
        Assertions.assertEquals(user, userRepository.findById(user.getId()).orElseThrow());
    }

    //Should successfully delete user and existsById should return false
    @Test
    void shouldReturnFalseWhenDeleteRecord() {
        User user = users.get(0);
        userRepository.delete(user);
        Assertions.assertFalse(userRepository.existsById(user.getId()));
    }

    private List<User> generateData() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            User user = new User();
            user.setId(i);
            user.setName("shiro" + i);
            user.setPassword("somePass" + i);
            user.setDescription("some awesome user" + i);
            user.setRole(Role.ADMIN);
            users.add(user);
        }
        return users;
    }
}