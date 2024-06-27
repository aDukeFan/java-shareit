package ru.practicum.shareit.booking.creator_request.checker;

import ru.practicum.shareit.booking.creator_request.model.CreateRequest;
import ru.practicum.shareit.exception.NotFoundException;

public class IsBookerOwnerChecker extends CreateRequestChecker {

    public IsBookerOwnerChecker(CreateRequestChecker next) {
        super(next);
    }

    @Override
    public void check(CreateRequest request) {
        if (request.getBookerId() == request.getOwnerId()) {
            throw new NotFoundException("Owner can't booking item");
        }
        checkNext(request);
    }
}
