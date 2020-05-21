package com.softserveinc.webapp.repository;

import com.softserveinc.webapp.model.Item;
import com.softserveinc.webapp.model.RegisteredOrder;
import com.softserveinc.webapp.model.Role;
import com.softserveinc.webapp.model.User;
import com.softserveinc.webapp.utils.EntitiesGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class RegisteredOrderRepositoryTest {

    @Autowired
    private RegisteredOrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    private List<RegisteredOrder> orders = new ArrayList<>();

    @BeforeEach
    void init() {
        orders.clear();
        List<User> users = (List<User>) userRepository.saveAll(EntitiesGenerator.generateUsers());
        List<Item> items = (List<Item>) itemRepository.saveAll(EntitiesGenerator.generateItems());
        orderRepository.saveAll(EntitiesGenerator.generateRegisteredOrders(users, items)).forEach(orders::add);
    }

    @Test
    @DisplayName("Should find order with given id")
    void shouldBeEqualsWhenCallFindBucketById() {
        RegisteredOrder order = orders.get(0);
        Assertions.assertEquals(order, orderRepository.findById(order.getId()).orElseThrow());
    }

    @Test
    @DisplayName("Should find order with given user id")
    void shouldBeEqualsWhenCallFindBucketByUserId() {
        RegisteredOrder order = orders.get(0);
        Assertions.assertEquals(order, orderRepository.findByUserId(order.getUser().getId()).get(0));
    }

    @Test
    @DisplayName("Test findAll method and compare result with generated set")
    void shouldBeEqualsWhenCallFindAll() {
        List<RegisteredOrder> bucketsCallResult = new ArrayList<>();
        orderRepository.findAll().forEach(bucketsCallResult::add);
        Assertions.assertTrue(bucketsCallResult.containsAll(orders));
    }

    @Test
    @DisplayName("Test saveAll method and try to find each added element")
    void shouldBeEqualsWhenSaveAll() {
        for (RegisteredOrder order : orders) {
            Assertions.assertEquals(order, orderRepository.findById(order.getId()).orElse(null));
        }
    }

    @Test
    @DisplayName("Should successfully save new user")
    void shouldNotThrowWhenSaveRecord() {
        User user = new User();
        RegisteredOrder order = new RegisteredOrder();
        UUID userID = UUID.randomUUID();
        user.setId(userID);
        user.setName("user");
        user.setPassword("somePass");
        user.setDescription("some awesome user");
        user.setRole(Role.USER);
        UUID bucketID = UUID.randomUUID();
        order.setId(bucketID);
        Assertions.assertDoesNotThrow(() -> {
            order.setUser(userRepository.save(user));
            orderRepository.save(order);
        });
    }

    @Test
    @DisplayName("Should successfully update old user")
    void shouldReturnNewValueWhenUpdateRecord() {
        RegisteredOrder order = orders.get(0);
        User user = order.getUser();
        int i = new Random().nextInt();
        user.setName("shiro" + i);
        user.setPassword("somePass" + i);
        user.setDescription("some awesome user" + i);
        orderRepository.save(order);
        Assertions.assertEquals(order, orderRepository.findByUserId(user.getId()).get(0));
    }

    @Test
    @DisplayName("Should successfully delete user and existsById should return false")
    void shouldReturnFalseWhenDeleteRecord() {
        RegisteredOrder order = orderRepository.findAll().get(3);
        Assertions.assertTrue(orderRepository.existsById(order.getId()));
        orderRepository.delete(order);
        Assertions.assertFalse(orderRepository.existsById(order.getId()));
    }
}