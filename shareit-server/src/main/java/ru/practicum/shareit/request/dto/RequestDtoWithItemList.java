package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.dto.ItemDtoOutcomeAvailableRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode
public class RequestDtoWithItemList {
    long id;
    String description;
    LocalDateTime created;
    List<ItemDtoOutcomeAvailableRequest> items = new ArrayList<>();
}
