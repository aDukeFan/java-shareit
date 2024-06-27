package ru.practicum.shareit.booking.creator_request.checker;

import lombok.AllArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.creator_request.model.CreateRequest;

@Setter
@AllArgsConstructor
public abstract class CreateRequestChecker {

    private final CreateRequestChecker next;

    public void checkNext(CreateRequest request) {
        if (next != null) {
            next.check(request);
        }
    }

    public abstract void check(CreateRequest request);
}
