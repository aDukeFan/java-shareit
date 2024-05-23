package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private ItemService service;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody ItemDto itemDto) {
        return service.create(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable long id, @RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemDto itemDto) {
        return service.update(id, userId, itemDto);
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable long id) {
        return service.getItemById(id);
    }

    @GetMapping
    public List<Item> getAllByOwner(@RequestHeader("X-Sharer-User-Id") long userId) {
        return service.getAllItemsByOwner(userId);
    }

    @GetMapping("/search")
    public List<Item> findByQuery(@RequestParam String text) {
        return service.findByQuery(text);
    }
}
