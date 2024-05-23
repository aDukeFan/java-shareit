package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemDtoMapper {

    public ItemDto toItemDto(Item item) {
        return new ItemDto()
                .setId(item.getId())
                .setName(item.getName())
                .setDescription(item.getDescription())
                .setAvailable(item.getAvailable())
                .setOwner(item.getOwner())
                .setRequest(item.getRequest());
    }

    public Item toItemFromDto(ItemDto itemDto) {
        return new Item()
                .setName(itemDto.getName())
                .setDescription(itemDto.getDescription())
                .setAvailable(itemDto.getAvailable())
                .setOwner(itemDto.getOwner())
                .setRequest(itemDto.getRequest());
    }
}
