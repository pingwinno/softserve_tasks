package com.softserveinc.webapp.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserveinc.webapp.model.*;
import com.softserveinc.webapp.repository.ItemRepository;
import com.softserveinc.webapp.repository.RegisteredOrderRepository;
import com.softserveinc.webapp.repository.UserRepository;
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
public class RegisteredOrderIntegrationTest {

    @Autowired
    private RegisteredOrderRepository bucketRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    private RegisteredOrder orderWithID = new RegisteredOrder();
    private RegisteredOrder orderWithoutID = new RegisteredOrder();
    private String orderJsonWithID;
    private String orderJsonArray;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() throws JsonProcessingException {
        User user = new User();
        user.setName("user");
        user.setPassword("somePass");
        user.setDescription("some user");
        user.setRole(Role.ADMIN);
        user.setPhoneNumber(PhoneNumberGenerator.generate());
        user = userRepository.save(user);
        orderWithoutID.setUser(user);
        itemRepository.saveAll(EntitiesGenerator.generateItems());
        orderWithoutID.setItems((List<Item>) itemRepository.findAll());
        orderWithID.setOrderState(OrderState.DELIVERING);
        orderWithoutID.setOrderState(OrderState.DELIVERING);
        orderWithID.setUser(user);
        orderWithID.setItems(orderWithoutID.getItems());
        ObjectMapper objectMapper = new ObjectMapper();
        orderWithID = bucketRepository.save(orderWithID);
        orderJsonWithID = objectMapper.writeValueAsString(orderWithID);
        orderJsonArray = objectMapper.writeValueAsString(Collections.singletonList(orderWithID));
    }


    @Test
    @DisplayName("Test get method")
    public void shouldReturnItemWhenCallGet() throws Exception {
        this.mockMvc.perform(get("/registered_order/{id}", orderWithID.getId())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(orderJsonWithID)));
    }

    @Test
    @DisplayName("Test getAll method")
    public void shouldReturnItemWhenCallGetAll() throws Exception {
        this.mockMvc.perform(get("/registered_order")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(orderJsonArray));
    }

    @Test
    @DisplayName("Test search by name")
    public void shouldReturnItemWhenCallSearchByName() throws Exception {
        this.mockMvc.perform(get("/registered_order/search/?{field}={value}", "user_id",
                orderWithID.getUser().getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(orderJsonWithID)));
    }

    @Test
    @DisplayName("Test get method with request non existing registered_order")
    public void shouldReturn404WhenCallGet() throws Exception {
        UUID uuid = UUID.randomUUID();
        this.mockMvc.perform(get("/registered_order/{id}", uuid)).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test add method")
    public void shouldReturn200WhenCallPostForAdd() throws Exception {
        this.mockMvc.perform(post("/registered_order")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(orderJsonWithID))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(content().string(containsString(orderJsonWithID)));
    }

    @Test
    @DisplayName("Test update method")
    public void shouldReturn200WhenCallPatchForUpdate() throws Exception {
        this.mockMvc.perform(patch("/registered_order/{id}", orderWithID.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(orderJsonWithID))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test update method when registered_order is not present")
    public void shouldReturn404WhenCallPatchForUpdateWithNonExistingUser() throws Exception {
        this.mockMvc.perform(patch("/registered_order/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJsonWithID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test delete method")
    public void shouldReturn200WhenCallDelete() throws Exception {
        this.mockMvc.perform(delete("/registered_order/{id}", orderWithID.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("Test delete method with request non existing registered_order")
    public void shouldReturn404WhenCallDelete() throws Exception {
        UUID uuid = UUID.randomUUID();
        this.mockMvc.perform(delete("/registered_order/{id}", uuid))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
