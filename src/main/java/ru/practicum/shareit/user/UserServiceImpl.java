package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.Constants;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository repository;
    private final UserMapper userMapper;

    @Override
    public UserDto create(UserDto userDto) {
        User savedUser = repository.save(userMapper.toUser(userDto));
        return userMapper.toDto(savedUser);
    }

    @Override
    @CachePut(value = "userDto", key = "#userId")
    public UserDto update(long userId, UserDto userDto) {
        User userToUpdate = repository.findById(userId)
                .orElseThrow(() -> new ValidationException(Constants.NO_USER_WITH_SUCH_ID + userId));
        userDto.setId(userId);
        User savedUser = repository.save(userMapper.updateUserFromDto(userDto, userToUpdate));
        return userMapper.toDto(savedUser);
    }

    @Override
    @CacheEvict("users")
    public void delete(long userId) {
        repository.deleteById(userId);
    }

    @Override
    @Cacheable("users")
    public UserDto get(long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_USER_WITH_SUCH_ID + userId));
        return userMapper.toDto(user);
    }

    @Override
    @Cacheable("users")
    public List<UserDto> getAll() {
        return repository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}
