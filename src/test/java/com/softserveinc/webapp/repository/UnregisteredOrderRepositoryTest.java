package com.softserveinc.webapp.repository;

import com.softserveinc.webapp.model.Item;
import com.softserveinc.webapp.model.UnregisteredOrder;
import com.softserveinc.webapp.utils.EntitiesGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Random;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class UnregisteredOrderRepositoryTest {

    @Autowired
    private UnregisteredOrderRepository orderRepository;
    @Autowired
    private ItemRepository itemRepository;
    private List<UnregisteredOrder> orders;

    @BeforeEach
    void init() {
        orders = (List<UnregisteredOrder>) orderRepository.saveAll(EntitiesGenerator
                .generateUnregisteredOrders((List<Item>) itemRepository.saveAll(EntitiesGenerator.generateItems())));
    }

    @Test
    @DisplayName("Should find order with given id")
    void shouldBeEqualsWhenCallFindBucketById() {
        UnregisteredOrder order = orders.get(0);
        Assertions.assertEquals(order, orderRepository.findById(order.getId()).orElseThrow());
    }

    @Test
    @DisplayName("Should find order with given phone id")
    void shouldBeEqualsWhenCallFindBucketByUserId() {
        UnregisteredOrder order = orders.get(0);
        Assertions.assertEquals(order, orderRepository.findByPhoneNumber(order.getPhoneNumber()).get(0));
    }

    @Test
    @DisplayName("Test findAll method and compare result with generated set")
    void shouldBeEqualsWhenCallFindAll() {
        List<UnregisteredOrder> bucketsCallResult = (List<UnregisteredOrder>) orderRepository.findAll();
        Assertions.assertTrue(bucketsCallResult.containsAll(orders));
    }

    @Test
    @DisplayName("Test saveAll method and try to find each added element")
    void shouldBeEqualsWhenSaveAll() {
        for (UnregisteredOrder order : orders) {
            Assertions.assertEquals(order, orderRepository.findById(order.getId()).orElse(null));
        }
    }

    @Test
    @DisplayName("Should successfully save new phone")
    void shouldNotThrowWhenSaveRecord() {
        UnregisteredOrder order = new UnregisteredOrder();
        order.setPhoneNumber(new Random().nextInt());
        Assertions.assertDoesNotThrow(() -> {
            orderRepository.save(order);
        });
    }

    @Test
    @DisplayName("Should successfully add items")
    void shouldReturnNewValueWhenUpdateRecord() {
        UnregisteredOrder order = orders.get(0);
        List<Item> items = (List<Item>) itemRepository.saveAll(EntitiesGenerator.generateItems());
        order.setItems(items);
        orderRepository.save(order);
        Assertions.assertEquals(order, orderRepository.findById(order.getId()).orElseThrow());
    }

    @Test
    @DisplayName("Should successfully delete items and existsById should return false")
    void shouldReturnFalseWhenDeleteRecord() {
        UnregisteredOrder order = null;
        for (UnregisteredOrder unregisteredOrder : orderRepository.findAll()) {
            order = unregisteredOrder;
            return;
        }
        Assertions.assertTrue(orderRepository.existsById(order.getId()));
        orderRepository.delete(order);
        Assertions.assertFalse(orderRepository.existsById(order.getId()));
    }
}