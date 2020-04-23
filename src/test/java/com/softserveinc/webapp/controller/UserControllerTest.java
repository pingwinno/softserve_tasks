package com.softserveinc.webapp.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserveinc.webapp.exception.NotFoundException;
import com.softserveinc.webapp.exception.WrongParamsException;
import com.softserveinc.webapp.model.Role;
import com.softserveinc.webapp.model.User;
import com.softserveinc.webapp.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
    private static final String ROLE_CONSTRAIN = "Enum: ADMIN, MODERATOR, USER";

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
        userJsonWithoutID = objectMapper.writeValueAsString(userWithoutID);
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

    @Test
    @DisplayName("Test get method")
    public void shouldReturnUserWhenCallGet() throws Exception {
        when(userService.get(userWithID.getId())).thenReturn(userWithID);
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
                        , fieldWithPath("phoneNumber").description("phone number").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("phoneNumber")))
                        , fieldWithPath("description").description("description").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("description")))
                        , fieldWithPath("role").description("role").attributes(key("constraints")
                                .value(ROLE_CONSTRAIN)))));
    }

    @Test
    @DisplayName("Test getAll method")
    public void shouldReturnUserWhenCallGetAll() throws Exception {
        doReturn(List.of(userWithID)).when(userService).getAll();
        this.mockMvc.perform(get("/user")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(userJsonWithID))).andDo(document("user/{methodName}"
                , responseFields(
                        fieldWithPath("[].id").description("user id").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("id")))
                        , fieldWithPath("[].name").description("name").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("name")))
                        , fieldWithPath("[].password").description("password").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("password")))
                        , fieldWithPath("[].phoneNumber").description("phone number").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("phoneNumber")))
                        , fieldWithPath("[].description").description("description").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("description")))
                        , fieldWithPath("[].role").description("role").attributes(key("constraints")
                                .value(ROLE_CONSTRAIN)))));
    }

    @Test
    @DisplayName("Test search by name")
    public void shouldReturnUserWhenCallGetByName() throws Exception {
        when(userService.getBy(Collections.singletonMap("name", userWithID.getName()))).thenReturn(Collections.singletonList(userWithID));
        this.mockMvc.perform(get("/user/search?{field}={value}", "name", userWithID.getName()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(userJsonWithID)))
                .andDo(document("user/{methodName}"
                        , requestParameters(parameterWithName("name").description("search user by user name"))
                        , responseFields(
                                fieldWithPath("[].id").description("user id").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("id")))
                                , fieldWithPath("[].name").description("name").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("name")))
                                , fieldWithPath("[].password").description("password").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("password")))
                                , fieldWithPath("[].phoneNumber").description("phone number").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("phoneNumber")))
                                , fieldWithPath("[].description").description("description").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("description")))
                                , fieldWithPath("[].role").description("role").attributes(key("constraints")
                                        .value(ROLE_CONSTRAIN)))));
    }

    @Test
    @DisplayName("Test search by phone number")
    public void shouldReturnUserWhenCallGetByPhoneNumber() throws Exception {
        when(userService.getBy(Collections.singletonMap("phone", String.valueOf(userWithID.getPhoneNumber()))))
                .thenReturn(Collections.singletonList(userWithID));
        this.mockMvc.perform(get("/user/search?{field}={value}", "phone", userWithID.getPhoneNumber()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(userJsonWithID)))
                .andDo(document("user/{methodName}"
                        , requestParameters(parameterWithName("phone").description("search user by phone number"))
                        , responseFields(
                                fieldWithPath("[].id").description("user id").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("id")))
                                , fieldWithPath("[].name").description("name").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("name")))
                                , fieldWithPath("[].password").description("password").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("password")))
                                , fieldWithPath("[].phoneNumber").description("phone number").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("phoneNumber")))
                                , fieldWithPath("[].description").description("description").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("description")))
                                , fieldWithPath("[].role").description("role").attributes(key("constraints")
                                        .value(ROLE_CONSTRAIN)))));
    }

    @Test
    @DisplayName("Test search with wrong key")
    public void shouldReturn400WhenSearchWithWrongKey() throws Exception {
        doThrow(WrongParamsException.class).when(userService).getBy(Collections.singletonMap("some", "value"));
        this.mockMvc.perform(get("/user/search?{field}={value}", "some", "value"))
                .andExpect(status().isBadRequest())
                .andDo(document("user/{methodName}"));
    }

    @Test
    @DisplayName("Test get method with request non existing user")
    public void shouldReturn404WhenCallGet() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(userService.get(uuid)).thenThrow(NotFoundException.class);
        this.mockMvc.perform(get("/user/{id}", uuid)).andDo(print()).andExpect(status().isNotFound())
                .andDo(document("user/{methodName}"
                        , pathParameters(parameterWithName("id").description("user id"))));
    }

    @Test
    @DisplayName("Test add method")
    public void shouldReturn200WhenCallPostForAdd() throws Exception {
        when(userService.add(userWithoutID)).thenReturn(userWithID);
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
                                , fieldWithPath("phoneNumber").description("phone number").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("phoneNumber")))
                                , subsectionWithPath("description").description("description").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("description")))
                                , subsectionWithPath("role").description("role").attributes(key("constraints")
                                        .value(ROLE_CONSTRAIN)))
                        , responseFields(
                                fieldWithPath("id").description("user id").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("id")))
                                , fieldWithPath("name").description("name").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("name")))
                                , fieldWithPath("password").description("password").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("password")))
                                , fieldWithPath("phoneNumber").description("phone number").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("phoneNumber")))
                                , fieldWithPath("description").description("description").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("description")))
                                , fieldWithPath("role").description("role").attributes(key("constraints")
                                        .value(ROLE_CONSTRAIN)))));
        verify(userService, times(1)).add(userWithoutID);
    }

    @Test
    @DisplayName("Test update method")
    public void shouldReturn200WhenCallPatchForUpdate() throws Exception {
        doNothing().when(userService).update(userWithID.getId(), userWithID);
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
                        , fieldWithPath("phoneNumber").description("phone number").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("phoneNumber")))
                        , fieldWithPath("description").description("description").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("description")))
                        , fieldWithPath("role").description("role").attributes(key("constraints")
                                .value(ROLE_CONSTRAIN)))));
    }

    @Test
    @DisplayName("Test update method when user is not present")
    public void shouldReturn404WhenCallPatchForUpdateWithNonExistingUser() throws Exception {
        doThrow(new NotFoundException()).when(userService).update(userWithID.getId(), userWithID);
        this.mockMvc.perform(patch("/user/{id}", userWithID.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(userJsonWithID))
                .andDo(print())
                .andExpect(status().isNotFound()).andDo(document("user/{methodName}"
                , pathParameters(parameterWithName("id").description("user id"))));
    }

    @Test
    @DisplayName("Test delete method")
    public void shouldReturn200WhenCallDelete() throws Exception {
        doNothing().when(userService).delete(userWithID.getId());
        this.mockMvc.perform(delete("/user/{id}", userWithID.getId())).andDo(print()).andExpect(status().isOk())
                .andDo(document("user/{methodName}"
                        , pathParameters(parameterWithName("id").description("user id"))));
    }


    @Test
    @DisplayName("Test delete method with request non existing user")
    public void shouldReturn404WhenCallDelete() throws Exception {
        UUID uuid = UUID.randomUUID();
        doThrow(NotFoundException.class).when(userService).delete(uuid);
        this.mockMvc.perform(delete("/user/{id}", uuid)).andDo(print()).andExpect(status().isNotFound())
                .andDo(document("user/{methodName}"
                        , pathParameters(parameterWithName("id").description("user id"))));
    }
}
