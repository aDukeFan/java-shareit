package ru.practicum.shareit.request.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.model.ItemRequest;

@Component
public class ItemRequestDtoMapper {

    public ItemRequestDto toItemRequestDto(ItemRequest request) {
        return new ItemRequestDto()
                .setId(request.getId())
                .setDescription(request.getDescription())
                .setCreated(request.getCreated())
                .setRequestor(request.getRequestor());
    }

    public ItemRequest toItemRequestFromDto(ItemRequestDto itemRequestDto) {
        return new ItemRequest()
                .setDescription(itemRequestDto.getDescription())
                .setCreated(itemRequestDto.getCreated())
                .setRequestor(itemRequestDto.getRequestor());
    }
}
