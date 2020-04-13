package com.softserveinc.webapp.repository;

import com.softserveinc.webapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;

@org.springframework.stereotype.Repository
public class UserUserRepositoryImpl implements UserRepository {


    @Autowired
    private Map<Long, User> userStorage;

    @Override
    public User save(User user) {
        return userStorage.put(user.getId(), user);
    }

    @Override
    public <S extends User> Iterable<S> saveAll(Iterable<S> iterable) {
        iterable.forEach(user -> userStorage.put(user.getId(), user));
        return (Iterable<S>) userStorage.values();
    }

    @Override
    public Optional<User> findById(Long o) {
        return Optional.ofNullable(userStorage.get(o));
    }

    @Override
    public boolean existsById(Long aLong) {
        return userStorage.containsKey(aLong);
    }

    @Override
    public Iterable<User> findAll() {
        return new ArrayList<>(userStorage.values());
    }

    @Override
    public void delete(User user) {
        userStorage.remove(user.getId());
    }

    @Override
    public void deleteAll() {
        userStorage.clear();
    }

    @Override
    public boolean isEmpty() {
        return userStorage.isEmpty();
    }


}
