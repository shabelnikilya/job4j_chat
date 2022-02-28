package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.model.Message;
import ru.job4j.model.Person;
import ru.job4j.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class.getSimpleName());
    private final PersonService service;
    private final ObjectMapper objectMapper;

    public PersonController(PersonService service, ObjectMapper objectMapper) {
        this.service = service;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public ResponseEntity<List<Person>> findAll() {
        return new ResponseEntity<List<Person>>(
                (List) service.findAll(), HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        Optional<Person> person = this.service.findById(id);
        if (person.isEmpty()) {
            new ResponseStatusException(HttpStatus.NOT_FOUND, "person not find. Please check id");
        }
        return new ResponseEntity<>(
                person.get(), HttpStatus.OK
        );
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        validPerson(person);
        if (service.findByLogin(person) != null) {
            throw new IllegalArgumentException("This login already exists");
        }
        return new ResponseEntity<>(
                this.service.save(person),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        validPerson(person);
        this.service.save(person);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/")
    public Person partUpdate(@RequestBody Person person) throws InvocationTargetException, IllegalAccessException {
        return this.service.partUpdate(person);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        this.service.delete(person);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public void exceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", e.getMessage());
            put("type", e.getClass());
        }}));
        LOGGER.error(e.getLocalizedMessage());
    }

    public static void validPerson(Person person) {
        if (person == null || person.getName() == null || person.getSurname() == null
        || person.getLogin() == null || person.getPassword() == null || person.getRole() == null) {
            throw new NullPointerException("Body of http request is empty or parameters is null(person controller)");
        }
    }
}
