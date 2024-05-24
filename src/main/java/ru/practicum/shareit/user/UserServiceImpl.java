package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private InMemoryUserRepository repository;
    private UserDtoMapper userDtoMapper;

    @Override
    public UserDto create(UserDto userDto) {
        if (repository.getAll().stream()
                .anyMatch(user1 -> user1.getEmail().equals(userDto.getEmail()))) {
            throw new ValidationException("Email must be unique");
        }
        User savedUser = repository.save(userDtoMapper.toUserFromDto(userDto));
        return userDtoMapper.toUserDto(savedUser);
    }

    @Override
    public UserDto update(long userId, UserDto userDto) {
        if (repository.getAll().stream()
                .filter(user1 -> user1.getId() != userId)
                .anyMatch(user1 -> user1.getEmail().equals(userDto.getEmail()))) {
            throw new ValidationException("Email must be unique");
        }
        User user = repository.getById(userId);
        User updatedUser = userDtoMapper.toUserFromDtoToUpdate(userDto, user);
        return userDtoMapper.toUserDto(repository.update(updatedUser));
    }

    @Override
    public void delete(long userId) {
        repository.delete(userId);
    }

    @Override
    public UserDto get(long userId) {
        return userDtoMapper.toUserDto(repository.getById(userId));
    }

    @Override
    public List<UserDto> getAll() {
        return repository.getAll().stream()
                .map(user -> userDtoMapper.toUserDto(user))
                .collect(Collectors.toList());
    }
}
