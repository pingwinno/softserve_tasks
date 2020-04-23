package com.softserveinc.webapp.service;

import com.softserveinc.webapp.exception.NotFoundException;
import com.softserveinc.webapp.exception.WrongParamsException;
import com.softserveinc.webapp.model.*;
import com.softserveinc.webapp.service.interfaces.BasketService;
import com.softserveinc.webapp.service.interfaces.RegisteredOrderService;
import com.softserveinc.webapp.service.interfaces.UnregisteredOrderService;
import com.softserveinc.webapp.service.interfaces.UserService;
import com.softserveinc.webapp.utils.EntitiesGenerator;
import com.softserveinc.webapp.utils.PhoneNumberGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
class BasketServiceTest {

    private List<Item> items;
    private RegisteredOrder registeredOrder;
    private UnregisteredOrder unregisteredOrder;

    @MockBean
    private Basket sessionBasket;

    @MockBean
    private RegisteredOrderService registeredOrderService;

    @MockBean
    private UnregisteredOrderService unregisteredOrderService;

    @MockBean
    private UserService userService;

    @Autowired
    private BasketService basketService;

    @BeforeEach
    void init() {
        items = EntitiesGenerator.generateItems();
        registeredOrder = new RegisteredOrder();
        registeredOrder.setItems(items);
        registeredOrder.setOrderState(OrderState.PROCESSING);
        registeredOrder.setId(UUID.randomUUID());
        registeredOrder.setUser(EntitiesGenerator.generateUsers().get(0));
        unregisteredOrder = new UnregisteredOrder();
        unregisteredOrder.setItems(items);
        unregisteredOrder.setOrderState(OrderState.PROCESSING);
        unregisteredOrder.setPhoneNumber(PhoneNumberGenerator.generate());
    }

    @Test
    @DisplayName("Test getBasket method")
    void shouldReturnItemsListWhenCallGetAll() {
        when(sessionBasket.getItems()).thenReturn(items);
        Assertions.assertEquals(items, basketService.getBucket(sessionBasket));
    }

    @Test
    @DisplayName("Test add item method")
    void shouldDoesntThrowWhenCallAdd() {
        doNothing().when(sessionBasket).addItem(items.get(0));
        Assertions.assertDoesNotThrow(() -> basketService.addItem(items.get(0), sessionBasket));
    }

    @Test
    @DisplayName("Test remove success item method")
    void shouldDoesntThrowWhenCallRemove() {
        UUID uuid = UUID.randomUUID();
        when(sessionBasket.removeItem(uuid)).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> basketService.removeItem(uuid, sessionBasket));
    }

    @Test
    @DisplayName("Test remove failed item method")
    void shouldThrowNotFoundExceptionCallRemove() {
        UUID uuid = UUID.randomUUID();
        when(sessionBasket.removeItem(uuid)).thenReturn(false);
        Assertions.assertThrows(NotFoundException.class, () -> basketService.removeItem(uuid, sessionBasket));
    }

    @Test
    @DisplayName("Test checkout with registered user")
    void shouldReturnUUIDWhenCallCheckOutRegisteredOrder() throws WrongParamsException {
        RegisteredOrder registeredOrderWithId = new RegisteredOrder();
        registeredOrderWithId.setId(UUID.randomUUID());
        registeredOrder.setId(null);
        when(sessionBasket.getItems()).thenReturn(items);
        when(registeredOrderService.add(registeredOrder)).thenReturn(registeredOrderWithId);
        when(userService.getBy(Map.of("phone", String.valueOf(registeredOrder.getUser().getPhoneNumber()))))
                .thenReturn(List.of(registeredOrder.getUser()));
        Assertions.assertEquals(registeredOrderWithId.getId(), basketService.checkout(registeredOrder.getUser()
                .getPhoneNumber(), sessionBasket));
    }

    @Test
    @DisplayName("Test checkout with registered user")
    void shouldReturnUUIDWhenCallCheckOutUnregisteredOrder() throws WrongParamsException {
        UnregisteredOrder unregisteredOrderWithId = new UnregisteredOrder();
        unregisteredOrderWithId.setId(UUID.randomUUID());
        registeredOrder.setId(null);
        when(sessionBasket.getItems()).thenReturn(items);
        when(unregisteredOrderService.add(unregisteredOrder)).thenReturn(unregisteredOrderWithId);
        when(userService.getBy(Map.of("phone", String.valueOf(registeredOrder.getUser().getPhoneNumber()))))
                .thenReturn(Collections.emptyList());
        Assertions.assertEquals(unregisteredOrderWithId.getId(),
                basketService.checkout(unregisteredOrder.getPhoneNumber(), sessionBasket));
    }
}