package com.softserveinc.webapp.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserveinc.webapp.exception.UserNotFoundException;
import com.softserveinc.webapp.model.Role;
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

import java.util.UUID;

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
    private static User userWithID = new User();
    private static User userWithoutID = new User();
    static private String userJsonWithID;
    private static String userJsonWithoutID;

    private ConstraintDescriptions constraintDescriptions = new ConstraintDescriptions(User.class);
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @BeforeAll
    public static void init() throws JsonProcessingException {
        userWithoutID.setName("user");
        userWithoutID.setPassword("somePass");
        userWithoutID.setDescription("some user");
        userWithoutID.setRole(Role.ADMIN);
        userWithID.setName("user");
        userWithID.setPassword("somePass");
        userWithID.setDescription("some user");
        userWithID.setRole(Role.ADMIN);
        ObjectMapper objectMapper = new ObjectMapper();
        userJsonWithoutID = objectMapper.writeValueAsString(userWithID);
        userWithID.setId(UUID.randomUUID());
        userJsonWithID = objectMapper.writeValueAsString(userWithID);
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
        when(userService.getUser(userWithID.getId())).thenReturn(userWithID);
        this.mockMvc.perform(get("/user/{id}", userWithID.getId())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(userJsonWithID))).andDo(document("user/{methodName}"
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
        UUID uuid = UUID.randomUUID();
        when(userService.getUser(uuid)).thenThrow(UserNotFoundException.class);
        this.mockMvc.perform(get("/user/{id}", uuid)).andDo(print()).andExpect(status().isNotFound())
                .andDo(document("user/{methodName}"
                        , pathParameters(parameterWithName("id").description("user id"))));
    }

    //Test add method
    @Test
    public void shouldReturn200WhenCallPostForAdd() throws Exception {
        when(userService.addUser(userWithoutID)).thenReturn(userWithID);
        this.mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(userJsonWithoutID))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(content().string(containsString(userJsonWithID)))
                .andDo(document("user/{methodName}"
                        , relaxedRequestFields(subsectionWithPath("name").description("name").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("name")))
                                , subsectionWithPath("password").description("password").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("password")))
                                , subsectionWithPath("description").description("description").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("description")))
                                , subsectionWithPath("role").description("role").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("role"))))
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
                                        .value(constraintDescriptions.descriptionsForProperty("role"))))
                ));
        verify(userService, times(1)).addUser(userWithoutID);
    }

    //Test update method
    @Test
    public void shouldReturn200WhenCallPatchForUpdate() throws Exception {
        doNothing().when(userService).updateUser(userWithID.getId(), userWithID);
        this.mockMvc.perform(patch("/user/{id}", userWithID.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(userJsonWithID))
                .andDo(print())
                .andExpect(status().isOk()).andDo(document("user/{methodName}"
                , pathParameters(parameterWithName("id").description("user id"))
                , relaxedRequestFields(fieldWithPath("name").description("name").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("name")))
                        , fieldWithPath("password").description("password").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("password")))
                        , fieldWithPath("description").description("description").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("description")))
                        , fieldWithPath("role").description("role").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("role"))))));
    }

    //Test update method when user is not present
    @Test
    public void shouldReturn404WhenCallPatchForUpdateWithNonExistingUser() throws Exception {
        doThrow(new UserNotFoundException()).when(userService).updateUser(any(), any());
        this.mockMvc.perform(patch("/user/{id}", userWithID.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJsonWithID))
                .andDo(print())
                .andExpect(status().isNotFound()).andDo(document("user/{methodName}"
                , pathParameters(parameterWithName("id").description("user id"))));
    }

    //Test delete method
    @Test
    public void shouldReturn200WhenCallDelete() throws Exception {
        doNothing().when(userService).deleteUser(userWithID.getId());
        this.mockMvc.perform(delete("/user/{id}", userWithID.getId())).andDo(print()).andExpect(status().isOk())
                .andDo(document("user/{methodName}"
                        , pathParameters(parameterWithName("id").description("user id"))));
    }

    //Test delete method with request non existing user
    @Test
    public void shouldReturn404WhenCallDelete() throws Exception {
        UUID uuid = UUID.randomUUID();
        doThrow(UserNotFoundException.class).when(userService).deleteUser(uuid);
        this.mockMvc.perform(delete("/user/{id}", uuid)).andDo(print()).andExpect(status().isNotFound())
                .andDo(document("user/{methodName}"
                        , pathParameters(parameterWithName("id").description("user id"))));
    }


}
