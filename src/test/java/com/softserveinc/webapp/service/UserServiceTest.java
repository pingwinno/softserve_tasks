package com.softserveinc.webapp.service;

import com.softserveinc.webapp.exception.UserNotFoundException;
import com.softserveinc.webapp.exception.WrongParamsException;
import com.softserveinc.webapp.model.Role;
import com.softserveinc.webapp.model.User;
import com.softserveinc.webapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

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
        user.setName("user" + uuid);
        user.setPassword("somePass" + uuid);
        user.setDescription("some awesome user" + uuid);
        user.setRole(Role.ADMIN);
    }

    //Test user search by id
    @Test
    void shouldReturnUserWhenCallGetUser() throws UserNotFoundException {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        assertNotNull(userService.getUser(user.getId()));
    }

    //Test handling empty search result
    @Test
    void shouldThrowUserNotFoundExceptionWhenCallGetUser() {
        UUID uuid = UUID.randomUUID();
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUser(uuid));
    }

    //Test adding user
    @Test
    void shouldThrowsNothingWhenCallAddUser() {
        when(userRepository.save(user)).thenReturn(user);
        assertDoesNotThrow(() -> userService.addUser(user));
    }

    //Test handling wrong params validation
    @Test
    void shouldThrowIllegalArgumentExceptionWhenCallAddUserWithIncorrectInput() {
        user.setPassword("");
        assertThrows(WrongParamsException.class, () -> userService.addUser(user));
    }

    //Test adding user
    @Test
    void shouldThrowNothingWhenCallUpdateUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        assertDoesNotThrow(() -> userService.updateUser(user.getId(), user));
    }

    //Test handling update non existing user
    @Test
    void shouldThrowUserNotFoundExceptionWhenCallUpdateUserThatNotExist() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(user.getId(), user));
    }

    //Test handling update with wrong user object
    @Test
    void shouldThrowIllegalArgumentExceptionWhenCallUpdateUserWithIncorrectInput() {
        user.setPassword("");
        assertThrows(WrongParamsException.class, () -> userService.updateUser(user.getId(), user));
    }

    //Test user delete by id
    @Test
    void shouldThrowNothingWhenCallDeleteUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> userService.deleteUser(user.getId()));
    }

    //Test handling delete non existing user
    @Test
    void shouldThrowUserNotFoundExceptionWhenCallDeleteUser() {
        UUID uuid = UUID.randomUUID();
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(uuid));
    }
}