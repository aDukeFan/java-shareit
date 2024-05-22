package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item create(long userId, Item item);

    Item update(long id, long userId, Item item);

    Item getItemById(long itemId);

    List<Item> getAllItemsByOwner(long userId);

    List<Item> findByQuery(String text);
}
