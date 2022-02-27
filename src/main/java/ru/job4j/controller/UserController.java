package ru.job4j.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.model.Person;
import ru.job4j.service.PersonService;

import java.util.List;

@RestController
@RequestMapping("/person")
public class UserController {
    private final PersonService personService;
    private final BCryptPasswordEncoder encoder;

    public UserController(PersonService personService, BCryptPasswordEncoder encoder) {
        this.personService = personService;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public void signUp(@RequestBody Person person) {
        PersonController.validPerson(person);
        person.setPassword(encoder.encode(person.getPassword()));
        personService.save(person);
    }

    @GetMapping("/all")
    public List<Person> findAll() {
        return (List<Person>) personService.findAll();
    }
}
