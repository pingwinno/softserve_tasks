package com.softserveinc.webapp.controller;

import com.softserveinc.webapp.model.Item;
import com.softserveinc.webapp.service.interfaces.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Item> getItem() {
        return itemService.getAll();
    }

    @GetMapping(path = "search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Item> getItem(@RequestParam Map<String, String> request) {
        return itemService.getBy(request);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Item getItem(@PathVariable UUID id) {
        return itemService.get(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Item addItem(@RequestBody Item item) {
        return itemService.add(item);
    }

    @PatchMapping("/{id}")
    public void updateItem(@PathVariable UUID id, @RequestBody Item item) {
        itemService.update(id, item);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable UUID id) {
        itemService.delete(id);
    }
}
