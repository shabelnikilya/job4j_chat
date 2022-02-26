package ru.job4j.service;

import ru.job4j.model.Room;

import java.util.Optional;

@org.springframework.stereotype.Service
public class RoomService implements Service<Room> {
    private final RoomService roomRepository;

    public RoomService(RoomService roomRepository) {
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
}
