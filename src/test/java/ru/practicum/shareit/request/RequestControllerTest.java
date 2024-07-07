package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.RequestDtoIncome;

import java.time.LocalDateTime;

@WebMvcTest(controllers = RequestController.class)
class RequestControllerTest {


    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private RequestService requestService;

    @Autowired
    private MockMvc mvc;

    @Test
    void create() {
        RequestDtoIncome income = new RequestDtoIncome()
                .setDescription("Some need")
                .setCreated(LocalDateTime.now().withNano(0));
    }

    @Test
    void getByRequestId() {
    }

    @Test
    void getAllByRequesterId() {
    }

    @Test
    void getAllWithParams() {
    }
}