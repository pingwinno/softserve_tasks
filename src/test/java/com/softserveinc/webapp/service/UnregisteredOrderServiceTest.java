package com.softserveinc.webapp.service;

import com.softserveinc.webapp.exception.NotFoundException;
import com.softserveinc.webapp.exception.WrongParamsException;
import com.softserveinc.webapp.model.Item;
import com.softserveinc.webapp.model.OrderState;
import com.softserveinc.webapp.model.UnregisteredOrder;
import com.softserveinc.webapp.repository.UnregisteredOrderRepository;
import com.softserveinc.webapp.service.interfaces.UnregisteredOrderService;
import com.softserveinc.webapp.utils.PhoneNumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class UnregisteredOrderServiceTest {

    private UnregisteredOrder order = new UnregisteredOrder();
    @MockBean
    private UnregisteredOrderRepository orderRepository;
    @Autowired
    private UnregisteredOrderService orderService;

    @BeforeEach
    public void init() {
        UUID bucketID = UUID.randomUUID();
        order.setId(bucketID);
        order.setPhoneNumber(PhoneNumberGenerator.generate());
        Item item = new Item();
        item.setName("item");
        item.setPrice(BigDecimal.TEN);
        item.setDescription("some awesome item");
        item.setCategory("category");
        order.setOrderState(OrderState.DELIVERING);
        order.setItems(Collections.singletonList(item));
    }

    @Test
    @DisplayName("Test order search by id")
    void shouldReturnUserWhenCallGetItem() throws NotFoundException {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        assertNotNull(orderService.get(order.getId()));
    }

    @Test
    @DisplayName("Test handling empty search result")
    void shouldThrowUserNotFoundExceptionWhenCallGetItem() {
        UUID uuid = UUID.randomUUID();
        when(orderRepository.findById(uuid)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> orderService.get(uuid));
    }

    @Test
    @DisplayName("Test order getAll")
    void shouldReturnItemWhenCallGetAll() {
        List<UnregisteredOrder> buckets = new LinkedList<>() {
            {
                add(order);
            }
        };
        when(orderRepository.findAll()).thenReturn(buckets);
        assertEquals(buckets, orderService.getAll());
    }

    @Test
    @DisplayName("Test order getBy phone number")
    void shouldReturnItemWhenCallGetByCategory() throws WrongParamsException {
        List<UnregisteredOrder> buckets = new LinkedList<>() {
            {
                add(order);
            }
        };
        when(orderRepository.findByPhoneNumber(order.getPhoneNumber())).thenReturn(buckets);
        assertEquals(order.getPhoneNumber(), orderService.getBy(
                Collections.singletonMap("phone", String.valueOf(order.getPhoneNumber()))).get(0).getPhoneNumber());
    }

    @Test
    @DisplayName("Test adding order")
    void shouldThrowsNothingWhenCallAddUser() {
        when(orderRepository.save(order)).thenReturn(order);
        assertDoesNotThrow(() -> orderService.add(order));
    }

    @Test
    @DisplayName("Test handling wrong params validation")
    void shouldThrowIllegalArgumentExceptionWhenCallAddUserWithIncorrectInput() {
        order.setPhoneNumber(0);
        assertThrows(WrongParamsException.class, () -> orderService.add(order));
    }

    @Test
    @DisplayName("Test adding order")
    void shouldThrowNothingWhenCallUpdateUser() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        assertDoesNotThrow(() -> orderService.update(order.getId(), order));
    }

    @Test
    @DisplayName("Test handling update non existing order")
    void shouldThrowUserNotFoundExceptionWhenCallUpdateUserThatNotExist() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> orderService.update(order.getId(), order));
    }

    @Test
    @DisplayName("Test handling update with wrong order object")
    void shouldThrowIllegalArgumentExceptionWhenCallUpdateUserWithIncorrectInput() {
        order.setItems(null);
        assertThrows(WrongParamsException.class, () -> orderService.update(order.getId(), order));
    }

    @Test
    @DisplayName("Test item delete by id")
    void shouldThrowNothingWhenCallDeleteUser() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        assertDoesNotThrow(() -> orderService.delete(order.getId()));
    }

    @Test
    @DisplayName("Test handling delete non existing item")
    void shouldThrowUserNotFoundExceptionWhenCallDeleteUser() {
        UUID uuid = UUID.randomUUID();
        when(orderRepository.findById(uuid)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> orderService.delete(uuid));
    }
}