package ru.practicum.shareit.booking.creator_request.checker;

import ru.practicum.shareit.booking.creator_request.model.CreateRequest;
import ru.practicum.shareit.exception.BookingTimeException;

public class IsAvailableChecker extends CreateRequestChecker {

    public IsAvailableChecker(CreateRequestChecker next) {
        super(next);
    }

    @Override
    public void check(CreateRequest request) {
        if (!request.isAvailable()) {
            throw new BookingTimeException("item is unavailable");
        }
    }
}
