package ru.practicum.shareit.booking.getter_request.checker;

import ru.practicum.shareit.booking.getter_request.model.GetterRequest;
import ru.practicum.shareit.booking.getter_request.model.GetterRequestState;
import ru.practicum.shareit.exception.BadRequestException;

import java.util.Arrays;

public class StateTypeChecker extends GetterRequestChecker {

    @Override
    public void check(GetterRequest request) {
        String state = request.getState();
        if (!Arrays.toString(GetterRequestState.values()).contains(state)) {
            throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
        checkNext(request);
    }
}
