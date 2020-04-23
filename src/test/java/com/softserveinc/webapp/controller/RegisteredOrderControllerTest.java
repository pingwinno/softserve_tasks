package com.softserveinc.webapp.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserveinc.webapp.exception.NotFoundException;
import com.softserveinc.webapp.exception.WrongParamsException;
import com.softserveinc.webapp.model.OrderState;
import com.softserveinc.webapp.model.RegisteredOrder;
import com.softserveinc.webapp.model.Role;
import com.softserveinc.webapp.model.User;
import com.softserveinc.webapp.service.interfaces.RegisteredOrderService;
import com.softserveinc.webapp.utils.EntitiesGenerator;
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
@WebMvcTest(RegisteredOrderController.class)
public class RegisteredOrderControllerTest {

    private static RegisteredOrder orderWithID = new RegisteredOrder();
    private static RegisteredOrder orderWithoutID = new RegisteredOrder();
    static private String orderJsonWithID;
    private static String orderJsonWithoutID;
    private static final String ROLE_CONSTRAIN = "Enum: ADMIN, MODERATOR, USER";
    private static final String STATE_CONSTRAIN = "Enum: PROCESSING, DELIVERING, DELIVERED";

    private ConstraintDescriptions constraintDescriptions = new ConstraintDescriptions(RegisteredOrder.class);
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RegisteredOrderService orderService;

