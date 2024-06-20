package ru.practicum.shareit.booking.creator_request.checker;

import lombok.Setter;
import ru.practicum.shareit.booking.creator_request.model.CreateRequest;

@Setter
public abstract class CreateRequestChecker {

    private CreateRequestChecker next;

    public void checkNext(CreateRequest request) {
        if (next != null) {
            next.check(request);
        }
    }

    public abstract void check(CreateRequest request);
}
