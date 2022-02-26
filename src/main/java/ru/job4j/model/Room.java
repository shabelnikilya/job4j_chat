package ru.job4j.model;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String password;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "rooms_persons", joinColumns = {
            @JoinColumn(name = "rooms_id", nullable = false, updatable = false)},
    inverseJoinColumns = {
            @JoinColumn(name = "persons_id", nullable = false, updatable = false)
    })
    private Set<Person> persons = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new LinkedList<>();

    public Room() {
    }

    public Room(String name, String password, Set<Person> persons, List<Message> messages) {
        this.name = name;
        this.password = password;
        this.persons = persons;
        this.messages = messages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return id == room.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
