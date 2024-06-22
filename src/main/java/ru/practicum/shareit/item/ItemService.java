package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDtoIncome;
import ru.practicum.shareit.item.dto.CommentDtoOutcome;
import ru.practicum.shareit.item.dto.ItemDtoIncome;
import ru.practicum.shareit.item.dto.ItemDtoOutcomeLong;
import ru.practicum.shareit.item.dto.ItemDtoOutcomeWithAvailable;

import java.util.List;

public interface ItemService {

    ItemDtoOutcomeWithAvailable create(long userId, ItemDtoIncome itemDtoIncome);

    ItemDtoOutcomeWithAvailable update(long id, long userId, ItemDtoIncome itemDto);

    ItemDtoOutcomeLong getItemById(long itemId, long userId);

    List<ItemDtoOutcomeLong> getAllItemsByOwner(long userId);

    List<ItemDtoOutcomeWithAvailable> findByQuery(String text);

    CommentDtoOutcome addComment(long userId, long itemId, CommentDtoIncome commentDtoIncome);
}
