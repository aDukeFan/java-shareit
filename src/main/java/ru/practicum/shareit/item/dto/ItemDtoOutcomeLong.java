package ru.practicum.shareit.item.dto;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookerDtoForOwnerItem;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class ItemDtoOutcomeLong {

    long id;
    String name;
    String description;
    Boolean available;
    BookerDtoForOwnerItem lastBooking;
    BookerDtoForOwnerItem nextBooking;
    List<CommentDtoOutcome> comments = new ArrayList<>();
}
