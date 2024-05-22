package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryUserRepository {

    private final List<User> users = new ArrayList<>();
    private long nextId = 1;

    public User save(User user) {
        user.setId(nextId);
        users.add(user);
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
        users.removeIf(user -> user.getId() == id);
    }

    public User getById(long id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst().orElse(null);
    }

    public List<User> getAll() {
        return users;
    }
}
