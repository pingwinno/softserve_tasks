package com.softserveinc.webapp.service.interfaces;

import com.softserveinc.webapp.exception.NotFoundException;
import com.softserveinc.webapp.exception.WrongParamsException;
import com.softserveinc.webapp.model.Basket;
import com.softserveinc.webapp.model.Item;

import java.util.List;
import java.util.UUID;

public interface BasketService {

    List<Item> getBucket(Basket sessionBasket);

    void addItem(Item item, Basket sessionBasket);

    void removeItem(UUID uuid, Basket sessionBasket) throws NotFoundException;

    UUID checkout(long phoneNumber, Basket sessionBasket) throws WrongParamsException;
}
