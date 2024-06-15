package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.util.Constants;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private ItemService service;

    @PostMapping
    public ItemDtoOutcomeWithAvailable create(@RequestHeader(Constants.X_SHARER_USER_ID) long userId,
                                              @Valid @RequestBody ItemDtoIncome itemDtoIncome) {
        return service.create(userId, itemDtoIncome);
    }

    @PatchMapping("/{id}")
    public ItemDtoOutcomeWithAvailable update(@PathVariable long id,
                                              @RequestHeader(Constants.X_SHARER_USER_ID) long userId,
                                              @RequestBody ItemDtoIncome itemDto) {
        return service.update(id, userId, itemDto);
    }

    @GetMapping("/{id}")
    public ItemDtoOutcomeLong getItemById(@PathVariable long id,
                               @RequestHeader(Constants.X_SHARER_USER_ID) long userId) {
        return service.getItemById(id, userId);
    }

    @GetMapping
    public List<ItemDtoOutcomeLong> getAllByOwner(@RequestHeader(Constants.X_SHARER_USER_ID) long userId) {
        return service.getAllItemsByOwner(userId);
    }

    @GetMapping("/search")
    public List<ItemDtoOutcomeWithAvailable> findByQuery(@RequestParam String text) {
        return service.findByQuery(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOutcome addComment(@RequestHeader(Constants.X_SHARER_USER_ID) long userId,
                                        @PathVariable long itemId,
                                        @Valid @RequestBody CommentDtoIncome commentDtoIncome) {
        return service.addComment(userId, itemId, commentDtoIncome);
    }
}
