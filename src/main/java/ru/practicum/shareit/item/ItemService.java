package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto create(long userId, ItemDto itemDto);

    ItemDto update(long id, long userId, ItemDto itemDto);

    ItemDto getItemById(long itemId);

    List<ItemDto> getAllItemsByOwner(long userId);

    List<ItemDto> findByQuery(String text);
}
