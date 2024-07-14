package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @SneakyThrows
    @Test
    public void createTest() {
        UserDto userDto = new UserDto()
                .setName("name")
                .setEmail("ya@ya.ru");

        when(userService.create(Mockito.any(UserDto.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock
                        .getArgument(0, UserDto.class)
                        .setId(1L)
                        .setName(userDto.getName())
                        .setEmail(userDto.getEmail()));

        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @SneakyThrows
    @Test
    void updateTest() {
        long userId = 1L;
        UserDto userDto = new UserDto()
                .setName("name")
                .setEmail("ya@ya.ru");

        mvc.perform(MockMvcRequestBuilders.patch("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        verify(userService).update(userId, userDto);
    }

    @SneakyThrows
    @Test
    void delete() {
        long userId = 1L;
        mvc.perform(MockMvcRequestBuilders.delete("/users/{id}", userId))
                .andExpect(status().isOk());
        verify(userService).delete(userId);
    }

    @SneakyThrows
    @Test
    void get() {
        long userId = 1L;
        mvc.perform(MockMvcRequestBuilders.get("/users/{id}", userId))
                .andExpect(status().isOk());
        verify(userService).get(userId);
    }

    @SneakyThrows
    @Test
    void getAll() {
        mvc.perform(MockMvcRequestBuilders.get("/users/"))
                .andExpect(status().isOk());
        verify(userService).getAll();
    }
}