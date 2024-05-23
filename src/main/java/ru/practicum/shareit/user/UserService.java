package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    UserDto create(UserDto userDto);

    UserDto update(long userId, UserDto userDto);

    void delete(long userId);

    UserDto get(long userId);

    List<User> getAll();
}
