package com.softserveinc.webapp.model;

import lombok.Data;

import java.util.*;

@Data
public class Basket {
    private Map<UUID, Item> items;

    public Basket() {
        items = new HashMap<>();
    }

    public List<Item> getItems() {
        return new ArrayList<>(items.values());
    }

    public void setItems(List<Item> items) {
        items.forEach(item -> this.items.put(item.getId(), item));

    }

    public void addItem(Item item) {
        items.put(item.getId(), item);
    }

    public boolean removeItem(UUID uuid) {
        return items.remove(uuid) != null;
    }
}
