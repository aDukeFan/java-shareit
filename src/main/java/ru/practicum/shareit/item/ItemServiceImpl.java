package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.Constants;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private ItemDtoMapper itemDtoMapper;


    @Override
    public ItemDto create(long userId, ItemDto itemDto) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.MESSAGE_BAD_OWNER_ID + userId));
        Item itemToSave = itemDtoMapper.toItemFromDto(itemDto)
                .setOwner(owner);
        return itemDtoMapper.toItemDto(itemRepository.save(itemToSave));
    }

    @Override
    public ItemDto update(long id, long userId, ItemDto itemDto) {
        Item itemToUpdate = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.NO_ITEM_WITH_SUCH_ID + id));
        if (itemToUpdate.getOwner().getId() != userId) {
            throw new NotFoundException(Constants.MESSAGE_BAD_OWNER_ID + userId);
        }
        Item itemToSave = itemDtoMapper.toItemFromDtoForUpdate(itemDto, itemToUpdate);
        return itemDtoMapper.toItemDto(itemRepository.save(itemToSave));
    }

    //Добавление дат бронирования при просмотре вещей
    // Осталась пара штрихов.
    // Итак, вы добавили возможность бронировать вещи.
    // Теперь нужно, чтобы владелец видел даты последнего
    // и ближайшего следующего бронирования для каждой вещи,
    // когда просматривает список (GET /items).

    @Override
    public List<ItemDto> getAllItemsByOwner(long userId) {
        return itemRepository.findAll().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .map(item -> itemDtoMapper.toItemDto(item))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findByQuery(String text) {
        if (text.isEmpty()) {
            return List.of();
        } else {
            return itemRepository.findAll().stream()
                    .filter(Item::getAvailable)
                    .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                    .map(item -> itemDtoMapper.toItemDto(item))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public ItemDto getItemById(long itemId) {
        return itemDtoMapper.toItemDto(itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_ITEM_WITH_SUCH_ID + itemId)));
    }

    @Override
    public void addComment(long userId, long itemId, CommentDto commentDto) {

    }

    // Комментарий можно добавить по эндпоинту POST /items/{itemId}/comment, создайте в контроллере метод для него.
    // Реализуйте логику по добавлению нового комментария к вещи в сервисе ItemServiceImpl.
    // Для этого также понадобится создать интерфейс CommentRepository.
    // Не забудьте добавить проверку, что пользователь, который пишет комментарий, действительно брал вещь в аренду.
    // Осталось разрешить пользователям просматривать комментарии других пользователей.
    // Отзывы можно будет увидеть по двум эндпоинтам — по GET /items/{itemId}
    // для одной конкретной вещи и по GET /items для всех вещей данного пользователя.

}
