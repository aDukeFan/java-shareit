package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class ItemDto {
    long id;
    @NotBlank
    @NotNull
    String name;
    @NotBlank
    @NotNull
    String description;
    @NotNull
    Boolean available;
    User owner;
    String request;
}
