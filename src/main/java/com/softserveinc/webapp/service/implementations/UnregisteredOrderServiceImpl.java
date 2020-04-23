package com.softserveinc.webapp.service.implementations;

import com.softserveinc.webapp.exception.NotFoundException;
import com.softserveinc.webapp.exception.WrongParamsException;
import com.softserveinc.webapp.model.OrderState;
import com.softserveinc.webapp.model.UnregisteredOrder;
import com.softserveinc.webapp.repository.UnregisteredOrderRepository;
import com.softserveinc.webapp.service.DataValidator;
import com.softserveinc.webapp.service.interfaces.UnregisteredOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UnregisteredOrderServiceImpl implements UnregisteredOrderService {

    @Autowired
    private DataValidator dataValidator;
    @Autowired
    private UnregisteredOrderRepository orderRepository;

    @Override
    public UnregisteredOrder get(UUID id) throws NotFoundException {
        return orderRepository
                .findById(id)
                .orElseThrow(() -> new
                        NotFoundException(String.format("Order with id=%s not found", id)));
    }

    @Override
    public List<UnregisteredOrder> getAll() {
        return (List<UnregisteredOrder>) orderRepository.findAll();
    }

    @Override
    public List<UnregisteredOrder> getBy(Map<String, String> request) throws WrongParamsException {
        if (request.containsKey("phone")) {
            return orderRepository.findByPhoneNumber(Long.parseLong(request.get("phone")));
        }
        if (request.containsKey("order_state")) {
            return orderRepository.findByOrderState(OrderState.valueOf(request.get("order_state")));
        }
        throw new WrongParamsException(String.format("key %s not found", request));
    }

    @Override
    public UnregisteredOrder add(UnregisteredOrder bucket) throws WrongParamsException {
        dataValidator.validate(bucket);
        return orderRepository.save(bucket);
    }

    @Override
    public void update(UUID id, UnregisteredOrder bucket) throws NotFoundException, WrongParamsException {
        if (bucket == null) {
            throw new NotFoundException(String.format("Order with id=%s not found", id));
        }
        dataValidator.validate(bucket);
        orderRepository
                .findById(id)
                .orElseThrow(
                        () -> new
                                NotFoundException(String.format("Order with id=%s not found", id)));
        orderRepository.save(bucket);
    }

    @Override
    public void delete(UUID id) throws NotFoundException {
        orderRepository.delete(orderRepository
                .findById(id)
                .orElseThrow(
                        () -> new
                                NotFoundException(String.format("Order with id=%s not found", id))));
    }
}
