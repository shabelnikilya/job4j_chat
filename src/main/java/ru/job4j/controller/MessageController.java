package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.model.Message;
import ru.job4j.model.Person;
import ru.job4j.service.MessageService;
import ru.job4j.service.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/message")
public class MessageController {
    private final Service<Message> service;

    public MessageController(MessageService service) {
        this.service = service;
    }

    @GetMapping("/")
    public List<Message> findAll() {
        return StreamSupport.stream(
                service.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable int id) {
        Optional<Message> message = this.service.findById(id);
        return new ResponseEntity<>(
                message.orElse(new Message()),
                message.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Message> create(@RequestBody Message message) {
        return new ResponseEntity<>(
                this.service.save(message),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Message message) {
        this.service.save(message);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Message message = new Message();
        message.setId(id);
        this.service.delete(message);
        return ResponseEntity.ok().build();
    }
}
