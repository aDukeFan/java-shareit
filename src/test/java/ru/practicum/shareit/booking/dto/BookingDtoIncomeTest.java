package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingDtoIncomeTest {

    @Autowired
    private JacksonTester<BookingDtoIncome> tester;

    @Test
    public void userDtoTest() throws IOException {
        BookingDtoIncome bookingDtoIncome = new BookingDtoIncome()
                .setStart(LocalDateTime.now())
                .setEnd(LocalDateTime.now().plusDays(5))
                .setItemId(1);
        JsonContent<BookingDtoIncome> result = tester.write(bookingDtoIncome);
        assertThat(result).hasJsonPath("$.start");
        assertThat(result).hasJsonPath("$.end");
        assertThat(result).hasJsonPath("$.itemId");

    }

}