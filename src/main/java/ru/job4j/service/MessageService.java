package ru.job4j.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.model.Message;
import ru.job4j.repository.MessageRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
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

    @Override
    public Message partUpdate(Message message) throws InvocationTargetException, IllegalAccessException {
        Message currentMessage = this.findById(message.getId())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
                );
        Map<String, Method> namePerMethod = loadMethod(currentMessage, new HashMap<>());
        updateFields(namePerMethod, message, currentMessage);
        this.save(message);
        return currentMessage;
    }
}
