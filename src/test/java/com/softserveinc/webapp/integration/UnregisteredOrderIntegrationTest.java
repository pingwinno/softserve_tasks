package com.softserveinc.webapp.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserveinc.webapp.model.Item;
import com.softserveinc.webapp.model.OrderState;
import com.softserveinc.webapp.model.UnregisteredOrder;
import com.softserveinc.webapp.repository.ItemRepository;
import com.softserveinc.webapp.repository.UnregisteredOrderRepository;
import com.softserveinc.webapp.utils.EntitiesGenerator;
import com.softserveinc.webapp.utils.PhoneNumberGenerator;
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

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UnregisteredOrderIntegrationTest {

    @Autowired
    private UnregisteredOrderRepository bucketRepository;
    @Autowired
    private ItemRepository itemRepository;
    private UnregisteredOrder orderWithID = new UnregisteredOrder();
    private UnregisteredOrder orderWithoutID = new UnregisteredOrder();
    private String orderJsonWithID;
    private String orderJsonArray;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() throws JsonProcessingException {
        long phoneNumber = PhoneNumberGenerator.generate();
        orderWithoutID.setPhoneNumber(phoneNumber);
        orderWithID.setPhoneNumber(phoneNumber);
        itemRepository.saveAll(EntitiesGenerator.generateItems());
        orderWithoutID.setItems((List<Item>) itemRepository.findAll());
        orderWithID.setItems(orderWithoutID.getItems());
        orderWithID.setOrderState(OrderState.DELIVERING);
        orderWithoutID.setOrderState(OrderState.DELIVERING);
        ObjectMapper objectMapper = new ObjectMapper();
        orderWithID = bucketRepository.save(orderWithID);
        orderJsonWithID = objectMapper.writeValueAsString(orderWithID);
        orderJsonArray = objectMapper.writeValueAsString(Collections.singletonList(orderWithID));
    }


    @Test
    @DisplayName("Test get method")
    public void shouldReturnItemWhenCallGet() throws Exception {
        this.mockMvc.perform(get("/unregistered_order/{id}", orderWithID.getId())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(orderJsonWithID)));
    }

    @Test
    @DisplayName("Test getAll method")
    public void shouldReturnItemWhenCallGetAll() throws Exception {
        this.mockMvc.perform(get("/unregistered_order")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(orderJsonArray));
    }

    @Test
    @DisplayName("Test search by name")
    public void shouldReturnItemWhenCallSearchByPhoneNumber() throws Exception {
        this.mockMvc.perform(get("/unregistered_order/search?{field}={value}", "phone",
                Long.toString(orderWithID.getPhoneNumber())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(orderJsonWithID)));
    }

    @Test
    @DisplayName("Test get method with request non existing unregistered_order")
    public void shouldReturn404WhenCallGet() throws Exception {
        UUID uuid = UUID.randomUUID();
        this.mockMvc.perform(get("/unregistered_order/{id}", uuid)).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test add method")
    public void shouldReturn200WhenCallPostForAdd() throws Exception {
        this.mockMvc.perform(post("/unregistered_order")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(orderJsonWithID))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(content().string(containsString(orderJsonWithID)));
    }

    @Test
    @DisplayName("Test update method")
    public void shouldReturn200WhenCallPatchForUpdate() throws Exception {
        this.mockMvc.perform(patch("/unregistered_order/{id}", orderWithID.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(orderJsonWithID))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test update method when unregistered_order is not present")
    public void shouldReturn404WhenCallPatchForUpdateWithNonExistingUser() throws Exception {
        this.mockMvc.perform(patch("/unregistered_order/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJsonWithID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test delete method")
    public void shouldReturn200WhenCallDelete() throws Exception {
        this.mockMvc.perform(delete("/unregistered_order/{id}", orderWithID.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("Test delete method with request non existing unregistered_order")
    public void shouldReturn404WhenCallDelete() throws Exception {
        UUID uuid = UUID.randomUUID();
        this.mockMvc.perform(delete("/unregistered_order/{id}", uuid))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
