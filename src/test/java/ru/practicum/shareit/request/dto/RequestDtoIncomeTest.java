package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class RequestDtoIncomeTest {

    @Autowired
    private JacksonTester<RequestDtoIncome> tester;

    @Test
    public void requestDtoIncomeTest() throws IOException {
        RequestDtoIncome income = new RequestDtoIncome()
                .setCreated(LocalDateTime.now())
                .setDescription("some needs");
        JsonContent<RequestDtoIncome> result = tester.write(income);
        assertThat(result).hasJsonPath("$.created");
        assertThat(result).hasJsonPath("$.description");
    }

}