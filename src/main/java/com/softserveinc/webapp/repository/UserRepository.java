package com.softserveinc.webapp.repository;


import com.softserveinc.webapp.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends com.softserveinc.webapp.repository.Repository<User, Long> {

}
