package ru.job4j.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.model.Message;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

public interface Service<T> {
    Logger LOGGER = LoggerFactory.getLogger(MessageService.class.getSimpleName());

    Iterable<T> findAll();

    Optional<T> findById(int id);

    T save(T t);

    void delete(T t);

    T partUpdate(T t) throws InvocationTargetException, IllegalAccessException;

    default Map<String, Method> loadMethod(T t, Map<String, Method> namePerMethod) {
        Method[] methods = t.getClass().getMethods();
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                namePerMethod.put(name, method);
            }
        }
        return namePerMethod;
    }

    default void updateFields(Map<String, Method> namePerMethod, T t, T currentT)
            throws InvocationTargetException, IllegalAccessException {
        for (String name : namePerMethod.keySet()) {
            if (name.startsWith("get")) {
                Method getMethod = namePerMethod.get(name);
                Method setMethod = namePerMethod.get(name.replace("get", "set"));
                if (setMethod == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid properties mapping");
                }
                    var newValue = getMethod.invoke(t);
                    if (newValue != null) {
                        setMethod.invoke(currentT, newValue);
                    }
            }
        }
    }
}
