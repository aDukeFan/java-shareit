package ru.practicum.shareit.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.item.dto.ItemDtoIncome;
import ru.practicum.shareit.item.dto.ItemDtoOutcomeAvailableRequest;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {


    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    @Test
    void create() throws Exception {
        ItemDtoIncome income = new ItemDtoIncome()
                .setName("Item")
                .setDescription("for users")
                .setAvailable(true);
        ItemDtoOutcomeAvailableRequest outcome = new ItemDtoOutcomeAvailableRequest()
                .setName("Item")
                .setAvailable(true)
                .setDescription("for users")
                .setId(1);
        when(itemService.create(1, income))
                .thenAnswer(invocationOnMock -> invocationOnMock
                        .getArgument(0, ItemDtoOutcomeAvailableRequest.class)
                        .setId(1)
                        .setName(income.getName())
                        .setAvailable(income.getAvailable())
                        .setDescription(income.getDescription()));

        mvc.perform(MockMvcRequestBuilders.post("/items")
                        .content(mapper.writeValueAsString(outcome))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is(outcome.getName())))
                .andExpect(jsonPath("$.description", is(outcome.getDescription())))
                .andExpect(jsonPath("$.available", is(outcome.getAvailable())));
    }

    @Test
    void update() {
    }

    @Test
    void getItemById() {
    }

    @Test
    void getAllByOwner() {
    }

    @Test
    void findByQuery() {
    }

    @Test
    void addComment() {
    }
}