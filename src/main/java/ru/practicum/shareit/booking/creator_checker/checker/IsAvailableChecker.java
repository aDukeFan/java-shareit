package ru.practicum.shareit.booking.creator_checker.checker;

import ru.practicum.shareit.booking.creator_checker.model.Creator;
import ru.practicum.shareit.exception.BookingTimeException;

public class IsAvailableChecker extends CreatorChecker {

    public IsAvailableChecker(CreatorChecker next) {
        super(next);
    }

    @Override
    public void check(Creator request) {
        if (!request.isAvailable()) {
            throw new BookingTimeException("item is unavailable");
        }
    }
}
