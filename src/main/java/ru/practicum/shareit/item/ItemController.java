package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.util.Constants;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private ItemService service;

    @PostMapping
    public ItemDto create(@RequestHeader(Constants.X_SHARER_USER_ID) long userId,
                          @Valid @RequestBody ItemDto itemDto) {
        return service.create(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable long id,
                          @RequestHeader(Constants.X_SHARER_USER_ID) long userId,
                          @RequestBody ItemDto itemDto) {
        return service.update(id, userId, itemDto);
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable long id,
                               @RequestHeader(Constants.X_SHARER_USER_ID) long userId) {
        return service.getItemById(id, userId);
    }

    @GetMapping
    public List<ItemDto> getAllByOwner(@RequestHeader(Constants.X_SHARER_USER_ID) long userId) {
        return service.getAllItemsByOwner(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> findByQuery(@RequestParam String text) {
        return service.findByQuery(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(Constants.X_SHARER_USER_ID) long userId,
                              @PathVariable long itemId,
                              @Valid @RequestBody CommentDto commentDto) {
        return service.addComment(userId, itemId, commentDto);
    }
}
