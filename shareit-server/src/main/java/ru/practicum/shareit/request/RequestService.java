package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.RequestDtoIncome;
import ru.practicum.shareit.request.dto.RequestDtoOutcome;
import ru.practicum.shareit.request.dto.RequestDtoWithItemList;

import java.util.List;

public interface RequestService {
    RequestDtoOutcome create(long bookerId, RequestDtoIncome income);

    List<RequestDtoWithItemList> getAllByRequesterId(long requesterId);

    RequestDtoWithItemList getByRequestId(long userId, long requestId);

    List<RequestDtoWithItemList> getAllWithParams(long userId, Integer from, Integer size);
}
