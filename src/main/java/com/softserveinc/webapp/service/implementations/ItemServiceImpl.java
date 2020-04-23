package com.softserveinc.webapp.service.implementations;

import com.softserveinc.webapp.exception.NotFoundException;
import com.softserveinc.webapp.exception.WrongParamsException;
import com.softserveinc.webapp.model.Item;
import com.softserveinc.webapp.repository.ItemRepository;
import com.softserveinc.webapp.service.DataValidator;
import com.softserveinc.webapp.service.interfaces.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ItemServiceImpl implements ItemService {

    private static final String CATEGORY = "category";
    private static final String NAME = "name";
    @Autowired
    private DataValidator dataValidator;
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Item get(UUID id) throws NotFoundException {
        return itemRepository
                .findById(id)
                .orElseThrow(() -> new
                        NotFoundException(String.format("Item with id=%s not found", id)));
    }

    @Override
    public List<Item> getAll() {
        return (List<Item>) itemRepository.findAll();
    }

    @Override
    public List<Item> getBy(Map<String, String> request) throws WrongParamsException {
        if (request.containsKey(CATEGORY) && request.containsKey(NAME)) {
            return itemRepository.findByNameAndCategory(request.get(NAME), request.get(CATEGORY));
        }
        if (request.containsKey(NAME)) {
            return itemRepository.findByName(request.get(NAME));
        }
        if (request.containsKey(CATEGORY)) {
            return itemRepository.findByCategory(request.get(CATEGORY));
        }
        throw new WrongParamsException(String.format("key %s not found", request));
    }

    @Override
    public Item add(Item item) throws WrongParamsException {
        dataValidator.validate(item);
        return itemRepository.save(item);
    }

    @Override
    public void update(UUID id, Item item) throws NotFoundException, WrongParamsException {
        dataValidator.validate(item);
        itemRepository
                .findById(id)
                .orElseThrow(
                        () -> new
                                NotFoundException(String.format("Item with id=%s not found", id)));
        itemRepository.save(item);
    }

    @Override
    public void delete(UUID id) throws NotFoundException {
        itemRepository.delete(itemRepository
                .findById(id)
                .orElseThrow(
                        () -> new
                                NotFoundException(String.format("Item with id=%s not found", id))));
    }
}
