package ru.practicum.shareit.request;


import org.mapstruct.Mapper;
import ru.practicum.shareit.request.dto.RequestDtoIncome;
import ru.practicum.shareit.request.dto.RequestDtoOutcome;
import ru.practicum.shareit.request.dto.RequestDtoWithItemList;
import ru.practicum.shareit.request.model.Request;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    Request toSave(RequestDtoIncome income);

    RequestDtoOutcome toSend(Request request);

    RequestDtoWithItemList toSendAll(Request request);
}
