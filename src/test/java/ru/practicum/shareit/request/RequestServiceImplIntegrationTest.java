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
        User user = new User().setName("name").setEmail("ya@ya.ru");
        User user2 = new User().setName("name2").setEmail("ya2@ya.ru");
        userRepository.save(user);
        userRepository.save(user2);
        RequestDtoIncome request = new RequestDtoIncome()
                .setDescription("I need a book");
        service.create(1, request);
        assertTrue(service.getAllWithParams(1L, 0, 10).isEmpty());
        assertEquals(1, service.getAllWithParams(2L, 0, 10).size());

    }
}