package ru.practicum.shareit.item.dto;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Objects;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class CommentDtoOutcome {

    long id;
    String authorName;
    String text;
    LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentDtoOutcome outcome = (CommentDtoOutcome) o;
        return id == outcome.id
                && Objects.equals(authorName, outcome.authorName)
                && Objects.equals(text, outcome.text)
                && Objects.equals(created, outcome.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authorName, text, created);
    }
}
