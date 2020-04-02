package com.softserveinc.webapp.controller;

import com.softserveinc.webapp.model.Role;
import com.softserveinc.webapp.exception.UserAlreadyExistsException;
import com.softserveinc.webapp.exception.UserNotFoundException;
import com.softserveinc.webapp.model.User;
import com.softserveinc.webapp.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    private static User user = new User();
    private ConstraintDescriptions constraintDescriptions = new ConstraintDescriptions(User.class);
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
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

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    //Test get method
    @Test
    public void shouldReturnUserWhenCallGet() throws Exception {
        when(userService.getUser(0)).thenReturn(user);
        this.mockMvc.perform(get("/user/{id}", 0)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        userJson))).andDo(document("user/{methodName}"
                , pathParameters(parameterWithName("id").description("user id"))
                , responseFields(
                        fieldWithPath("id").description("user id").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("id")))
                        , fieldWithPath("name").description("name").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("name")))
                        , fieldWithPath("password").description("password").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("password")))
                        , fieldWithPath("description").description("description").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("description")))
                        , fieldWithPath("role").description("role").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("role"))))));
    }

    //Test get method with request non existing user
    @Test
    public void shouldReturn404WhenCallGet() throws Exception {
        when(userService.getUser(1)).thenThrow(UserNotFoundException.class);
        this.mockMvc.perform(get("/user/{id}", 1)).andDo(print()).andExpect(status().isNotFound())
                .andDo(document("user/{methodName}"
                        , pathParameters(parameterWithName("id").description("user id"))));
    }

    //Test add method
    @Test
    public void shouldReturn200WhenCallPostForAdd() throws Exception {
        doNothing().when(userService).addUser(user);
        this.mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(userJson))
                .andDo(print())
                .andExpect(status().isOk()).andDo(document("user/{methodName}"
                , requestFields(
                        fieldWithPath("id").description("user id").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("id")))
                        , fieldWithPath("name").description("name").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("name")))
                        , fieldWithPath("password").description("password").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("password")))
                        , fieldWithPath("description").description("description").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("description")))
                        , fieldWithPath("role").description("role").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("role"))))));
        verify(userService, times(1)).addUser(user);
    }

    //Test add method with duplicated id
    @Test
    public void shouldReturn400WhenCallPostWithDuplicatedObjects() throws Exception {
        doThrow(UserAlreadyExistsException.class).when(userService).addUser(user);
        this.mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(userJson))
                .andDo(print())
                .andExpect(status().isBadRequest()).andDo(document("user/{methodName}"));
    }

    //Test update method
    @Test
    public void shouldReturn200WhenCallPatchForUpdate() throws Exception {
        doNothing().when(userService).updateUser(0, user);
        this.mockMvc.perform(patch("/user/{id}", 0)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(userJson))
                .andDo(print())
                .andExpect(status().isOk()).andDo(document("user/{methodName}"
                , pathParameters(parameterWithName("id").description("user id"))
                , requestFields(
                        fieldWithPath("id").description("user id").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("id")))
                        , fieldWithPath("name").description("name").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("name")))
                        , fieldWithPath("password").description("password").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("password")))
                        , fieldWithPath("description").description("description").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("description")))
                        , fieldWithPath("role").description("role").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("role"))))));
        verify(userService, times(1)).updateUser(0, user);
    }

    //Test update method with id mismatch in path and body request
    @Test
    public void shouldReturn400WhenCallPatchForUpdateWithIDsMismatch() throws Exception {
        doThrow(IllegalArgumentException.class).when(userService).updateUser(1, user);
        this.mockMvc.perform(patch("/user/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(userJson))
                .andDo(print())
                .andExpect(status().isBadRequest()).andDo(document("user/{methodName}"
                , pathParameters(parameterWithName("id").description("user id"))));
    }

    //Test update method when user is not present
    @Test
    public void shouldReturn404WhenCallPatchForUpdateWithNonExistingUser() throws Exception {
        doThrow(UserNotFoundException.class).when(userService).updateUser(0,user);
        this.mockMvc.perform(patch("/user/{id}", 0)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(userJson))
                .andDo(print())
                .andExpect(status().isNotFound()).andDo(document("user/{methodName}"
                , pathParameters(parameterWithName("id").description("user id"))));
    }

    //Test delete method
    @Test
    public void shouldReturn200WhenCallDelete() throws Exception {
        doNothing().when(userService).deleteUser(0);
        this.mockMvc.perform(delete("/user/{id}", 0)).andDo(print()).andExpect(status().isOk())
                .andDo(document("user/{methodName}"
                        , pathParameters(parameterWithName("id").description("user id"))));
    }

    //Test delete method with request non existing user
    @Test
    public void shouldReturn404WhenCallDelete() throws Exception {
        doThrow(UserNotFoundException.class).when(userService).deleteUser(1);
        this.mockMvc.perform(delete("/user/{id}", 1)).andDo(print()).andExpect(status().isNotFound())
                .andDo(document("user/{methodName}"
                        , pathParameters(parameterWithName("id").description("user id"))));
    }


}
