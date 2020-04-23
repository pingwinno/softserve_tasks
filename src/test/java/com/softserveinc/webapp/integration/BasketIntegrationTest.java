package com.softserveinc.webapp.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserveinc.webapp.model.Basket;
import com.softserveinc.webapp.model.Item;
import com.softserveinc.webapp.model.UnregisteredOrder;
import com.softserveinc.webapp.repository.ItemRepository;
import com.softserveinc.webapp.repository.UnregisteredOrderRepository;
import com.softserveinc.webapp.utils.EntitiesGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class BasketIntegrationTest {

    MockHttpSession mocksession;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private Basket basket;
    private MockMvc mockMvc;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UnregisteredOrderRepository unregisteredOrderRepository;
    private List<Item> items;
    private String jsonItem;

    @BeforeEach
    void init() throws JsonProcessingException {
        items = (List<Item>) itemRepository.saveAll(EntitiesGenerator.generateItems());
        ObjectMapper objectMapper = new ObjectMapper();
        jsonItem = objectMapper.writeValueAsString(items.get(0));
        this.mockMvc = webAppContextSetup(this.wac).build();
        mocksession = new MockHttpSession();
    }


    @Test
    @DisplayName("Test get all items in basket")
    void shouldReturnAllItemsWhenCallGetAll() throws Exception {
        this.mockMvc.perform(post("/basket").session(mocksession)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(jsonItem))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/basket").session(mocksession)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(jsonItem)));
    }

    @Test
    @DisplayName("Test add method")
    public void shouldReturn200WhenCallPostForAdd() throws Exception {
        this.mockMvc.perform(post("/basket").session(mocksession)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(jsonItem))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test remove method success")
    public void shouldReturn200WhenDeleteExistingItem() throws Exception {
        this.mockMvc.perform(post("/basket").session(mocksession)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content(jsonItem))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(delete("/basket/{id}", items.get(0).getId()).session(mocksession))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test remove method failed")
    public void shouldReturn400WhenDeleteExistingItem() throws Exception {
        UUID uuid = UUID.randomUUID();
        this.mockMvc.perform(delete("/basket/{id}", uuid))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test checkout")
    public void shouldReturnUUIDWhenCheckout() throws Exception {
        this.mockMvc.perform(post("/basket/checkout").session(mocksession)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content("{\"phoneNumber\": \"7773829994\"\n}"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper()
                        .writeValueAsString(((List<UnregisteredOrder>) unregisteredOrderRepository.findAll())
                                .get(0).getId())));
    }
}
