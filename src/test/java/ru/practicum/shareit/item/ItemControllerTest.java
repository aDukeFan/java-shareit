package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.item.dto.CommentDtoIncome;
import ru.practicum.shareit.item.dto.ItemDtoIncome;
import ru.practicum.shareit.util.Constants;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    @SneakyThrows
    @Test
    void createTest() {
        long userId = 1L;
        ItemDtoIncome income = new ItemDtoIncome()
                .setName("Item")
                .setDescription("for users")
                .setAvailable(true);

        mvc.perform(MockMvcRequestBuilders.post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constants.X_SHARER_USER_ID, userId)
                        .content(mapper.writeValueAsString(income)))
                .andExpect(status().isOk());

        verify(itemService).create(userId, income);
    }

    @SneakyThrows
    @Test
    void updateTest() {
        long userId = 1L;
        long itemId = 1L;
        ItemDtoIncome income = new ItemDtoIncome()
                .setName("Item")
                .setDescription("for users")
                .setAvailable(true);
        mvc.perform(MockMvcRequestBuilders.patch("/items/{id}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constants.X_SHARER_USER_ID, userId)
                        .content(mapper.writeValueAsString(income)))
                .andExpect(status().isOk());
        verify(itemService).update(itemId, userId, income);
    }

    @SneakyThrows
    @Test
    void getItemByIdTest() {
        long userId = 1L;
        long itemId = 1L;
        mvc.perform(MockMvcRequestBuilders.get("/items/{id}", itemId)
                        .header(Constants.X_SHARER_USER_ID, userId))
                .andExpect(status().isOk());
        verify(itemService).getItemById(itemId, userId);
    }

    @SneakyThrows
    @Test
    void getAllByOwnerTest() {
        long userId = 1L;
        Integer from = 1;
        Integer size = 1;
        mvc.perform(MockMvcRequestBuilders.get("/items")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constants.X_SHARER_USER_ID, userId))
                .andExpect(status().isOk());
        verify(itemService).getAllItemsByOwner(userId, from, size);
    }

    @SneakyThrows
    @Test
    void findByQueryTest() {
        String text = "some text";
        Integer from = 1;
        Integer size = 1;
        mvc.perform(MockMvcRequestBuilders.get("/items/search")
                        .param("text", text)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(itemService).findByQuery(text, from, size);
    }

    @SneakyThrows
    @Test
    void addCommentTest() {
        long userId = 1L;
        long itemId = 1L;
        CommentDtoIncome income = new CommentDtoIncome().setText("good comment");
        mvc.perform(MockMvcRequestBuilders.post("/items/{itemId}/comment", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(income))
                        .header(Constants.X_SHARER_USER_ID, userId))
                .andExpect(status().isOk());
        verify(itemService).addComment(userId, itemId, income);
    }
}