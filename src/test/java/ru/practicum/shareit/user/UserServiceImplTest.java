package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.ValidationException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private UserService userService;

    @Mock
    private UserRepository repository;

    private final UserMapper userMapper = new UserMapperImpl();

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(repository, userMapper);
    }

    @Test
    public void updateWithException() {
        assertThrows(ValidationException.class, () -> userService.update(1, new UserDto()));
        verify(repository).findById(anyLong());
    }

    @Test
    public void updateTest() {
        User user = new User()
                .setId(1L)
                .setName("Name")
                .setEmail("ya@ya.ru");
        UserDto userDto = new UserDto()
                .setEmail("toUpdate@ya.ru");
        User userToUpdate = userMapper.updateUserFromDto(userDto, user);
        when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        when(repository.save(userToUpdate))
                .thenReturn(userToUpdate);
        assertEquals(userToUpdate.getEmail(),
                userService.update(1L, userDto).getEmail());
        verify(repository).findById(anyLong());
        verify(repository).save(user);
    }

    @Test
    public void getTest() {
        User user = new User()
                .setId(1L)
                .setName("Name")
                .setEmail("ya@ya.ru");
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        assertEquals(userMapper.toDto(user), userService.get(1L));
    }

    @Test
    public void getAllTest() {
        assertTrue(userService.getAll().isEmpty());
    }

    @Test
    public void deleteTest() {
        userService.delete(anyLong());
        verify(repository).deleteById(anyLong());
    }
}