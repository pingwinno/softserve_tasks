package com.softserveinc.webapp.repository;

import com.softserveinc.webapp.model.Item;
import com.softserveinc.webapp.utils.EntitiesGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    private List<Item> items = new ArrayList<>();


    @BeforeEach
    void init() {
        items.clear();
        itemRepository.saveAll(EntitiesGenerator.generateItems()).forEach(item -> {
            items.add(item);
        });
    }

    @Test
    @DisplayName("Should find item with given id")
    void shouldBeEqualsWhenCallFindUserById() {
        Item item = items.get(0);
        Assertions.assertEquals(item, itemRepository.findById(item.getId()).orElseThrow());
    }

    @Test
    @DisplayName("Should find item with given name")
    void shouldBeEqualsWhenCallFindUserByName() {
        Item item = items.get(0);
        Assertions.assertEquals(item, itemRepository.findByName(item.getName()).get(0));
    }

    @Test
    @DisplayName("Should find item with given id")
    void shouldBeEqualsWhenCallFindUserByCategory() {
        Item item = items.get(0);
        Assertions.assertEquals(item, itemRepository.findByCategory(item.getCategory()).get(0));
    }

    @Test
    @DisplayName("Test findAll method and compare result with generated set")
    void shouldBeEqualsWhenCallFindAll() {
        List<Item> itemsCallResult = new ArrayList<>();
        itemRepository.findAll().forEach(itemsCallResult::add);
        Assertions.assertTrue(itemsCallResult.containsAll(items));
    }

    @Test
    @DisplayName("Test saveAll method and try to find each added element")
    void shouldBeEqualsWhenSaveAll() {
        for (Item item : items) {
            Assertions.assertEquals(item, itemRepository.findById(item.getId()).orElse(null));
        }
    }

    @Test
    @DisplayName("Should successfully save new item")
    void shouldNotThrowWhenSaveRecord() {
        UUID uuid = UUID.randomUUID();
        Item item = new Item();
        item.setId(uuid);
        item.setName("item");
        item.setPrice(BigDecimal.TEN);
        item.setDescription("some awesome item" + uuid);
        item.setCategory("category");
        Assertions.assertDoesNotThrow(() -> itemRepository.save(item));
    }

    @Test
    @DisplayName("Should successfully update old user")
    void shouldReturnNewValueWhenUpdateRecord() {
        Item item = items.get(0);
        int i = 100;
        item.setName("shiro" + i);
        item.setPrice(BigDecimal.valueOf(i));
        item.setDescription("some awesome user" + i);
        itemRepository.save(item);
        Assertions.assertEquals(item, itemRepository.findById(item.getId()).orElseThrow());
    }

    @Test
    @DisplayName("Should successfully delete user and existsById should return false")
    void shouldReturnFalseWhenDeleteRecord() {
        UUID uuid = UUID.randomUUID();
        Item item = new Item();
        item.setId(uuid);
        item.setName("item");
        item.setPrice(BigDecimal.TEN);
        item.setDescription("some awesome item" + uuid);
        item.setCategory("category");
        item = itemRepository.save(item);
        Assertions.assertTrue(itemRepository.existsById(item.getId()));
        itemRepository.delete(item);
        Assertions.assertFalse(itemRepository.existsById(item.getId()));
    }
}