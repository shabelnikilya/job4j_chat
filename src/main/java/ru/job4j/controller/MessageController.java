package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.job4j.marker.Operation;
import ru.job4j.model.Message;
import ru.job4j.service.MessageService;
import ru.job4j.service.Service;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Validated
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
                this.service.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable @Min(1) int id) {
        Optional<Message> message = this.service.findById(id);
        return new ResponseEntity<>(
                message.orElse(new Message()),
                message.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Message> create(@Valid @RequestBody Message message) {
        validMessage(message);
        return new ResponseEntity<>(
                this.service.save(message),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Message message) {
        validMessage(message);
        this.service.save(message);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/")
    @Validated(Operation.OnCreate.class)
    public Message partUpdate(@Valid @RequestBody Message message) throws InvocationTargetException, IllegalAccessException {
        return this.service.partUpdate(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Min(1) int id) {
        Message message = new Message();
        message.setId(id);
        this.service.delete(message);
        return ResponseEntity.ok().build();
    }

    public static void validMessage(Message message) {
        if (message == null || message.getText() == null) {
            throw new NullPointerException("Body of http request is empty or text is empty(message controller)");
        }
    }
}
