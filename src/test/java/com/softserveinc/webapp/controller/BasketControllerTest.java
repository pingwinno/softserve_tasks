package com.softserveinc.webapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserveinc.webapp.exception.NotFoundException;
import com.softserveinc.webapp.model.Basket;
import com.softserveinc.webapp.model.Item;
import com.softserveinc.webapp.service.interfaces.BasketService;
import com.softserveinc.webapp.utils.EntitiesGenerator;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
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
@WebMvcTest(BasketController.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class BasketControllerTest {

    private ConstraintDescriptions constraintDescriptions = new ConstraintDescriptions(Item.class);
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BasketService bucketService;

    @MockBean
    private Basket basket;

    private List<Item> items;

    private String jsonBucket;

    private String jsonItem;

    @BeforeEach
    void init() throws JsonProcessingException {
        items = EntitiesGenerator.generateItems();
        ObjectMapper objectMapper = new ObjectMapper();
        jsonBucket = objectMapper.writeValueAsString(items);
        jsonItem = objectMapper.writeValueAsString(items.get(0));
    }

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("Test get all items in basket")
    void shouldReturnAllItemsWhenCallGetAll() throws Exception {
        doReturn(items).when(bucketService).getBucket(basket);
        this.mockMvc.perform(get("/basket")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(jsonBucket))).andDo(document("basket/{methodName}"
                , responseFields(
                        fieldWithPath("[].id").description("item id").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("id")))
                        , fieldWithPath("[].name").description("Item name").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("name")))
                        , fieldWithPath("[].price").description("Item price").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("price")))
                        , fieldWithPath("[].description").description("Item description").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("description")))
                        , fieldWithPath("[].category").description("Item category").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("category"))))));
    }

    @Test
    @DisplayName("Test add method")
    public void shouldReturn200WhenCallPostForAdd() throws Exception {
        doNothing().when(bucketService).addItem(items.get(0), basket);
        this.mockMvc.perform(post("/basket")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(jsonItem))
                .andDo(print()).andExpect(status().isOk())
                .andDo(document("basket/{methodName}"
                        , relaxedRequestFields(fieldWithPath("id").description("item id").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("id")))
                                , fieldWithPath("name").description("Item name").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("name")))
                                , fieldWithPath("price").description("Item price").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("price")))
                                , fieldWithPath("description").description("Item description").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("description")))
                                , fieldWithPath("category").description("Item category").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("category"))))));
        verify(bucketService, times(1)).addItem(items.get(0), basket);
    }

    @Test
    @DisplayName("Test remove method success")
    public void shouldReturn200WhenDeleteExistingItem() throws Exception {
        UUID uuid = UUID.randomUUID();
        doNothing().when(bucketService).removeItem(uuid, basket);
        this.mockMvc.perform(delete("/basket/{id}", uuid))
                .andDo(print()).andExpect(status().isOk())
                .andDo(document("basket/{methodName}"
                        , pathParameters(parameterWithName("id").description("item id"))));
    }

    @Test
    @DisplayName("Test remove method failed")
    public void shouldReturn404WhenDeleteNonExistingItem() throws Exception {
        UUID uuid = UUID.randomUUID();
        doThrow(NotFoundException.class).when(bucketService).removeItem(uuid, basket);
        this.mockMvc.perform(delete("/basket/{id}", uuid))
                .andDo(print()).andExpect(status().isNotFound())
                .andDo(document("basket/{methodName}"
                        , pathParameters(parameterWithName("id").description("item id"))));
    }

    @Test
    @DisplayName("Test checkout")
    public void shouldReturnUUIDWhenCheckout() throws Exception {
        UUID uuid = UUID.randomUUID();
        long phoneNumber = 7773829994L;
        when(bucketService.checkout(phoneNumber, basket)).thenReturn(uuid);
        this.mockMvc.perform(post("/basket/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content("{\"phoneNumber\": \"7773829994\"\n}"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(uuid)))
                .andDo(document("basket/{methodName}"
                        , requestFields(fieldWithPath("phoneNumber").description("user phone number")
                                .attributes(key("constraints")
                                        .value("Phone number should be 10 digits length")))));
    }
}
