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
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {
    private static User user = new User();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    private String userJson = "{\"id\":0,\"name\":\"user0\"" +
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
        userRepository.save(user);
        this.mockMvc.perform(get("/user/0")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        userJson)));
        userRepository.deleteAll();
    }

    //Test get method with request non existing user
    @Test
    public void shouldReturn404WhenCallGet() throws Exception {
        this.mockMvc.perform(get("/user/1")).andDo(print()).andExpect(status().isNotFound());
    }

    //Test add method
    @Test
    public void shouldReturn200WhenCallPostForAdd() throws Exception {
        this.mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(userJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    //Test add method with duplicated id
    @Test
    public void shouldReturn400WhenCallPostWithDuplicatedObjects() throws Exception {
        userRepository.save(user);
        this.mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(userJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        userRepository.deleteAll();
    }

    //Test update method
    @Test
    public void shouldReturn200WhenCallPatchForUpdate() throws Exception {
        userRepository.save(user);
        this.mockMvc.perform(patch("/user/0")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(userJson))
                .andDo(print())
                .andExpect(status().isOk());
        userRepository.deleteAll();
    }

    //Test update method with id mismatch in path and body request
    @Test
    public void shouldReturn400WhenCallPatchForUpdateWithIDsMismatch() throws Exception {
        this.mockMvc.perform(patch("/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(userJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        userRepository.deleteAll();
    }

    @Test
    public void shouldReturn404WhenCallPatchForUpdateWithNonExistingUser() throws Exception {
        userRepository.deleteAll();
        this.mockMvc.perform(patch("/user/0")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(userJson))
                .andDo(print())
                .andExpect(status().isNotFound());
        userRepository.deleteAll();
    }

    //Test delete method
    @Test
    public void shouldReturn200WhenCallDelete() throws Exception {
        userRepository.save(user);
        this.mockMvc.perform(delete("/user/0")).andDo(print()).andExpect(status().isOk());
    }

    //Test delete method with request non existing user
    @Test
    public void shouldReturn404WhenCallDelete() throws Exception {
        userRepository.deleteAll();
        this.mockMvc.perform(delete("/user/1")).andDo(print()).andExpect(status().isNotFound());
    }
}
