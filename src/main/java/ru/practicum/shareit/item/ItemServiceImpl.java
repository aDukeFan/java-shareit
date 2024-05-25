package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.InMemoryUserRepository;
import ru.practicum.shareit.util.Constants;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private InMemoryItemRepository itemRepository;
    private InMemoryUserRepository userRepository;
    private ItemDtoMapper itemDtoMapper;


    @Override
    public ItemDto create(long userId, ItemDto itemDto) {
        if (userRepository.getById(userId) == null) {
            log.info("Try to save or item with wrong owner id: {}", userId);
            throw new NotFoundException(Constants.MESSAGE_BAD_OWNER_ID + userId);
        }
        Item itemToSave = itemDtoMapper.toItemFromDto(itemDto)
                .setOwner(userRepository.getById(userId));
        return itemDtoMapper.toItemDto(itemRepository.save(itemToSave));
    }

    @Override
    public ItemDto update(long id, long userId, ItemDto itemDto) {
        if (itemRepository.getById(id).getOwner().getId() != userId) {
            throw new NotFoundException(Constants.MESSAGE_BAD_OWNER_ID + userId);
        }
        Item item = itemRepository.getById(id);
        Item itemToUpdate = itemDtoMapper.toItemFromDtoForUpdate(itemDto, item);
        return itemDtoMapper.toItemDto(itemRepository.update(itemToUpdate));
    }

    @Override
    public List<ItemDto> getAllItemsByOwner(long userId) {
        return itemRepository.getAllItemsByOwner(userId).stream()
                .map(item -> itemDtoMapper.toItemDto(item))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findByQuery(String text) {
        if (text.isEmpty()) {
            return List.of();
        } else {
            return itemRepository.findByQuery(text).stream()
                    .map(item -> itemDtoMapper.toItemDto(item))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public ItemDto getItemById(long itemId) {
        return itemDtoMapper.toItemDto(itemRepository.getById(itemId));
    }

}
