package com.softserveinc.webapp.service.interfaces;

import com.softserveinc.webapp.exception.NotFoundException;
import com.softserveinc.webapp.exception.WrongParamsException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface Service<T> {
    T get(UUID id) throws NotFoundException;

    List<T> getAll();

    List<T> getBy(Map<String, String> request) throws WrongParamsException;

    T add(T t) throws WrongParamsException;

    void update(UUID id, T t) throws NotFoundException, WrongParamsException;

    void delete(UUID id) throws NotFoundException;
}
