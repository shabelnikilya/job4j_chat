package ru.job4j.service;

import ru.job4j.model.Message;
import ru.job4j.repository.MessageRepository;

import java.util.Optional;

@org.springframework.stereotype.Service
public class MessageService implements Service<Message> {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Iterable<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Optional<Message> findById(int id) {
        return messageRepository.findById(id);
    }

    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public void delete(Message message) {
        messageRepository.delete(message);
    }
}
