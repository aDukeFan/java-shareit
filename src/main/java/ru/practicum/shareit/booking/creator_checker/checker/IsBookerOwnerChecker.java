package ru.practicum.shareit.booking.creator_checker.checker;

import ru.practicum.shareit.booking.creator_checker.model.Creator;
import ru.practicum.shareit.exception.NotFoundException;

public class IsBookerOwnerChecker extends CreatorChecker {

    public IsBookerOwnerChecker(CreatorChecker next) {
        super(next);
    }

    @Override
    public void check(Creator request) {
        if (request.getBookerId() == request.getOwnerId()) {
            throw new NotFoundException("Owner can't booking item");
        }
        checkNext(request);
    }
}