    @BeforeAll
    public static void init() throws JsonProcessingException {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("user");
        user.setPassword("somePass");
        user.setDescription("some user");
        user.setRole(Role.ADMIN);
        orderWithID.setOrderState(OrderState.DELIVERING);
        orderWithoutID.setOrderState(OrderState.DELIVERING);
        orderWithoutID.setUser(user);
        orderWithoutID.setItems(EntitiesGenerator.generateItems());
        orderWithID.setUser(user);
        orderWithID.setItems(EntitiesGenerator.generateItems());
        ObjectMapper objectMapper = new ObjectMapper();
        orderJsonWithoutID = objectMapper.writeValueAsString(orderWithoutID);
        orderWithID.setId(UUID.randomUUID());
        orderJsonWithID = objectMapper.writeValueAsString(orderWithID);
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
    public void shouldReturnOrderWhenCallGet() throws Exception {
        when(orderService.get(orderWithID.getId())).thenReturn(orderWithID);
        this.mockMvc.perform(get("/registered_order/{id}", orderWithID.getId())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(orderJsonWithID))).andDo(document("registered_order/{methodName}"
                , pathParameters(parameterWithName("id").description("item id"))
                , responseFields(
                        fieldWithPath("id").description("registered order id").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("id")))
                        , fieldWithPath("orderState").description("order state").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("orderState")))
                        , fieldWithPath("user.id").description("user id").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("id")))
                        , fieldWithPath("user.name").description("name").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("name")))
                        , fieldWithPath("user.password").description("password").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("password")))
                        , fieldWithPath("user.phoneNumber").description("phone number").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("phoneNumber")))
                        , fieldWithPath("user.description").description("description").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("description")))
                        , fieldWithPath("user.role").description("role").attributes(key("constraints")
                                .value(ROLE_CONSTRAIN))
                        , fieldWithPath("items[].id").description("Item id").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("id")))
                        , fieldWithPath("items[].name").description("Item name").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("name")))
                        , fieldWithPath("items[].price").description("Item price").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("price")))
                        , fieldWithPath("items[].description").description("Item description").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("description")))
                        , fieldWithPath("items[].category").description("Item category").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("category"))))));
    }

    @Test
    @DisplayName("Test getAll method")
    public void shouldReturnOrdersWhenCallGetAll() throws Exception {
        when(orderService.getAll()).thenReturn(Collections.singletonList(orderWithID));
        this.mockMvc.perform(get("/registered_order")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(orderJsonWithID))).andDo(document("registered_order/{methodName}"
                , responseFields(
                        fieldWithPath("[].id").description("registered order id").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("id")))
                        , fieldWithPath("[].orderState").description("order state").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("orderState")))
                        , fieldWithPath("[].user.id").description("user id").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("id")))
                        , fieldWithPath("[].user.name").description("name").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("name")))
                        , fieldWithPath("[].user.password").description("password").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("password")))
                        , fieldWithPath("[].user.phoneNumber").description("phone number").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("phoneNumber")))
                        , fieldWithPath("[].user.description").description("description").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("description")))
                        , fieldWithPath("[].user.role").description("role").attributes(key("constraints")
                                .value(ROLE_CONSTRAIN))
                        , fieldWithPath("[].items[].id").description("Item id").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("id")))
                        , fieldWithPath("[].items[].name").description("Item name").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("name")))
                        , fieldWithPath("[].items[].price").description("Item price").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("price")))
                        , fieldWithPath("[].items[].description").description("Item description").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("description")))
                        , fieldWithPath("[].items[].category").description("Item category").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("category"))))));
    }

    @Test
    @DisplayName("Test search by user id")
    public void shouldReturnOrderWhenCallSearchByUserID() throws Exception {
        when(orderService.getBy(Collections.singletonMap("user_id", orderWithID.getUser().getId().toString())))
                .thenReturn(Collections.singletonList(orderWithID));
        this.mockMvc.perform(get("/registered_order/search?{field}={value}", "user_id",
                orderWithID.getUser().getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(orderJsonWithID)))
                .andDo(document("registered_order/{methodName}"
                        , requestParameters(parameterWithName("user_id").description("search registered order by user id"))
                        , responseFields(
                                fieldWithPath("[].id").description("registered order id").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("id")))
                                , fieldWithPath("[].orderState").description("order state").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("orderState")))
                                , fieldWithPath("[].user.id").description("user id").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("id")))
                                , fieldWithPath("[].user.name").description("name").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("name")))
                                , fieldWithPath("[].user.password").description("password").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("password")))
                                , fieldWithPath("[].user.phoneNumber").description("phone number").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("phoneNumber")))
                                , fieldWithPath("[].user.description").description("description").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("description")))
                                , fieldWithPath("[].user.role").description("role").attributes(key("constraints")
                                        .value(ROLE_CONSTRAIN))
                                , fieldWithPath("[].items[].id").description("Item id").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("id")))
                                , fieldWithPath("[].items[].name").description("Item name").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("name")))
                                , fieldWithPath("[].items[].price").description("Item price").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("price")))
                                , fieldWithPath("[].items[].description").description("Item description").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("description")))
                                , fieldWithPath("[].items[].category").description("Item category").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("category"))))));
    }

    @Test
    @DisplayName("Test search by order state")
    public void shouldReturnOrderWhenCallSearchByState() throws Exception {
        when(orderService.getBy(Collections.singletonMap("state", orderWithID.getOrderState().toString())))
                .thenReturn(Collections.singletonList(orderWithID));
        this.mockMvc.perform(get("/registered_order/search?{field}={value}", "state",
                orderWithID.getOrderState().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(orderJsonWithID)))
                .andDo(document("registered_order/{methodName}"
                        , requestParameters(parameterWithName("state").description("search registered order by order state"))
                        , responseFields(
                                fieldWithPath("[].id").description("registered order id").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("id")))
                                , fieldWithPath("[].orderState").description("order state").attributes(key("constraints")
                                        .value(STATE_CONSTRAIN))
                                , fieldWithPath("[].user.id").description("user id").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("id")))
                                , fieldWithPath("[].user.name").description("name").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("name")))
                                , fieldWithPath("[].user.password").description("password").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("password")))
                                , fieldWithPath("[].user.phoneNumber").description("phone number").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("phoneNumber")))
                                , fieldWithPath("[].user.description").description("description").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("description")))
                                , fieldWithPath("[].user.role").description("role").attributes(key("constraints")
                                        .value(ROLE_CONSTRAIN))
                                , fieldWithPath("[].items[].id").description("Item id").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("id")))
                                , fieldWithPath("[].items[].name").description("Item name").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("name")))
                                , fieldWithPath("[].items[].price").description("Item price").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("price")))
                                , fieldWithPath("[].items[].description").description("Item description").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("description")))
                                , fieldWithPath("[].items[].category").description("Item category").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("category"))))));
    }

    @Test
    @DisplayName("Test get method with request non existing registered order")
    public void shouldReturn404WhenCallGet() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(orderService.get(uuid)).thenThrow(NotFoundException.class);
        this.mockMvc.perform(get("/registered_order/{id}", uuid)).andDo(print()).andExpect(status().isNotFound())
                .andDo(document("registered_order/{methodName}"
                        , pathParameters(parameterWithName("id").description("registered order id"))));
    }

    @Test
    @DisplayName("Test search with wrong key")
    public void shouldReturn400WhenSearchWithWrongKey() throws Exception {
        doThrow(WrongParamsException.class).when(orderService).getBy(Collections.singletonMap("some","value"));
        this.mockMvc.perform(get("/registered_order/search?{field}={value}", "some", "value"))
                .andExpect(status().isBadRequest())
                .andDo(document("registered_order/{methodName}"));
    }

    @Test
    @DisplayName("Test add method")
    public void shouldReturn200WhenCallPostForAdd() throws Exception {
        when(orderService.add(orderWithoutID)).thenReturn(orderWithID);
        this.mockMvc.perform(post("/registered_order")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(orderJsonWithoutID))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(content().string(containsString(orderJsonWithID)))
                .andDo(document("registered_order/{methodName}"
                        , relaxedRequestFields(
                                fieldWithPath("id").description("registered order id").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("id")))
                                , fieldWithPath("orderState").description("order state").attributes(key("constraints")
                                        .value(STATE_CONSTRAIN))
                                , fieldWithPath("user.id").description("user id").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("id")))
                                , fieldWithPath("user.name").description("name").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("name")))
                                , fieldWithPath("user.password").description("password").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("password")))
                                , fieldWithPath("user.phoneNumber").description("phone number").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("phoneNumber")))
                                , fieldWithPath("user.description").description("description").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("description")))
                                , fieldWithPath("user.role").description("role").attributes(key("constraints")
                                        .value(ROLE_CONSTRAIN))
                                , fieldWithPath("items[].id").description("Item id").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("id")))
                                , fieldWithPath("items[].name").description("Item name").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("name")))
                                , fieldWithPath("items[].price").description("Item price").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("price")))
                                , fieldWithPath("items[].description").description("Item description").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("description")))
                                , fieldWithPath("items[].category").description("Item category").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("category"))))
                        , responseFields(
                                fieldWithPath("id").description("registered order id").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("id")))
                                , fieldWithPath("orderState").description("order state").attributes(key("constraints")
                                        .value(STATE_CONSTRAIN))
                                , fieldWithPath("user.id").description("user id").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("id")))
                                , fieldWithPath("user.name").description("name").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("name")))
                                , fieldWithPath("user.password").description("password").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("password")))
                                , fieldWithPath("user.phoneNumber").description("phone number").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("phoneNumber")))
                                , fieldWithPath("user.description").description("description").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("description")))
                                , fieldWithPath("user.role").description("role").attributes(key("constraints")
                                        .value(ROLE_CONSTRAIN))
                                , fieldWithPath("items[].id").description("Item id").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("id")))
                                , fieldWithPath("items[].name").description("Item name").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("name")))
                                , fieldWithPath("items[].price").description("Item price").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("price")))
                                , fieldWithPath("items[].description").description("Item description").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("description")))
                                , fieldWithPath("items[].category").description("Item category").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("category"))))));
        verify(orderService, times(1)).add(orderWithoutID);
    }

    @Test
    @DisplayName("Test update method")
    public void shouldReturn200WhenCallPatchForUpdate() throws Exception {
        doNothing().when(orderService).update(orderWithID.getId(), orderWithID);
        this.mockMvc.perform(patch("/registered_order/{id}", orderWithID.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(orderJsonWithID))
                .andDo(print())
                .andExpect(status().isOk()).andDo(document("registered_order/{methodName}"
                , pathParameters(parameterWithName("id").description("item id"))
                , relaxedRequestFields(
                        fieldWithPath("id").description("registered order id").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("id")))
                        , fieldWithPath("orderState").description("order state").attributes(key("constraints")
                                .value(STATE_CONSTRAIN))
                        , fieldWithPath("user.id").description("user id").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("id")))
                        , fieldWithPath("user.name").description("name").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("name")))
                        , fieldWithPath("user.password").description("password").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("password")))
                        , fieldWithPath("user.phoneNumber").description("phone number").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("phoneNumber")))
                        , fieldWithPath("user.description").description("description").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("description")))
                        , fieldWithPath("user.role").description("role").attributes(key("constraints")
                                .value(ROLE_CONSTRAIN))
                        , fieldWithPath("items[].id").description("Item id").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("id")))
                        , fieldWithPath("items[].name").description("Item name").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("name")))
                        , fieldWithPath("items[].price").description("Item price").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("price")))
                        , fieldWithPath("items[].description").description("Item description").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("description")))
                        , fieldWithPath("items[].category").description("Item category").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("category"))))));
    }

    @Test
    @DisplayName("Test update method when registered order is not present")
    public void shouldReturn404WhenCallPatchForUpdateWithNonExistingOrder() throws Exception {
        doThrow(new NotFoundException()).when(orderService).update(orderWithID.getId(), orderWithID);
        this.mockMvc.perform(patch("/registered_order/{id}", orderWithID.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJsonWithID))
                .andDo(print())
                .andExpect(status().isNotFound()).andDo(document("registered_order/{methodName}"
                , pathParameters(parameterWithName("id").description("item id"))));
    }

    @Test
    @DisplayName("Test delete method")
    public void shouldReturn200WhenCallDelete() throws Exception {
        doNothing().when(orderService).delete(orderWithID.getId());
        this.mockMvc.perform(delete("/registered_order/{id}", orderWithID.getId())).andDo(print()).andExpect(status().isOk())
                .andDo(document("registered_order/{methodName}"
                        , pathParameters(parameterWithName("id").description("item id"))));
    }

    @Test
    @DisplayName("Test delete method with request non existing registered order")
    public void shouldReturn404WhenCallDelete() throws Exception {
        UUID uuid = UUID.randomUUID();
        doThrow(NotFoundException.class).when(orderService).delete(uuid);
        this.mockMvc.perform(delete("/registered_order/{id}", uuid)).andDo(print()).andExpect(status().isNotFound())
                .andDo(document("registered_order/{methodName}"
                        , pathParameters(parameterWithName("id").description("item id"))));
    }
}
