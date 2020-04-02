package com.softserveinc.webapp.repository;

import java.util.Optional;

public interface Repository<T, ID> {

    <S extends T> S save(S s);

    <S extends T> Iterable<S> saveAll(Iterable<S> iterable);

    Optional<T> findById(ID aLong);

    boolean existsById(ID aLong);

    Iterable<T> findAll();

    void delete(T user);

    void deleteAll();

    boolean isEmpty();

}
