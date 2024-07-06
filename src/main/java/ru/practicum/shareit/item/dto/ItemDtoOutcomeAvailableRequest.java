package ru.practicum.shareit.item.dto;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class ItemDtoOutcomeAvailableRequest {

    long id;
    String name;
    String description;
    Boolean available;
    Long requestId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDtoOutcomeAvailableRequest that = (ItemDtoOutcomeAvailableRequest) o;
        return id == that.id
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(available, that.available)
                && Objects.equals(requestId, that.requestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, available, requestId);
    }
}
