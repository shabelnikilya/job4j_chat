package ru.job4j.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.job4j.marker.Operation;
import ru.job4j.model.Message;
import ru.job4j.model.Room;
import ru.job4j.service.RoomService;
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
@RequestMapping("/room")
public class RoomController {
    private static final String API = "http://localhost:8080/message/";
    private static final String API_ID = "http://localhost:8080/message/{id}";
    private final Service<Room> service;
    @Autowired
    private RestTemplate rest;

    public RoomController(RoomService service) {
        this.service = service;
    }

    @GetMapping("/")
    public List<Room> findAll() {
        return StreamSupport.stream(this.service.findAll()
                .spliterator(), false
        ).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> findById(@PathVariable @Min(1) int id) {
        Optional<Room> room = this.service.findById(id);
        return new ResponseEntity<>(
                room.orElse(new Room()),
                room.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Room> create(@Valid @RequestBody Room room) {
        validPerson(room);
        return new ResponseEntity<>(
                this.service.save(room),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Room room) {
        validPerson(room);
        this.service.save(room);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/")
    @Validated(Operation.OnUpdate.class)
    public Room partUpdate(@Valid @RequestBody Room room) throws InvocationTargetException, IllegalAccessException {
        return this.service.partUpdate(room);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Min(1) int id) {
        Room room = new Room();
        room.setId(id);
        this.service.delete(room);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/message")
    public List<Message> findAllMessage() {
        return rest.exchange(
                API,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Message>>() { }
        ).getBody();
    }

    @PostMapping("/message")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Message> create(@Valid @RequestBody Message message) {
        MessageController.validMessage(message);
        Message rsl = rest.postForObject(API, message, Message.class);
        return new ResponseEntity<>(
                rsl,
                HttpStatus.CREATED
        );
    }

    @PutMapping("/message")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Message message) {
        MessageController.validMessage(message);
        rest.put(API, message);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/message/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable @Min(1) int id) {
        rest.delete(API_ID, id);
        return ResponseEntity.ok().build();
    }

    public static void validPerson(Room room) {
        if (room == null || room.getName() == null ||  room.getPassword() == null) {
            throw new NullPointerException("Body of http request is empty or parameters is null(room controller)");
        }
    }
}
