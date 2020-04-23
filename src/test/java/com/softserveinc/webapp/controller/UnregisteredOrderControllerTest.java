package com.softserveinc.webapp.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserveinc.webapp.exception.NotFoundException;
import com.softserveinc.webapp.exception.WrongParamsException;
import com.softserveinc.webapp.model.OrderState;
import com.softserveinc.webapp.model.UnregisteredOrder;
import com.softserveinc.webapp.service.interfaces.UnregisteredOrderService;
import com.softserveinc.webapp.utils.EntitiesGenerator;
import com.softserveinc.webapp.utils.PhoneNumberGenerator;
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
@WebMvcTest(UnregisteredOrderController.class)
public class UnregisteredOrderControllerTest {

    private static UnregisteredOrder orderWithID = new UnregisteredOrder();
    private static UnregisteredOrder orderWithoutID = new UnregisteredOrder();
    static private String orderJsonWithID;
    private static String orderJsonWithoutID;

    private ConstraintDescriptions constraintDescriptions = new ConstraintDescriptions(UnregisteredOrder.class);
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UnregisteredOrderService orderService;

    @BeforeAll
    public static void init() throws JsonProcessingException {
        long phoneNumber = PhoneNumberGenerator.generate();
        orderWithoutID.setPhoneNumber(phoneNumber);
        orderWithID.setPhoneNumber(phoneNumber);
        orderWithoutID.setItems(EntitiesGenerator.generateItems());
        orderWithID.setItems(EntitiesGenerator.generateItems());
        orderWithID.setOrderState(OrderState.DELIVERING);
        orderWithoutID.setOrderState(OrderState.DELIVERING);
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
        this.mockMvc.perform(get("/unregistered_order/{id}", orderWithID.getId())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(orderJsonWithID))).andDo(document("unregistered_order/{methodName}"
                , pathParameters(parameterWithName("id").description("item id"))
                , responseFields(
                        fieldWithPath("id").description("unregistered_order id").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("id")))
                        , fieldWithPath("orderState").description("order state").attributes(key("constraints")
                                .value("Enum: PROCESSING, DELIVERING, DELIVERED"))
                        , fieldWithPath("phoneNumber").description("phone number").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("phoneNumber")))
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
    public void shouldReturnOrderWhenCallGetAll() throws Exception {
        when(orderService.getAll()).thenReturn(Collections.singletonList(orderWithID));
        this.mockMvc.perform(get("/unregistered_order")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(orderJsonWithID))).andDo(document("unregistered_order/{methodName}"
                , responseFields(
                        fieldWithPath("[].id").description("unregistered_order id").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("id")))
                        , fieldWithPath("[].orderState").description("order state").attributes(key("constraints")
                                .value("Enum: PROCESSING, DELIVERING, DELIVERED"))
                        , fieldWithPath("[].phoneNumber").description("phone number").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("phoneNumber")))
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
    @DisplayName("Test search by phone number")
    public void shouldReturnOrderWhenCallSearchByPhoneNumber() throws Exception {
        when(orderService.getBy(Collections.singletonMap("phone", Long.toString(orderWithID.getPhoneNumber()))))
                .thenReturn(Collections.singletonList(orderWithID));
        this.mockMvc.perform(get("/unregistered_order/search?{field}={value}", "phone",
                Long.toString(orderWithID.getPhoneNumber())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(orderJsonWithID)))
                .andDo(document("unregistered_order/{methodName}"
                        , requestParameters(parameterWithName("phone").description("search unregistered_order by phone number"))
                        , responseFields(
                                fieldWithPath("[].id").description("unregistered_order id").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("id")))
                                , fieldWithPath("[].orderState").description("order state").attributes(key("constraints")
                                        .value("Enum: PROCESSING, DELIVERING, DELIVERED"))
                                , fieldWithPath("[].phoneNumber").description("phone number").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("phoneNumber")))
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
    public void shouldReturnOrderWhenCallSearchByOrderState() throws Exception {
        when(orderService.getBy(Collections.singletonMap("state", Long.toString(orderWithID.getPhoneNumber()))))
                .thenReturn(Collections.singletonList(orderWithID));
        this.mockMvc.perform(get("/unregistered_order/search?{field}={value}", "state",
                Long.toString(orderWithID.getPhoneNumber())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(orderJsonWithID)))
                .andDo(document("unregistered_order/{methodName}"
                        , requestParameters(parameterWithName("state").description("search unregistered_order by order state"))
                        , responseFields(
                                fieldWithPath("[].id").description("unregistered_order id").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("id")))
                                , fieldWithPath("[].orderState").description("order state").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("orderState")))
                                , fieldWithPath("[].phoneNumber").description("phone number").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("phoneNumber")))
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
    @DisplayName("Test search with wrong key")
    public void shouldReturn400WhenSearchWithWrongKey() throws Exception {
        doThrow(WrongParamsException.class).when(orderService).getBy(Collections.singletonMap("some","value"));
        this.mockMvc.perform(get("/unregistered_order/search?{field}={value}", "some", "value"))
                .andExpect(status().isBadRequest())
                .andDo(document("unregistered_order/{methodName}"));
    }

    @Test
    @DisplayName("Test get method with request non existing unregistered_order")
    public void shouldReturn404WhenCallGet() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(orderService.get(uuid)).thenThrow(NotFoundException.class);
        this.mockMvc.perform(get("/unregistered_order/{id}", uuid)).andDo(print()).andExpect(status().isNotFound())
                .andDo(document("unregistered_order/{methodName}"
                        , pathParameters(parameterWithName("id").description("unregistered_order id"))));
    }

    @Test
    @DisplayName("Test add method")
    public void shouldReturn200WhenCallPostForAdd() throws Exception {
        when(orderService.add(orderWithoutID)).thenReturn(orderWithID);
        this.mockMvc.perform(post("/unregistered_order")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(orderJsonWithoutID))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(content().string(containsString(orderJsonWithID)))
                .andDo(document("unregistered_order/{methodName}"
                        , relaxedRequestFields(
                                fieldWithPath("id").description("unregistered_order id").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("id")))
                                , fieldWithPath("orderState").description("order state").attributes(key("constraints")
                                        .value("Enum: PROCESSING, DELIVERING, DELIVERED"))
                                , fieldWithPath("phoneNumber").description("phone number").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("phoneNumber")))
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
                                fieldWithPath("id").description("unregistered_order id").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("id")))
                                , fieldWithPath("orderState").description("order state").attributes(key("constraints")
                                        .value("Enum: PROCESSING, DELIVERING, DELIVERED"))
                                , fieldWithPath("phoneNumber").description("phone number").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("phoneNumber")))
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
        this.mockMvc.perform(patch("/unregistered_order/{id}", orderWithID.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(orderJsonWithID))
                .andDo(print())
                .andExpect(status().isOk()).andDo(document("unregistered_order/{methodName}"
                , pathParameters(parameterWithName("id").description("item id"))
                , relaxedRequestFields(
                        fieldWithPath("id").description("unregistered_order id").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("id")))
                        , fieldWithPath("orderState").description("order state").attributes(key("constraints")
                                .value("Enum: PROCESSING, DELIVERING, DELIVERED"))
                        , fieldWithPath("phoneNumber").description("phone number").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("phoneNumber")))
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
    @DisplayName("Test update method when unregistered_order is not present")
    public void shouldReturn404WhenCallPatchForUpdateWithNonExistingOrder() throws Exception {
        doThrow(new NotFoundException()).when(orderService).update(orderWithID.getId(), orderWithID);
        this.mockMvc.perform(patch("/unregistered_order/{id}", orderWithID.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJsonWithID))
                .andDo(print())
                .andExpect(status().isNotFound()).andDo(document("unregistered_order/{methodName}"
                , pathParameters(parameterWithName("id").description("item id"))));
    }

    @Test
    @DisplayName("Test delete method")
    public void shouldReturn200WhenCallDelete() throws Exception {
        doNothing().when(orderService).delete(orderWithID.getId());
        this.mockMvc.perform(delete("/unregistered_order/{id}", orderWithID.getId())).andDo(print()).andExpect(status().isOk())
                .andDo(document("unregistered_order/{methodName}"
                        , pathParameters(parameterWithName("id").description("item id"))));
    }

    @Test
    @DisplayName("Test delete method with request non existing unregistered_order")
    public void shouldReturn404WhenCallDelete() throws Exception {
        UUID uuid = UUID.randomUUID();
        doThrow(NotFoundException.class).when(orderService).delete(uuid);
        this.mockMvc.perform(delete("/unregistered_order/{id}", uuid)).andDo(print()).andExpect(status().isNotFound())
                .andDo(document("unregistered_order/{methodName}"
                        , pathParameters(parameterWithName("id").description("item id"))));
    }
}
