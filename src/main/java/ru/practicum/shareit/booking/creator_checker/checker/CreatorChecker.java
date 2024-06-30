package ru.practicum.shareit.booking.creator_checker.checker;

import lombok.AllArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.creator_checker.model.Creator;

@Setter
@AllArgsConstructor
public abstract class CreatorChecker {

    private final CreatorChecker next;

    public void checkNext(Creator request) {
        if (next != null) {
            next.check(request);
        }
    }

    public abstract void check(Creator request);
}
