package ru.practicum.shareit.item.dto;


import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
@EqualsAndHashCode
public class ItemDtoOutcomeAvailableRequest {

    long id;
    String name;
    String description;
    Boolean available;
    Long requestId;
}
