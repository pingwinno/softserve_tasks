package com.softserveinc.webapp.service.implementations;

import com.softserveinc.webapp.exception.NotFoundException;
import com.softserveinc.webapp.exception.WrongParamsException;
import com.softserveinc.webapp.model.*;
import com.softserveinc.webapp.service.interfaces.BasketService;
import com.softserveinc.webapp.service.interfaces.RegisteredOrderService;
import com.softserveinc.webapp.service.interfaces.UnregisteredOrderService;
import com.softserveinc.webapp.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BasketServiceImpl implements BasketService {

    @Autowired
    private RegisteredOrderService registeredOrderService;

    @Autowired
    private UnregisteredOrderService unregisteredOrderService;

    @Autowired
    private UserService userService;

    @Override
    public List<Item> getBucket(Basket sessionBasket) {
        return sessionBasket.getItems();
    }

    @Override
    public void addItem(Item item, Basket sessionBasket) {
        sessionBasket.addItem(item);
    }

    @Override
    public void removeItem(UUID uuid, Basket sessionBasket) throws NotFoundException {
        if (!sessionBasket.removeItem(uuid)) {
            throw new NotFoundException("Item not found in this basket");
        }
    }

    @Override
    public UUID checkout(long phoneNumber, Basket sessionBasket) throws WrongParamsException {
        List<User> users = userService.getBy(Map.of("phone", String.valueOf(phoneNumber)));
        if (users.isEmpty()) {
            UnregisteredOrder registeredOrder = new UnregisteredOrder();
            registeredOrder.setPhoneNumber(phoneNumber);
            registeredOrder.setItems(sessionBasket.getItems());
            registeredOrder.setOrderState(OrderState.PROCESSING);
            return unregisteredOrderService.add(registeredOrder).getId();
        }
        RegisteredOrder registeredOrder = new RegisteredOrder();
        registeredOrder.setUser(users.get(0));
        registeredOrder.setItems(sessionBasket.getItems());
        registeredOrder.setOrderState(OrderState.PROCESSING);
        return registeredOrderService.add(registeredOrder).getId();
    }
}
