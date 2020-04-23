package com.softserveinc.webapp.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@Entity(name = "UNREGISTERED_ORDERS")
public class UnregisteredOrder {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @DecimalMin(value = "1000000000")
    @DecimalMax(value = "9999999999")
    @NotNull
    private long phoneNumber;

    @NotNull(message = "State can't be null")
    private OrderState orderState;

    @OneToMany
    @NotNull(message = "Items can't be null")
    private List<Item> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UnregisteredOrder order = (UnregisteredOrder) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
