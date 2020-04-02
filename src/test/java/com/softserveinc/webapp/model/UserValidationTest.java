package com.softserveinc.webapp.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

class UserValidationTest {

    private Set<ConstraintViolation<User>> violations;
    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();
    private User user;

    @BeforeEach
    void init() {
        user = new User();
        long i = 0;
        user.setId(i);
        user.setName("name" + i);
        user.setPassword("somePass" + i);
        user.setDescription("some awesome user" + i);
        user.setRole(Role.ADMIN);
    }

    //Test name field validation
    @Test
    void shouldReturnNameViolationWhenNameEmpty() {
        user.setName("");
        violations = validator.validate(user);
        violations
                .forEach(
                        userConstraintViolation
                                -> Assertions.assertEquals(
                                "name", userConstraintViolation.getPropertyPath().toString()));
    }

    //Test password field validation
    @Test
    void shouldReturnPasswordViolationWhenPasswordShorterThan8Symbols() {
        user.setPassword("12345");
        violations = validator.validate(user);
        violations
                .forEach(
                        userConstraintViolation
                                -> Assertions.assertEquals(
                                "password", userConstraintViolation.getPropertyPath().toString()));
    }

    //Test description field validation
    @Test
    void shouldReturnDescriptionViolationWhenDescriptionIsBlank() {
        user.setDescription("   ");
        violations = validator.validate(user);
        violations
                .forEach(
                        userConstraintViolation
                                -> Assertions.assertEquals(
                                "description", userConstraintViolation.getPropertyPath().toString()));
    }

    //Test description field validation
    @Test
    void shouldReturnRoleViolationWhenRoleIsNull() {
        user.setRole(null);
        violations = validator.validate(user);
        violations
                .forEach(
                        userConstraintViolation
                                -> Assertions.assertEquals(
                                "role", userConstraintViolation.getPropertyPath().toString()));
    }
}