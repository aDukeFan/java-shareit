package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.ValidationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private UserService userService;
    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(repository, userMapper);
    }
    @Test
    void updateWithException() {
        assertThrows(ValidationException.class, () -> userService.update(1, new UserDto()));
        verify(repository).findById(anyLong());
    }
}