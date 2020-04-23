package com.softserveinc.webapp.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserveinc.webapp.model.Item;
import com.softserveinc.webapp.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ItemIntegrationTest {

    @Autowired
    private ItemRepository itemRepository;
    private Item itemWithID = new Item();
    private Item itemWithoutID = new Item();
    private String itemJsonWithID;
    private String itemJsonArray;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() throws JsonProcessingException {
        itemWithoutID.setName("item");
        itemWithoutID.setPrice(BigDecimal.TEN);
        itemWithoutID.setDescription("some item");
        itemWithoutID.setCategory("category");
        itemWithID.setName("item");
        itemWithID.setPrice(BigDecimal.TEN);
        itemWithID.setDescription("some item");
        itemWithID.setCategory("category");
        ObjectMapper objectMapper = new ObjectMapper();
        itemWithID = itemRepository.save(itemWithoutID);
        itemJsonWithID = objectMapper.writeValueAsString(itemWithID);
        itemJsonArray = objectMapper.writeValueAsString(Collections.singletonList(itemWithID));
    }


    @Test
    @DisplayName("Test get method")
    public void shouldReturnItemWhenCallGet() throws Exception {
        this.mockMvc.perform(get("/item/{id}", itemWithID.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(itemJsonWithID));
    }

    @Test
    @DisplayName("Test get all method")
    public void shouldReturnItemWhenCallGetAll() throws Exception {
        this.mockMvc.perform(get("/item")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(itemJsonArray));
    }

    @Test
    @DisplayName("Test get method with request non existing item")
    public void shouldReturn404WhenCallGet() throws Exception {
        UUID uuid = UUID.randomUUID();
        this.mockMvc.perform(get("/item/{id}", uuid)).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test search by name")
    public void shouldReturnItemWhenCallSearchByName() throws Exception {
        this.mockMvc.perform(get("/item/search?{field}={value}", "name", itemWithID.getName()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(itemJsonArray));
    }

    @Test
    @DisplayName("Test search by category")
    public void shouldReturnItemWhenCallSearchByCategory() throws Exception {
        this.mockMvc.perform(get("/item/search?{field}={value}", "category", itemWithID.getCategory()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(itemJsonArray));
    }


    @Test
    @DisplayName("Test add method")
    public void shouldReturn200WhenCallPostForAdd() throws Exception {
        this.mockMvc.perform(post("/item")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(itemJsonWithID))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test update method")
    public void shouldReturn200WhenCallPatchForUpdate() throws Exception {
        this.mockMvc.perform(patch("/item/{id}", itemWithID.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(itemJsonWithID))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test patch method with non existing item")
    public void shouldReturn404WhenCallPatchForUpdateWithNonExistingItem() throws Exception {
        this.mockMvc.perform(patch("/item/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(itemJsonWithID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test delete method")
    public void shouldReturn200WhenCallDelete() throws Exception {
        this.mockMvc.perform(delete("/item/{id}", itemWithID.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test delete method with request non existing item")
    public void shouldReturn404WhenCallDelete() throws Exception {
        this.mockMvc.perform(delete("/item/{id}", UUID.randomUUID()))
                .andDo(print()).andExpect(status().isNotFound());
    }
}
