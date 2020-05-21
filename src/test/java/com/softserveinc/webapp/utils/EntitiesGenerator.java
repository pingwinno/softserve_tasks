package com.softserveinc.webapp.utils;

import com.softserveinc.webapp.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntitiesGenerator {

    public static List<User> generateUsers() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setId(UUID.randomUUID());
            user.setName("user" + i);
            user.setPassword("somePass" + i);
            user.setPhoneNumber(PhoneNumberGenerator.generate());
            user.setDescription("some awesome user" + i);
            user.setRole(Role.ADMIN);
            users.add(user);
        }
        return users;
    }

    public static List<Item> generateItems() {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Item item = new Item();
            item.setName("item");
            item.setPrice(BigDecimal.TEN);
            item.setDescription("some awesome item");
            item.setCategory("category");
            items.add(item);
        }
        return items;
    }

    public static List<RegisteredOrder> generateRegisteredOrders(List<User> users, List<Item> items) {
        List<RegisteredOrder> orders = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            RegisteredOrder order = new RegisteredOrder();
            order.setUser(users.get(i));
            int finalI = i;
            order.setOrderState(OrderState.DELIVERING);
            order.setItems(new ArrayList<>() {
                {
                    add(items.get(finalI));
                }
            });
            orders.add(order);
        }
        return orders;
    }

    public static List<UnregisteredOrder> generateUnregisteredOrders(List<Item> items) {
        List<UnregisteredOrder> orders = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            UnregisteredOrder order = new UnregisteredOrder();
            order.setId(UUID.randomUUID());
            long phoneNumber = PhoneNumberGenerator.generate();
            order.setPhoneNumber(phoneNumber);
            order.setOrderState(OrderState.DELIVERING);
            int finalI = i;
            order.setItems(new ArrayList<>() {
                {
                    add(items.get(finalI));
                }
            });
            orders.add(order);
        }
        return orders;
    }
}
