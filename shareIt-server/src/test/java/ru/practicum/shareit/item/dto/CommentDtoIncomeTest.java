package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class CommentDtoIncomeTest {

    @Autowired
    private JacksonTester<CommentDtoIncome> tester;

    @Test
    public void commentDtoIncomeTest() throws IOException {
        CommentDtoIncome income = new CommentDtoIncome()
                .setCreatedTime(LocalDateTime.now())
                .setText("some comment");
        JsonContent<CommentDtoIncome> result = tester.write(income);
        assertThat(result).hasJsonPath("$.createdTime");
        assertThat(result).hasJsonPath("$.text");
    }

}