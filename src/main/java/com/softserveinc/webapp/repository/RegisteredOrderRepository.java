package com.softserveinc.webapp.repository;

import com.softserveinc.webapp.model.OrderState;
import com.softserveinc.webapp.model.RegisteredOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RegisteredOrderRepository extends CrudRepository<RegisteredOrder, UUID> {
    List<RegisteredOrder> findAll();

    List<RegisteredOrder> findByUserId(UUID uuid);

    List<RegisteredOrder> findByOrderState(OrderState orderState);
}
