package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.Constants;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private InMemoryUserRepository repository;
    private UserDtoMapper userDtoMapper;

    @Override
    public UserDto create(UserDto userDto) {
        throwValidExceptionForDuplicateEmail(userDto.getEmail());
        User savedUser = repository.save(userDtoMapper.toUserFromDto(userDto));
        return userDtoMapper.toUserDto(savedUser);
    }

    @Override
    public UserDto update(long userId, UserDto userDto) {
        User user = repository.getById(userId);
        if (userDto.getEmail() != null && !userDto.getEmail().equals(user.getEmail())) {
            repository.getEmails().remove(user.getEmail());
            throwValidExceptionForDuplicateEmail(userDto.getEmail());
        }
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

    private void throwValidExceptionForDuplicateEmail(String email) {
        if (repository.getEmails().contains(email)) {
            log.info("Try to save or update user with duplicate email: {}", email);
            throw new ValidationException(Constants.MESSAGE_FOR_DUPLICATE_EMAIL + email);
        }
    }
}
