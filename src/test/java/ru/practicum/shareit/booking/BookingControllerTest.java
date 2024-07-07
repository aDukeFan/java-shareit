package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.booking.booking_getter.model.BookingGetter;
import ru.practicum.shareit.booking.booking_getter.model.BookingGetterType;
import ru.practicum.shareit.booking.dto.BookingDtoIncome;
import ru.practicum.shareit.util.Constants;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {


    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    @SneakyThrows
    @Test
    void createBooking() {
        long userId = 1L;
        BookingDtoIncome income = new BookingDtoIncome()
                .setItemId(1L)
                .setStart(LocalDateTime.now().withNano(0).plusDays(1))
                .setEnd(LocalDateTime.now().withNano(0).plusDays(3));
        mvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constants.X_SHARER_USER_ID, userId)
                        .content(mapper.writeValueAsString(income)))
                .andExpect(status().isOk());
        verify(bookingService).createBooking(userId, income);
    }

    @SneakyThrows
    @Test
    void approveBooking() {
        long userId = 1L;
        long bookingId = 1L;
        mvc.perform(MockMvcRequestBuilders.patch("/bookings/{bookingId}", bookingId)
                        .header(Constants.X_SHARER_USER_ID, userId)
                        .param("approved", String.valueOf(true)))
                .andExpect(status().isOk());
        verify(bookingService).approveBooking(userId, bookingId, true);
    }

    @SneakyThrows
    @Test
    void getBooking() {
        long userId = 1L;
        long bookingId = 1L;
        mvc.perform(MockMvcRequestBuilders.get("/bookings/{bookingId}", bookingId)
                        .header(Constants.X_SHARER_USER_ID, userId))
                .andExpect(status().isOk());
        verify(bookingService).getBooking(userId, bookingId);
    }

    @SneakyThrows
    @Test
    void getBookerBookings() {
        long userId = 1L;
        Integer from = 1;
        Integer size = 1;
        String state = "ALL";
        BookingGetter getter = new BookingGetter()
                .setUserId(userId)
                .setState(state)
                .setType(BookingGetterType.BOOKER)
                .setSize(size)
                .setFrom(from);
        mvc.perform(MockMvcRequestBuilders.get("/bookings")
                        .header(Constants.X_SHARER_USER_ID, userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .param("state", state))
                .andExpect(status().isOk());
        verify(bookingService).getAllBookingsById(getter);
    }

    @SneakyThrows
    @Test
    void getOwnerBookings() {
        long userId = 1L;
        Integer from = 1;
        Integer size = 1;
        String state = "ALL";
        BookingGetter getter = new BookingGetter()
                .setUserId(userId)
                .setState(state)
                .setType(BookingGetterType.OWNER)
                .setSize(size)
                .setFrom(from);
        mvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .header(Constants.X_SHARER_USER_ID, userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .param("state", state))
                .andExpect(status().isOk());
        verify(bookingService).getAllBookingsById(getter);
    }
}