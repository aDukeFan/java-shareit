package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserServiceImplIntegrationTest {

    @Autowired
    private UserService service;

    @Test
    void createTest() {
        UserDto userDto = new UserDto()
                .setName("name")
                .setEmail("ya@ya.ru");
        UserDto savedUserDto = service.create(userDto);
        userDto.setId(1);
        assertEquals(userDto, savedUserDto);
    }
}