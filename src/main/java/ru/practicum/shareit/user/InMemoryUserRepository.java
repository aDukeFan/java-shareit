package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;

    public User save(User user) {
        user.setId(nextId);
        users.put(nextId, user);
        nextId++;
        return user;
    }

    public User update(User user) {
        User userToUpdate = getById(user.getId());
        if (userToUpdate != null) {
            if (user.getName() != null) {
                userToUpdate.setName(user.getName());
            }
            if (user.getEmail() != null) {
                userToUpdate.setEmail(user.getEmail());
            }
        }
        return userToUpdate;
    }

    public void delete(long id) {
        users.remove(id);
    }

    public User getById(long id) {
        return users.get(id);
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }
}
