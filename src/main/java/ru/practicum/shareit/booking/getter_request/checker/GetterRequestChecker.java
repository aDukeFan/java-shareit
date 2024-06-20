package ru.practicum.shareit.booking.getter_request.checker;

import lombok.Setter;
import ru.practicum.shareit.booking.getter_request.model.GetterRequest;

@Setter
public abstract class GetterRequestChecker {

    private GetterRequestChecker next;

    public void checkNext(GetterRequest request) {
        if (next != null) {
            next.check(request);
        }
    }

    public abstract void check(GetterRequest request);
}
