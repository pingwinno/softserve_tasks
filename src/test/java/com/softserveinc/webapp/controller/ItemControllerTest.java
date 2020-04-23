package com.softserveinc.webapp.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserveinc.webapp.exception.NotFoundException;
import com.softserveinc.webapp.exception.WrongParamsException;
import com.softserveinc.webapp.model.Item;
import com.softserveinc.webapp.service.interfaces.ItemService;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
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
@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    private static Item itemWithID = new Item();
    private static Item itemWithoutID = new Item();
    static private String itemJsonWithID;
    private static String itemJsonWithoutID;

    private ConstraintDescriptions constraintDescriptions = new ConstraintDescriptions(Item.class);
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService itemService;

    @BeforeAll
    public static void init() throws JsonProcessingException {
        itemWithoutID.setName("item");
        itemWithoutID.setPrice(BigDecimal.TEN);
        itemWithoutID.setDescription("some item");
        itemWithoutID.setCategory("category");
        itemWithID.setName("item");
        itemWithID.setPrice(BigDecimal.TEN);
        itemWithID.setDescription("some item");
        itemWithID.setCategory("category");
        ObjectMapper objectMapper = new ObjectMapper();
        itemJsonWithoutID = objectMapper.writeValueAsString(itemWithoutID);
        itemWithID.setId(UUID.randomUUID());
        itemJsonWithID = objectMapper.writeValueAsString(itemWithID);
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
    public void shouldReturnItemWhenCallGet() throws Exception {
        when(itemService.get(itemWithID.getId())).thenReturn(itemWithID);
        this.mockMvc.perform(get("/item/{id}", itemWithID.getId())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(itemJsonWithID))).andDo(document("item/{methodName}"
                , pathParameters(parameterWithName("id").description("item id"))
                , responseFields(
                        fieldWithPath("id").description("item id").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("id")))
                        , fieldWithPath("name").description("Item name").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("name")))
                        , fieldWithPath("price").description("Item price").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("price")))
                        , fieldWithPath("description").description("Item description").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("description")))
                        , fieldWithPath("category").description("Item category").attributes(key("constraints")
                                .value(constraintDescriptions.descriptionsForProperty("category"))))));
    }

    @Test
    @DisplayName("Test get all method")
    public void shouldReturnItemWhenCallGetAll() throws Exception {
        when(itemService.getAll()).thenReturn(Collections.singletonList(itemWithID));
        this.mockMvc.perform(get("/item")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(itemJsonWithID))).andDo(document("item/{methodName}"
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
    @DisplayName("Test search by name")
    public void shouldReturnItemWhenCallSearchByName() throws Exception {
        when(itemService.getBy(Collections.singletonMap("name", itemWithID.getName()))).thenReturn(Collections.singletonList(itemWithID));
        this.mockMvc.perform(get("/item/search?{field}={value}", "name", itemWithID.getName()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(itemJsonWithID)))
                .andDo(document("item/{methodName}"
                        , requestParameters(parameterWithName("name").description("search items by item name"))
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
    @DisplayName("Test search by category")
    public void shouldReturnItemWhenCallSearchByCategory() throws Exception {
        when(itemService.getBy(Collections.singletonMap("category", itemWithID.getCategory())))
                .thenReturn(Collections.singletonList(itemWithID));
        this.mockMvc.perform(get("/item/search?{field}={value}",
                "category", itemWithID.getCategory()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(itemJsonWithID)))
                .andDo(document("item/{methodName}"
                        , requestParameters(
                                parameterWithName("category").description("search items by category"))
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
    @DisplayName("Test search by name and category")
    public void shouldReturnItemWhenCallSearchByNameAndCategory() throws Exception {
        when(itemService.getBy(Map.of("name", itemWithID.getName(),
                "category", itemWithID.getCategory()))).thenReturn(Collections.singletonList(itemWithID));
        this.mockMvc.perform(get("/item/search?{field}={value}&{field2}={value2}",
                "name", itemWithID.getName(), "category", itemWithID.getCategory()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(itemJsonWithID)))
                .andDo(document("item/{methodName}"
                        , requestParameters(parameterWithName("name").description("search items by item name")
                                , parameterWithName("category").description("search items by category"))

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
    @DisplayName("Test search with wrong key")
    public void shouldReturn400WhenSearchWithWrongKey() throws Exception {
        doThrow(WrongParamsException.class).when(itemService).getBy(Collections.singletonMap("some", "value"));
        this.mockMvc.perform(get("/item/search?{field}={value}", "some", "value"))
                .andExpect(status().isBadRequest())
                .andDo(document("item/{methodName}"));
    }

    @Test
    @DisplayName("Test get method with request non existing item")
    public void shouldReturn404WhenCallGet() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(itemService.get(uuid)).thenThrow(NotFoundException.class);
        this.mockMvc.perform(get("/item/{id}", uuid)).andDo(print()).andExpect(status().isNotFound())
                .andDo(document("item/{methodName}"
                        , pathParameters(parameterWithName("id").description("item id"))));
    }

    @Test
    @DisplayName("Test add method")
    public void shouldReturn200WhenCallPostForAdd() throws Exception {
        when(itemService.add(itemWithoutID)).thenReturn(itemWithID);
        this.mockMvc.perform(post("/item")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(itemJsonWithoutID))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(content().string(containsString(itemJsonWithID)))
                .andDo(document("item/{methodName}"
                        , relaxedRequestFields(fieldWithPath("id").description("item id").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("id")))
                                , fieldWithPath("name").description("Item name").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("name")))
                                , fieldWithPath("price").description("Item price").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("price")))
                                , fieldWithPath("description").description("Item description").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("description")))
                                , fieldWithPath("category").description("Item category").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("category"))))
                        , responseFields(
                                fieldWithPath("id").description("item id").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("id")))
                                , fieldWithPath("name").description("Item name").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("name")))
                                , fieldWithPath("price").description("Item price").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("price")))
                                , fieldWithPath("description").description("Item description").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("description")))
                                , fieldWithPath("category").description("Item category").attributes(key("constraints")
                                        .value(constraintDescriptions.descriptionsForProperty("category"))))
                ));
        verify(itemService, times(1)).add(itemWithoutID);
    }

    @Test
    @DisplayName("Test update method")
    public void shouldReturn200WhenCallPatchForUpdate() throws Exception {
        doNothing().when(itemService).update(itemWithID.getId(), itemWithID);
        this.mockMvc.perform(patch("/item/{id}", itemWithID.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(itemJsonWithID))
                .andDo(print())
                .andExpect(status().isOk()).andDo(document("item/{methodName}"
                , pathParameters(parameterWithName("id").description("item id"))
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
    }

    @Test
    @DisplayName("Test update method when item is not present")
    public void shouldReturn404WhenCallPatchForUpdateWithNonExistingItem() throws Exception {
        doThrow(new NotFoundException()).when(itemService).update(itemWithID.getId(), itemWithID);
        this.mockMvc.perform(patch("/item/{id}", itemWithID.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemJsonWithID))
                .andDo(print())
                .andExpect(status().isNotFound()).andDo(document("item/{methodName}"
                , pathParameters(parameterWithName("id").description("item id"))));
    }

    @Test
    @DisplayName("Test delete method")
    public void shouldReturn200WhenCallDelete() throws Exception {
        doNothing().when(itemService).delete(itemWithID.getId());
        this.mockMvc.perform(delete("/item/{id}", itemWithID.getId())).andDo(print()).andExpect(status().isOk())
                .andDo(document("item/{methodName}"
                        , pathParameters(parameterWithName("id").description("item id"))));
    }

    @Test
    @DisplayName("Test delete method with request non existing item")
    public void shouldReturn404WhenCallDelete() throws Exception {
        UUID uuid = UUID.randomUUID();
        doThrow(NotFoundException.class).when(itemService).delete(uuid);
        this.mockMvc.perform(delete("/item/{id}", uuid)).andDo(print()).andExpect(status().isNotFound())
                .andDo(document("item/{methodName}"
                        , pathParameters(parameterWithName("id").description("item id"))));
    }
}
