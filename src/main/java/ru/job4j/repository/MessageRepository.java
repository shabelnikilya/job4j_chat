package ru.job4j.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.model.Message;

import java.util.Optional;

public interface MessageRepository extends CrudRepository<Message, Integer> {
    @Override
    @Query("select distinct m from Message m join fetch m.person")
    Iterable<Message> findAll();

    @Override
    @Query("select distinct m from Message m join fetch m.person where m.id = :id")
    Optional<Message> findById(@Param("id") Integer integer);
}
