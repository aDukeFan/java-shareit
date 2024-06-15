package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {

    ItemDtoOutcomeWithAvailable create(long userId, ItemDtoIncome itemDtoIncome);

    ItemDtoOutcomeWithAvailable update(long id, long userId, ItemDtoIncome itemDto);

    ItemDtoOutcomeLong getItemById(long itemId, long userId);

    List<ItemDtoOutcomeLong> getAllItemsByOwner(long userId);

    List<ItemDtoOutcomeWithAvailable> findByQuery(String text);

    CommentDtoOutcome addComment(long userId, long itemId, CommentDtoIncome commentDtoIncome);
}
