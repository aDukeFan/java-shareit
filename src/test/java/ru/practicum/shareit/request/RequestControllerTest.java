package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.request.dto.RequestDtoIncome;
import ru.practicum.shareit.util.Constants;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RequestController.class)
class RequestControllerTest {


    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private RequestService requestService;

    @Autowired
    private MockMvc mvc;

    @SneakyThrows
    @Test
    void create() {
        long bookerId = 1L;
        RequestDtoIncome income = new RequestDtoIncome()
                .setDescription("Some need")
                .setCreated(LocalDateTime.now().withNano(0));
        mvc.perform(MockMvcRequestBuilders.post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constants.X_SHARER_USER_ID, bookerId)
                        .content(mapper.writeValueAsString(income)))
                .andExpect(status().isOk());
        verify(requestService).create(bookerId, income);
    }

    @SneakyThrows
    @Test
    void getByRequestId() {
        long userId = 1L;
        long requestId = 1L;
        mvc.perform(MockMvcRequestBuilders.get("/requests/{requestId}", requestId)
                        .header(Constants.X_SHARER_USER_ID, userId))
                .andExpect(status().isOk());
        verify(requestService).getByRequestId(userId, requestId);
    }

    @SneakyThrows
    @Test
    void getAllByRequesterId() {
        long requesterId = 1L;
        mvc.perform(MockMvcRequestBuilders.get("/requests")
                        .header(Constants.X_SHARER_USER_ID, requesterId))
                .andExpect(status().isOk());
        verify(requestService).getAllByRequesterId(requesterId);
    }

    @SneakyThrows
    @Test
    void getAllWithParams() {
        long userId = 1L;
        Integer from = 1;
        Integer size = 1;
        mvc.perform(MockMvcRequestBuilders.get("/requests/all")
                        .header(Constants.X_SHARER_USER_ID, userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());
        verify(requestService).getAllWithParams(userId, from, size);
    }
}