package ru.job4j.service;

import java.util.Optional;

public interface Service<T> {

    Iterable<T> findAll();

    Optional<T> findById(int id);

    T save(T t);

    void delete(T t);
}
