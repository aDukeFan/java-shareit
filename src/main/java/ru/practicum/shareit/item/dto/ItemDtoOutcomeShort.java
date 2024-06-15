package ru.practicum.shareit.item.dto;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class ItemDtoOutcomeShort {

    long id;
    String name;
    String description;
}
