package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.dto.RequestDtoIncome;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RequestServiceImplIntegrationTest {

    @Autowired
    private RequestService service;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DirtiesContext
    public void getAllWithParamsTest() {
        User firstUser = new User().setName("First").setEmail("ya@ya.ru");
        User secondUser = new User().setName("Second").setEmail("ya2@ya.ru");
        userRepository.save(firstUser);
        userRepository.save(secondUser);
        RequestDtoIncome request = new RequestDtoIncome()
                .setDescription("I need a book");
        service.create(1, request);
        assertTrue(service.getAllWithParams(1L, 0, 10).isEmpty());
        assertEquals(1, service.getAllWithParams(2L, 0, 10).size());

    }
}