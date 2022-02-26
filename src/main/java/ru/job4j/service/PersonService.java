package ru.job4j.service;

import ru.job4j.model.Person;
import ru.job4j.repository.PersonRepository;

import java.util.Optional;

@org.springframework.stereotype.Service
public class PersonService implements Service<Person> {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Iterable<Person> findAll() {
        return personRepository.findAll();
    }

    @Override
    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    @Override
    public Person save(Person person) {
        return personRepository.save(person);
    }

    @Override
    public void delete(Person person) {
        personRepository.delete(person);
    }
}
