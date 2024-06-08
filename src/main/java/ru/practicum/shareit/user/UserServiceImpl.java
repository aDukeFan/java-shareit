package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.Constants;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository repository;
    private UserDtoMapper userDtoMapper;

    @Override
    public UserDto create(UserDto userDto) {
        User savedUser = repository.save(userDtoMapper.toUserFromDto(userDto));
        return userDtoMapper.toUserDto(savedUser);
    }

    @Override
    public UserDto update(long userId, UserDto userDto) {
        User userToUpdate = repository.findById(userId)
                .orElseThrow(() -> new ValidationException(Constants.NO_USER_WITH_SUCH_ID + userId));
        User userToSave = userDtoMapper.toUserFromDtoToUpdate(userDto, userToUpdate);
        User finalUser = repository.save(userToSave);
        return userDtoMapper.toUserDto(finalUser);
    }

    @Override
    public void delete(long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_USER_WITH_SUCH_ID + userId));
        repository.delete(user);
    }

    @Override
    public UserDto get(long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_USER_WITH_SUCH_ID + userId));
        return userDtoMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        return repository.findAll().stream()
                .map(user -> userDtoMapper.toUserDto(user))
                .collect(Collectors.toList());
    }
}
