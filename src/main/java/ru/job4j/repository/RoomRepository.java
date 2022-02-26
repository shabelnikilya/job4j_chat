package ru.job4j.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.model.Room;

import java.util.Optional;

public interface RoomRepository extends CrudRepository<Room, Integer> {
    @Override
    @Query("select distinct r from Room r join fetch r.persons")
    Iterable<Room> findAll();

    @Override
    @Query("select distinct r from Room r join fetch r.persons where r.id = :id")
    Optional<Room> findById(@Param("id") Integer integer);
}
