package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class ItemRequest {
    long id;
    String description;
    User requestor;
    LocalDateTime created;

}
