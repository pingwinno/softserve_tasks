package com.softserveinc.webapp.service;

import com.softserveinc.webapp.exception.UserAlreadyExistsException;
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
        int i = 0;
        user.setId(i);
        user.setName("user" + i);
        user.setPassword("somePass" + i);
        user.setDescription("some awesome user" + i);
        user.setRole(Role.ADMIN);
    }

    //Test user search by id
    @Test
    void shouldReturnUserWhenCallGetUser() throws UserNotFoundException {
        when(userRepository.findById(0L)).thenReturn(Optional.of(user));
        assertNotNull(userService.getUser(0));
    }

    //Test handling empty search result
    @Test
    void shouldThrowUserNotFoundExceptionWhenCallGetUser() {
        when(userRepository.findById(0L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUser(0));
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

    //Test duplicate handling
    @Test
    void shouldThrowUserAlreadyExistsExceptionWhenCallAddUserThatAlreadyPresentInDB() {
        when(userRepository.findById(0L)).thenReturn(Optional.of(user));
        assertThrows(UserAlreadyExistsException.class, () -> userService.addUser(user));
    }

    //Test adding user
    @Test
    void shouldThrowNothingWhenCallUpdateUser() {
        when(userRepository.findById(0L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        assertDoesNotThrow(() -> userService.updateUser(0, user));
    }

    //Test handling update non existing user
    @Test
    void shouldThrowUserNotFoundExceptionWhenCallUpdateUserThatNotExist() {
        when(userRepository.findById(0L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(0, user));
    }

    //Test handling update with id mismatch in params and user object
    @Test
    void shouldThrowIllegalArgumentExceptionWhenCallUpdateUserWithIdMismatch() {
        assertThrows(WrongParamsException.class, () -> userService.updateUser(1, user));
    }

    //Test handling update with wrong user object
    @Test
    void shouldThrowIllegalArgumentExceptionWhenCallUpdateUserWithIncorrectInput() {
        user.setPassword("");
        assertThrows(WrongParamsException.class, () -> userService.updateUser(0, user));
    }

    //Test user delete by id
    @Test
    void shouldThrowNothingWhenCallDeleteUser() {
        when(userRepository.findById(0L)).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> userService.deleteUser(0));
    }

    //Test handling delete non existing user
    @Test
    void shouldThrowUserNotFoundExceptionWhenCallDeleteUser() {
        when(userRepository.findById(0L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(0));
    }
}