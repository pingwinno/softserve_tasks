package com.softserveinc.webapp.repository;


import com.softserveinc.webapp.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    List<User> findAll();

    List<User> findByName(String name);

    List<User> findByPhoneNumber(long phoneNumber);
}
