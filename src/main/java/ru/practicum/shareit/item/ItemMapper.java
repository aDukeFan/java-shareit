package ru.practicum.shareit.item;

import org.mapstruct.*;
import ru.practicum.shareit.item.dto.ItemDtoIncome;
import ru.practicum.shareit.item.dto.ItemDtoOutcomeWithAvailable;
import ru.practicum.shareit.item.dto.ItemDtoOutcomeLong;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item toSave(ItemDtoIncome itemDtoIncome);

    ItemDtoOutcomeWithAvailable toSendAfterSave(Item item);

    ItemDtoOutcomeLong toGetById(Item item);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    Item updateItemFromDto(ItemDtoIncome itemDto, @MappingTarget Item item);
}
