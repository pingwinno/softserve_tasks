package com.softserveinc.webapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserveinc.webapp.model.Basket;
import com.softserveinc.webapp.model.Item;
import com.softserveinc.webapp.service.interfaces.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/basket")
public class BasketController {

    @Autowired
    private BasketService basketService;

    @Autowired
    private Basket basket;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Item> getBucket() {
        return basketService.getBucket(basket);
    }

    @PostMapping
    public void addItem(@RequestBody Item item) {
        basketService.addItem(item, basket);
    }

    @DeleteMapping(path = "{id}")
    public void deleteItem(@PathVariable("id") UUID uuid) {
        basketService.removeItem(uuid, basket);
    }

    @PostMapping(path = "/checkout", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public UUID checkoutOrder(@RequestBody String phoneJson) throws JsonProcessingException {
        JsonNode root = new ObjectMapper().readTree(phoneJson);
        return basketService.checkout(root.path("phoneNumber").asLong(), basket);
    }
}
