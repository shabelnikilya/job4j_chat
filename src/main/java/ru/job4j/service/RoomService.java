package ru.job4j.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.model.Room;
import ru.job4j.repository.RoomRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@org.springframework.stereotype.Service
public class RoomService implements Service<Room> {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Iterable<Room> findAll() {
        return roomRepository.findAll();
    }

    @Override
    public Optional<Room> findById(int id) {
        return roomRepository.findById(id);
    }

    @Override
    public Room save(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public void delete(Room room) {
        roomRepository.delete(room);
    }

    @Override
    public Room partUpdate(Room room) throws InvocationTargetException, IllegalAccessException {
        Room currentRoom = this.findById(room.getId())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
                );
        Map<String, Method> namePerMethod = loadMethod(currentRoom, new HashMap<>());
        updateFields(namePerMethod, room, currentRoom);
        this.save(room);
        return currentRoom;
    }
}
