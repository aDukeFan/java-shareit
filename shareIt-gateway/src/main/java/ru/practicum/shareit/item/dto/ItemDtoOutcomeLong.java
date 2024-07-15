package ru.practicum.shareit.item.dto;


import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookerDtoForOwnerItem;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
@EqualsAndHashCode
public class ItemDtoOutcomeLong {

    long id;
    String name;
    String description;
    Boolean available;
    BookerDtoForOwnerItem lastBooking;
    BookerDtoForOwnerItem nextBooking;
    List<CommentDtoOutcome> comments = new ArrayList<>();
}
