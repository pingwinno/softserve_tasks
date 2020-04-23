package com.softserveinc.webapp.controller;

import com.softserveinc.webapp.model.RegisteredOrder;
import com.softserveinc.webapp.service.interfaces.RegisteredOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("registered_order")
public class RegisteredOrderController {

    @Autowired
    private RegisteredOrderService orderService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RegisteredOrder> getItem() {
        return orderService.getAll();
    }

    @GetMapping(path = "search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RegisteredOrder> getItem(@RequestParam Map<String, String> request) {
        return orderService.getBy(request);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RegisteredOrder getItem(@PathVariable UUID id) {
        return orderService.get(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public RegisteredOrder addItem(@RequestBody RegisteredOrder bucket) {
        return orderService.add(bucket);
    }

    @PatchMapping("/{id}")
    public void updateItem(@PathVariable UUID id, @RequestBody RegisteredOrder bucket) {
        orderService.update(id, bucket);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable UUID id) {
        orderService.delete(id);
    }
}
