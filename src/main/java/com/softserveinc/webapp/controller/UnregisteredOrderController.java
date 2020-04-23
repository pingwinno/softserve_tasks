package com.softserveinc.webapp.controller;

import com.softserveinc.webapp.model.UnregisteredOrder;
import com.softserveinc.webapp.service.interfaces.UnregisteredOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("unregistered_order")
public class UnregisteredOrderController {

    @Autowired
    private UnregisteredOrderService orderService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UnregisteredOrder> getItem() {
        return orderService.getAll();
    }

    @GetMapping(path = "search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UnregisteredOrder> getItem(@RequestParam Map<String, String> request) {
        return orderService.getBy(request);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UnregisteredOrder getItem(@PathVariable UUID id) {
        return orderService.get(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public UnregisteredOrder addItem(@RequestBody UnregisteredOrder order) {
        return orderService.add(order);
    }

    @PatchMapping("/{id}")
    public void updateItem(@PathVariable UUID id, @RequestBody UnregisteredOrder order) {
        orderService.update(id, order);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable UUID id) {
        orderService.delete(id);
    }
}
