package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
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
public class BookingDtoToSend {
    @NotNull
    long id;
    @NotNull
    long bookerId;
    @NotNull
    @FutureOrPresent
    LocalDateTime start;
    @NotNull
    LocalDateTime end;

}
