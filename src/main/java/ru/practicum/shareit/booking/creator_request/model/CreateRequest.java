package ru.practicum.shareit.booking.creator_request.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
@Data
public class CreateRequest {

    long bookerId;
    long ownerId;
    LocalDateTime start;
    LocalDateTime end;
    boolean isAvailable;
}
