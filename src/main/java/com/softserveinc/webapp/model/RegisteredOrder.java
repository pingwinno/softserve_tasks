package com.softserveinc.webapp.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@Entity(name = "REGISTERED_ORDERS")
public class RegisteredOrder {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotNull(message = "User can't be null")
    @OneToOne
    private User user;

    @NotNull(message = "Items can't be null")
    @OneToMany
    private List<Item> items;

    @NotNull(message = "State can't be null")
    private OrderState orderState;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegisteredOrder order = (RegisteredOrder) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
