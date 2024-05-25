package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;

import javax.validation.ValidationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceImplTest {

    private UserServiceImpl service;

    @BeforeEach
    void createService() {
        this.service = new UserServiceImpl(
                new InMemoryUserRepository(),
                new UserDtoMapper());
    }

    @BeforeEach
    void start() {
        for (int i = 1; i < 6; i++) {
            String email = "email" + i + "@ya.ru";
            service.create(new UserDto().setEmail(email));
        }
    }

    @Test
    void createUserWithDuplicateEmail() {
        assertThrows(ValidationException.class,
                () -> service.create(new UserDto().setEmail("email1@ya.ru")));
    }

    @ParameterizedTest
    @CsvSource(value = {"1, email2@ya.ru", "2, email3@ya.ru", "3, email1@ya.ru"})
    void updateUserWithDuplicateEmail(long id, String email) {
        assertThrows(ValidationException.class,
                () -> service.update(id, new UserDto().setEmail(email)));
    }
}