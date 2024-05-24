package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.InMemoryUserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private InMemoryItemRepository itemRepository;
    private InMemoryUserRepository userRepository;
    private ItemDtoMapper itemDtoMapper;


    @Override
    public ItemDto create(long userId, ItemDto itemDto) {
        if (userRepository.getById(userId) == null) {
            throw new NotFoundException("no owner with such ID " + userId);
        }
        Item itemToSave = itemDtoMapper.toItemFromDto(itemDto)
                .setOwner(userRepository.getById(userId));
        return itemDtoMapper.toItemDto(itemRepository.save(itemToSave));
    }

    @Override
    public ItemDto update(long id, long userId, ItemDto itemDto) {
        if (userRepository.getById(userId) == null) {
            throw new NotFoundException("no owner with such ID " + userId);
        }
        if (itemRepository.getById(id).getOwner().getId() != userId) {
            throw new NotFoundException("Item has another owner");
        }
        Item itemToUpdate = itemDtoMapper.toItemFromDto(itemDto).setId(id);
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
