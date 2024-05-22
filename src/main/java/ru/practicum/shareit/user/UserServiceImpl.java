package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;

import javax.validation.ValidationException;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private InMemoryUserRepository repository;

    @Override
    public User create(User user) {
        if (repository.getAll().stream()
                .anyMatch(user1 -> user1.getEmail().equals(user.getEmail()))) {
            throw new ValidationException("Email must be unique");
        }
        return repository.save(user);
    }

    @Override
    public User update(long userId, User user) {
        if (repository.getAll().stream()
                .filter(user1 -> user1.getId() != userId)
                .anyMatch(user1 -> user1.getEmail().equals(user.getEmail()))) {
            throw new ValidationException("Email must be unique");
        }
        user.setId(userId);
        return repository.update(user);
    }

    @Override
    public void delete(long userId) {
        repository.delete(userId);
    }

    @Override
    public User get(long userId) {
        return repository.getById(userId);
    }

    @Override
    public List<User> getAll() {
        return repository.getAll();
    }
}
