package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto create(long userId, ItemDto itemDto);

    ItemDto update(long id, long userId, ItemDto itemDto);

    ItemDto getItemById(long itemId);

    List<Item> getAllItemsByOwner(long userId);

    List<Item> findByQuery(String text);
}
