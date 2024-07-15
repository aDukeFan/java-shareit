package ru.practicum.shareit.booking.dto;


import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.validavor.ValidBookingDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
@Setter
@Getter
@EqualsAndHashCode
@ValidBookingDate(message = "Bad booking time!")
public class BookingDtoIncome {

    @NotNull
    long itemId;
    @NotNull
    @FutureOrPresent
    LocalDateTime start;
    @NotNull
    @Future
    LocalDateTime end;
}
