package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.model.Person;

public interface PersonRepository extends CrudRepository<Person, Integer> {
        Person findByLogin(String name);
}
