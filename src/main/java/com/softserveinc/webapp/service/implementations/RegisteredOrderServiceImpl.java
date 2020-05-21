package com.softserveinc.webapp.service.implementations;

import com.softserveinc.webapp.exception.NotFoundException;
import com.softserveinc.webapp.exception.WrongParamsException;
import com.softserveinc.webapp.model.OrderState;
import com.softserveinc.webapp.model.RegisteredOrder;
import com.softserveinc.webapp.repository.RegisteredOrderRepository;
import com.softserveinc.webapp.service.DataValidator;
import com.softserveinc.webapp.service.interfaces.RegisteredOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class RegisteredOrderServiceImpl implements RegisteredOrderService {

    @Autowired
    private DataValidator dataValidator;
    @Autowired
    private RegisteredOrderRepository orderRepository;

    @Override
    public RegisteredOrder get(UUID id) throws NotFoundException {
        return orderRepository
                .findById(id)
                .orElseThrow(() -> new
                        NotFoundException(String.format("Order with id=%s not found", id)));
    }

    @Override
    public List<RegisteredOrder> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<RegisteredOrder> getBy(Map<String, String> request) throws WrongParamsException {
        if (request.containsKey("user_id")) {
            return orderRepository.findByUserId(UUID.fromString(request.get("user_id")));
        }
        if (request.containsKey("order_state")) {
            return orderRepository.findByOrderState(OrderState.valueOf(request.get("order_state")));
        }
        throw new WrongParamsException(String.format("key %s not found", request));
    }

    @Override
    public RegisteredOrder add(RegisteredOrder bucket) throws WrongParamsException {
        dataValidator.validate(bucket);
        return orderRepository.save(bucket);
    }

    @Override
    public void update(UUID id, RegisteredOrder bucket) throws NotFoundException, WrongParamsException {
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
