package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> tester;

    @Test
    public void userDtoTest() throws IOException {
        UserDto userDto = new UserDto()
                .setName("name")
                .setEmail("ya@ya.ru");
        JsonContent<UserDto> result = tester.write(userDto);
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.email");
    }
}