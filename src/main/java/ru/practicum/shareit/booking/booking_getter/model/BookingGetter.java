package ru.practicum.shareit.booking.booking_getter.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
@Data
public class BookingGetter {
    long userId;
    BookingGetterType type;
    String state;
    Integer from;
    Integer size;
}
