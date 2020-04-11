package com.softserveinc.webapp.repository;

import com.softserveinc.webapp.model.Role;
import com.softserveinc.webapp.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private List<User> users = new ArrayList<>();


    @BeforeEach
    void init() {
        users.clear();
        userRepository.saveAll(generateData()).forEach(users::add);
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
        List<User> usersCallResult = new ArrayList<>();
        userRepository.findAll().forEach(usersCallResult::add);
        Assertions.assertTrue(usersCallResult.containsAll(users));
    }

    //Test saveAll method and try to find each added element
    @Test
    void shouldBeEqualsWhenSaveAll() {
        for (User user : users) {
            Assertions.assertEquals(user, userRepository.findById(user.getId()).orElse(null));
        }
    }

    //Should successfully save new user
    @Test
    void shouldNotThrowWhenSaveRecord() {
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setId(uuid);
        user.setName("shiro" + uuid);
        user.setPassword("somePass" + uuid);
        user.setDescription("some awesome user" + uuid);
        user.setRole(Role.ADMIN);
        Assertions.assertDoesNotThrow(() -> userRepository.save(user));
    }

    //Should successfully update old user
    @Test
    void shouldReturnNewValueWhenUpdateRecord() {
        User user = users.get(0);
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
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setId(uuid);
        user.setName("shiro" + uuid);
        user.setPassword("somePass" + uuid);
        user.setDescription("some awesome user" + uuid);
        user.setRole(Role.ADMIN);
        user = userRepository.save(user);
        System.out.println(user);
        Assertions.assertTrue(userRepository.existsById(user.getId()));
        userRepository.delete(user);
        Assertions.assertFalse(userRepository.existsById(user.getId()));
    }

    private List<User> generateData() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            User user = new User();
            user.setId(UUID.randomUUID());
            user.setName("shiro" + i);
            user.setPassword("somePass" + i);
            user.setDescription("some awesome user" + i);
            user.setRole(Role.ADMIN);
            users.add(user);
        }
        return users;
    }
}