package com.softserveinc.webapp.service;

import com.softserveinc.webapp.exception.NotFoundException;
import com.softserveinc.webapp.exception.WrongParamsException;
import com.softserveinc.webapp.model.Role;
import com.softserveinc.webapp.model.User;
import com.softserveinc.webapp.repository.UserRepository;
import com.softserveinc.webapp.service.interfaces.UserService;
import com.softserveinc.webapp.utils.PhoneNumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    private User user = new User();
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @BeforeEach
    public void init() {
        UUID uuid = UUID.randomUUID();
        user.setId(uuid);
        user.setName("user");
        user.setPassword("somePass");
        user.setDescription("some awesome user");
        user.setPhoneNumber(PhoneNumberGenerator.generate());
        user.setRole(Role.ADMIN);
    }

    @Test
    @DisplayName("Test user search by id")
    void shouldReturnUserWhenCallGetUser() throws NotFoundException {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        assertNotNull(userService.get(user.getId()));
    }

    @Test
    @DisplayName("Test user getAll")
    void shouldReturnUserWhenCallGetAll() {
        List<User> users = new LinkedList<>() {
            {
                add(user);
            }
        };
        when(userRepository.findAll()).thenReturn(users);
        assertNotNull(userService.getAll());
    }

    @Test
    @DisplayName("Test user getBy name")
    void shouldReturnItemWhenCallGetByName() throws WrongParamsException {
        List<User> users = new LinkedList<>() {
            {
                add(user);
            }
        };
        when(userRepository.findByName("user")).thenReturn(users);
        assertEquals("user", userService.getBy(Collections.singletonMap("name", "user")).get(0).getName());
    }

    @Test
    @DisplayName("Test handling empty search result")
    void shouldThrowUserNotFoundExceptionWhenCallGetUser() {
        UUID uuid = UUID.randomUUID();
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.get(uuid));
    }

    @Test
    @DisplayName("Test adding user")
    void shouldThrowsNothingWhenCallAddUser() {
        when(userRepository.save(user)).thenReturn(user);
        assertDoesNotThrow(() -> userService.add(user));
    }

    @Test
    @DisplayName("Test handling wrong params validation")
    void shouldThrowIllegalArgumentExceptionWhenCallAddUserWithIncorrectInput() {
        user.setPassword("");
        assertThrows(WrongParamsException.class, () -> userService.add(user));
    }

    @Test
    @DisplayName("Test adding user")
    void shouldThrowNothingWhenCallUpdateUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        assertDoesNotThrow(() -> userService.update(user.getId(), user));
    }

    @Test
    @DisplayName("Test handling update non existing user")
    void shouldThrowUserNotFoundExceptionWhenCallUpdateUserThatNotExist() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.update(user.getId(), user));
    }

    @Test
    @DisplayName("Test handling update with wrong user object")
    void shouldThrowIllegalArgumentExceptionWhenCallUpdateUserWithIncorrectInput() {
        user.setPassword("");
        assertThrows(WrongParamsException.class, () -> userService.update(user.getId(), user));
    }

    @Test
    @DisplayName("Test user delete by id")
    void shouldThrowNothingWhenCallDeleteUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> userService.delete(user.getId()));
    }

    @Test
    @DisplayName("Test handling delete non existing user")
    void shouldThrowUserNotFoundExceptionWhenCallDeleteUser() {
        UUID uuid = UUID.randomUUID();
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.delete(uuid));
    }
}