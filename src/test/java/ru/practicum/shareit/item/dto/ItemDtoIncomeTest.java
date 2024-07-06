package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@JsonTest
public class ItemDtoIncomeTest {

    @Autowired
    private JacksonTester<ItemDtoIncome> tester;

    @Test
    public void itemDtoIncomeTest() throws IOException {
        ItemDtoIncome income = new ItemDtoIncome()
                .setAvailable(true)
                .setName("Name")
                .setDescription("some description")
                .setRequestId(1L);
        JsonContent<ItemDtoIncome> result = tester.write(income);
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.available");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.requestId");

    }

}