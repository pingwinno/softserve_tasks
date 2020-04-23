package com.softserveinc.webapp.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

class ItemValidationTest {

    private Set<ConstraintViolation<Item>> violations;
    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();
    private Item item = new Item();

    @BeforeEach
    void init() {
        UUID uuid = UUID.randomUUID();
        item.setId(uuid);
        item.setName("item");
        item.setPrice(BigDecimal.TEN);
        item.setDescription("some awesome item" + uuid);
        item.setCategory("category");
    }

    @Test
    @DisplayName("Test name field validation")
    void shouldReturnNameViolationWhenNameEmpty() {
        item.setName("");
        violations = validator.validate(item);
        violations
                .forEach(
                        userConstraintViolation
                                -> Assertions.assertEquals(
                                "name", userConstraintViolation.getPropertyPath().toString()));
    }

    @Test
    @DisplayName("Test price field validation")
    void shouldReturnPasswordViolationWhenPasswordShorterThan8Symbols() {
        item.setPrice(null);
        violations = validator.validate(item);
        violations
                .forEach(
                        userConstraintViolation
                                -> Assertions.assertEquals(
                                "price", userConstraintViolation.getPropertyPath().toString()));
    }

    @Test
    @DisplayName("Test category field validation")
    void shouldReturnDescriptionViolationWhenDescriptionIsBlank() {
        item.setCategory("   ");
        violations = validator.validate(item);
        violations
                .forEach(
                        userConstraintViolation
                                -> Assertions.assertEquals(
                                "category", userConstraintViolation.getPropertyPath().toString()));
    }
}