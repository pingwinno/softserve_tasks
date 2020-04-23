package com.softserveinc.webapp.service;

import com.softserveinc.webapp.exception.NotFoundException;
import com.softserveinc.webapp.exception.WrongParamsException;
import com.softserveinc.webapp.model.Item;
import com.softserveinc.webapp.repository.ItemRepository;
import com.softserveinc.webapp.service.interfaces.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ItemServiceTest {

    private Item item = new Item();
    @MockBean
    private ItemRepository itemRepository;
    @Autowired
    private ItemService itemService;

    @BeforeEach
    public void init() {
        UUID uuid = UUID.randomUUID();
        item.setId(uuid);
        item.setName("item");
        item.setPrice(BigDecimal.TEN);
        item.setDescription("some awesome item" + uuid);
        item.setCategory("category");

    }

    @Test
    @DisplayName("Test item search by id")
    void shouldReturnUserWhenCallGetItem() throws NotFoundException {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        assertNotNull(itemService.get(item.getId()));
    }

    @Test
    @DisplayName("Test handling empty search result")
    void shouldThrowUserNotFoundExceptionWhenCallGetItem() {
        UUID uuid = UUID.randomUUID();
        when(itemRepository.findById(uuid)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemService.get(uuid));
    }

    @Test
    @DisplayName("Test item getAll")
    void shouldReturnItemWhenCallGetAll() {
        List<Item> items = new LinkedList<>() {
            {
                add(item);
            }
        };
        when(itemRepository.findAll()).thenReturn(items);
        assertEquals(items, itemService.getAll());
    }

    @Test
    @DisplayName("Test item getBy category")
    void shouldReturnItemWhenCallGetByCategory() throws WrongParamsException {
        List<Item> items = new LinkedList<>() {
            {
                add(item);
            }
        };
        when(itemRepository.findByCategory("category")).thenReturn(items);
        assertEquals("category", itemService.getBy(Collections.singletonMap("category", "category")).get(0).getCategory());
    }

    @Test
    @DisplayName("Test item getBy name")
    void shouldReturnItemWhenCallGetByName() throws WrongParamsException {
        List<Item> items = new LinkedList<>() {
            {
                add(item);
            }
        };
        when(itemRepository.findByName("item")).thenReturn(items);
        assertEquals("item", itemService.getBy(Collections.singletonMap("name", "item")).get(0).getName());
    }

    @Test
    @DisplayName("Test item getBy name and category")
    void shouldReturnItemWhenCallGetByNameAndCategory() throws WrongParamsException {
        List<Item> items = new LinkedList<>() {
            {
                add(item);
            }
        };
        when(itemRepository.findByNameAndCategory("item","category")).thenReturn(items);
        assertEquals("item", itemService.getBy(Map.of("name", "item","category","category"))
                .get(0).getName());
    }

    @Test
    @DisplayName("Test adding item")
    void shouldThrowsNothingWhenCallAddUser() {
        when(itemRepository.save(item)).thenReturn(item);
        assertDoesNotThrow(() -> itemService.add(item));
    }

    @Test
    @DisplayName("Test handling wrong params validation")
    void shouldThrowIllegalArgumentExceptionWhenCallAddUserWithIncorrectInput() {
        item.setPrice(null);
        assertThrows(WrongParamsException.class, () -> itemService.add(item));
    }

    @Test
    @DisplayName("Test adding item")
    void shouldThrowNothingWhenCallUpdateUser() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);
        assertDoesNotThrow(() -> itemService.update(item.getId(), item));
    }

    @Test
    @DisplayName("Test handling update non existing item")
    void shouldThrowUserNotFoundExceptionWhenCallUpdateUserThatNotExist() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemService.update(item.getId(), item));
    }

    @Test
    @DisplayName("Test handling update with wrong item object")
    void shouldThrowIllegalArgumentExceptionWhenCallUpdateUserWithIncorrectInput() {
        item.setPrice(null);
        assertThrows(WrongParamsException.class, () -> itemService.update(item.getId(), item));
    }

    @Test
    @DisplayName("Test item delete by id")
    void shouldThrowNothingWhenCallDeleteUser() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        assertDoesNotThrow(() -> itemService.delete(item.getId()));
    }

    @Test
    @DisplayName("Test handling delete non existing item")
    void shouldThrowUserNotFoundExceptionWhenCallDeleteUser() {
        UUID uuid = UUID.randomUUID();
        when(itemRepository.findById(uuid)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemService.delete(uuid));
    }
}