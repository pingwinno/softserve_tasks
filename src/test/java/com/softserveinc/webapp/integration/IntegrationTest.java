package com.softserveinc.webapp.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserveinc.webapp.model.Role;
import com.softserveinc.webapp.model.User;
import com.softserveinc.webapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class IntegrationTest {
    @Autowired
    UserRepository userRepository;
    private User userWithID = new User();
    private User userWithoutID = new User();
    private String userJsonWithID;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() throws JsonProcessingException {
        userWithoutID.setName("user");
        userWithoutID.setPassword("somePass");
        userWithoutID.setDescription("some user");
        userWithoutID.setRole(Role.ADMIN);
        userWithID.setName("user");
        userWithID.setPassword("somePass");
        userWithID.setDescription("some user");
        userWithID.setRole(Role.ADMIN);
        ObjectMapper objectMapper = new ObjectMapper();
        userWithID = userRepository.save(userWithoutID);
        userJsonWithID = objectMapper.writeValueAsString(userWithID);
    }


    //Test get method
    @Test
    public void shouldReturnUserWhenCallGet() throws Exception {
        this.mockMvc.perform(get("/user/{id}", userWithID.getId())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(userJsonWithID)));
    }

    //Test get method with request non existing user
    @Test
    public void shouldReturn404WhenCallGet() throws Exception {
        UUID uuid = UUID.randomUUID();
        this.mockMvc.perform(get("/user/{id}", uuid)).andDo(print()).andExpect(status().isNotFound());
    }

    //Test add method
    @Test
    public void shouldReturn200WhenCallPostForAdd() throws Exception {
        this.mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(userJsonWithID))
                .andDo(print())
                .andExpect(status().isOk());
    }


    //Test update method
    @Test
    public void shouldReturn200WhenCallPatchForUpdate() throws Exception {
        this.mockMvc.perform(patch("/user/{id}", userWithID.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(userJsonWithID))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn404WhenCallPatchForUpdateWithNonExistingUser() throws Exception {
        this.mockMvc.perform(patch("/user/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(userJsonWithID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    //Test delete method
    @Test
    public void shouldReturn200WhenCallDelete() throws Exception {
        this.mockMvc.perform(delete("/user/{id}", userWithID.getId())).andDo(print()).andExpect(status().isOk());
    }

    //Test delete method with request non existing user
    @Test
    public void shouldReturn404WhenCallDelete() throws Exception {
        this.mockMvc.perform(delete("/user/{id}", UUID.randomUUID()))
                .andDo(print()).andExpect(status().isNotFound());
    }
}
