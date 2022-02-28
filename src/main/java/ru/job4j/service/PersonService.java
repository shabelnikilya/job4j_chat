package ru.job4j.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.model.Person;
import ru.job4j.repository.PersonRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyList;

@org.springframework.stereotype.Service
public class PersonService implements Service<Person>, UserDetailsService {
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

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Person user = personRepository.findByLogin(s);
        if (user == null) {
            throw new UsernameNotFoundException(s);
        }
        return new User(user.getLogin(), user.getPassword(), emptyList());
    }

    @Override
    public Person partUpdate(Person person) throws InvocationTargetException, IllegalAccessException {
        Person currentPerson = this.findById(person.getId())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
                );
        Map<String, Method> namePerMethod = loadMethod(currentPerson, new HashMap<>());
        updateFields(namePerMethod, person, currentPerson);
        this.save(person);
        return currentPerson;
    }

    public Person findByLogin(Person person) {
        return personRepository.findByLogin(person.getLogin());
    }
}
