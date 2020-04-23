package com.softserveinc.webapp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Objects;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(message = "Name can't be empty")
    private String name;

    @Size(min = 8, max = 64, message = "Password should be longer than 8 symbols")
    private String password;

    @DecimalMin(value = "1000000000")
    @DecimalMax(value = "9999999999")
    @NotNull
    @Column(unique = true)
    private long phoneNumber;

    @NotBlank(message = "Description can't be empty")
    private String description;

    @NotNull(message = "Role can't be null")
    private Role role;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", role=" + role +
                '}';
    }
}
