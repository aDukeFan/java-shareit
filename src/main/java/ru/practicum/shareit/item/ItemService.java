package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDtoIncome;
import ru.practicum.shareit.item.dto.CommentDtoOutcome;
import ru.practicum.shareit.item.dto.ItemDtoIncome;
import ru.practicum.shareit.item.dto.ItemDtoOutcomeLong;
import ru.practicum.shareit.item.dto.ItemDtoOutcomeAvailableRequest;

import java.util.List;

public interface ItemService {

    ItemDtoOutcomeAvailableRequest create(long userId, ItemDtoIncome itemDtoIncome);

    ItemDtoOutcomeAvailableRequest update(long id, long userId, ItemDtoIncome itemDto);

    ItemDtoOutcomeLong getItemById(long itemId, long userId);

    List<ItemDtoOutcomeLong> getAllItemsByOwner(long userId, Integer from, Integer size);

    List<ItemDtoOutcomeAvailableRequest> findByQuery(String text, Integer from, Integer size);

    CommentDtoOutcome addComment(long userId, long itemId, CommentDtoIncome commentDtoIncome);
}
