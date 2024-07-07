package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDtoIncome;

import java.time.LocalDateTime;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {


    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    @Test
    void createBooking() {
        BookingDtoIncome income = new BookingDtoIncome()
                .setItemId(1L)
                .setStart(LocalDateTime.now().withNano(0))
                .setEnd(LocalDateTime.now().withNano(0).plusDays(1));

    }

    @Test
    void approveBooking() {
    }

    @Test
    void getBooking() {
    }

    @Test
    void getBookerBookings() {
    }

    @Test
    void getOwnerBookings() {
    }
}