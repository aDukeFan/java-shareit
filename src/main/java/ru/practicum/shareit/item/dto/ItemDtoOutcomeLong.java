package ru.practicum.shareit.item.dto;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookerDtoForOwnerItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class ItemDtoOutcomeLong {

    long id;
    String name;
    String description;
    Boolean available;
    BookerDtoForOwnerItem lastBooking;
    BookerDtoForOwnerItem nextBooking;
    List<CommentDtoOutcome> comments = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDtoOutcomeLong that = (ItemDtoOutcomeLong) o;
        return id == that.id
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(available, that.available)
                && Objects.equals(lastBooking, that.lastBooking)
                && Objects.equals(nextBooking, that.nextBooking)
                && Objects.equals(comments, that.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, available, lastBooking, nextBooking, comments);
    }
}
