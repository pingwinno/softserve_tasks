package com.softserveinc.webapp.integration;

import com.softserveinc.webapp.model.Role;
import com.softserveinc.webapp.model.User;
import com.softserveinc.webapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")

public class IntegrationTest {
    private static User user = new User();
    @Autowired
    UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;

    private String userJson = "{\"id\":%s,\"name\":\"user0\"" +
            ",\"password\":\"somePass0\"" +
            ",\"description\":\"some awesome user0\"" +
            ",\"role\":\"ADMIN\"}";

    @BeforeAll
    public static void init() {
        int i = 0;
        user.setId(i);
        user.setName("user" + i);
        user.setPassword("somePass" + i);
        user.setDescription("some awesome user" + i);
        user.setRole(Role.ADMIN);
    }

    //Test get method
    @Test
    public void shouldReturnUserWhenCallGet() throws Exception {
        user = userRepository.save(user);
        this.mockMvc.perform(get("/user/{id}", user.getId())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        String.format(userJson, user.getId()))));
    }

    //Test get method with request non existing user
    @Test
    public void shouldReturn404WhenCallGet() throws Exception {
        this.mockMvc.perform(get("/user/{id}", 1000)).andDo(print()).andExpect(status().isNotFound());
    }

    //Test add method
    @Test
    public void shouldReturn200WhenCallPostForAdd() throws Exception {
        this.mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(String.format(userJson, user.getId() + 1)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    //Test add method with duplicated id
    @Test
    public void shouldReturn400WhenCallPostWithDuplicatedObjects() throws Exception {
        user = userRepository.save(user);
        this.mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(String.format(userJson, user.getId())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    //Test update method
    @Test
    public void shouldReturn200WhenCallPatchForUpdate() throws Exception {
        user = userRepository.save(user);
        this.mockMvc.perform(patch("/user/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(String.format(userJson, user.getId())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    //Test update method with id mismatch in path and body request
    @Test
    public void shouldReturn400WhenCallPatchForUpdateWithIDsMismatch() throws Exception {
        this.mockMvc.perform(patch("/user/{id}", user.getId() + 100)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(String.format(userJson, user.getId())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn404WhenCallPatchForUpdateWithNonExistingUser() throws Exception {
        this.mockMvc.perform(patch("/user/{id}", 1000)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(String.format(userJson, 1000)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    //Test delete method
    @Test
    public void shouldReturn200WhenCallDelete() throws Exception {
        user = userRepository.save(user);
        this.mockMvc.perform(delete("/user/{id}", user.getId())).andDo(print()).andExpect(status().isOk());
    }

    //Test delete method with request non existing user
    @Test
    public void shouldReturn404WhenCallDelete() throws Exception {
        this.mockMvc.perform(delete("/user/{id}", 1000)).andDo(print()).andExpect(status().isNotFound());
    }
}
