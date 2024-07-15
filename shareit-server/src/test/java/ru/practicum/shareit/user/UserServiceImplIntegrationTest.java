package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserServiceImplIntegrationTest {

    @Autowired
    private UserService service;

    @Test
    @DirtiesContext
    public void createTest() {
        UserDto userDto = new UserDto()
                .setName("name")
                .setEmail("ya@ya.ru");
        UserDto savedUserDto = service.create(userDto);
        userDto.setId(1L);
        assertEquals(userDto, savedUserDto);
    }
}