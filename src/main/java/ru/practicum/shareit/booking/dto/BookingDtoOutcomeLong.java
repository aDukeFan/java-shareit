package ru.practicum.shareit.booking.dto;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDtoOutcomeShort;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
@Setter
@Getter
public class BookingDtoOutcomeLong {

    long id;
    UserDto booker;
    ItemDtoOutcomeShort item;
    LocalDateTime start;
    LocalDateTime end;
    BookingStatus status;
}
