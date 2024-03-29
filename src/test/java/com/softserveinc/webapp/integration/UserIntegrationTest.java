package com.softserveinc.webapp.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserveinc.webapp.model.Role;
import com.softserveinc.webapp.model.User;
import com.softserveinc.webapp.repository.UserRepository;
import com.softserveinc.webapp.utils.PhoneNumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:UserIntegrationTest"
        , "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
        , "spring.jpa.hibernate.ddl-auto=create"})
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    private User userWithID = new User();
    private User userWithoutID = new User();
    private String userJsonWithID;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() throws JsonProcessingException {
        long phoneNumber = PhoneNumberGenerator.generate();
        userWithoutID.setName("user");
        userWithoutID.setPassword("somePass");
        userWithoutID.setDescription("some user");
        userWithoutID.setRole(Role.ADMIN);
        userWithoutID.setPhoneNumber(phoneNumber);
        userWithID.setName("user");
        userWithID.setPassword("somePass");
        userWithID.setDescription("some user");
        userWithID.setRole(Role.ADMIN);
        userWithID.setPhoneNumber(phoneNumber);
        ObjectMapper objectMapper = new ObjectMapper();
        userWithID = userRepository.save(userWithoutID);
        userJsonWithID = objectMapper.writeValueAsString(userWithID);
    }


    @Test
    @DisplayName("Test get method")
    public void shouldReturnUserWhenCallGet() throws Exception {
        this.mockMvc.perform(get("/user/{id}", userWithID.getId())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(userJsonWithID)));
    }

    @Test
    @DisplayName("Test search by name")
    public void shouldReturnUserWhenCallGetByName() throws Exception {
        this.mockMvc.perform(get("/user/search?{field}={value}", "name", userWithID.getName()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(userJsonWithID)));
    }

    @Test
    @DisplayName("Test get method with request non existing user")
    public void shouldReturn404WhenCallGet() throws Exception {
        UUID uuid = UUID.randomUUID();
        this.mockMvc.perform(get("/user/{id}", uuid)).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test add method")
    public void shouldReturn200WhenCallPostForAdd() throws Exception {
        this.mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(userJsonWithID))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test update method")
    public void shouldReturn200WhenCallPutForUpdate() throws Exception {
        this.mockMvc.perform(put("/user/{id}", userWithID.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(userJsonWithID))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test patch method with non existing user")
    public void shouldReturn404WhenCallPutForUpdateWithNonExistingUser() throws Exception {
        this.mockMvc.perform(put("/user/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(userJsonWithID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test delete method")
    public void shouldReturn200WhenCallDelete() throws Exception {
        this.mockMvc.perform(delete("/user/{id}", userWithID.getId())).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test delete method with request non existing user")
    public void shouldReturn404WhenCallDelete() throws Exception {
        this.mockMvc.perform(delete("/user/{id}", UUID.randomUUID()))
                .andDo(print()).andExpect(status().isNotFound());
    }
}
