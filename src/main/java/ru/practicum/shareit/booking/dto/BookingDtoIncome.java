package ru.practicum.shareit.booking.dto;


import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
@Setter
@Getter
@EqualsAndHashCode
public class BookingDtoIncome {

    @NotNull
    long itemId;
    @NotNull
    @FutureOrPresent
    LocalDateTime start;
    @NotNull
    LocalDateTime end;
}
