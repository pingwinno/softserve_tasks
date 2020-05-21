package com.softserveinc.webapp.repository;

import com.softserveinc.webapp.model.OrderState;
import com.softserveinc.webapp.model.UnregisteredOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UnregisteredOrderRepository extends CrudRepository<UnregisteredOrder, UUID> {
    List<UnregisteredOrder> findByPhoneNumber(long phoneNumber);

    List<UnregisteredOrder> findByOrderState(OrderState orderState);
}
