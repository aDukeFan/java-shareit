package ru.practicum.shareit.user;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
public class InMemoryUserRepository {

    private final Map<Long, User> users = new HashMap<>();
    @Getter
    private final Set<String> emails = new HashSet<>();
    private long nextId = 1;

    public User save(User user) {
        user.setId(nextId);
        users.put(nextId, user);
        emails.add(user.getEmail());
        nextId++;
        return user;
    }

    public User update(User user) {
        emails.add(user.getEmail());
        users.put(user.getId(), user);
        return user;
    }

    public void delete(long id) {
        emails.remove(users.get(id).getEmail());
        users.remove(id);
    }

    public User getById(long id) {
        return users.get(id);
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

}
