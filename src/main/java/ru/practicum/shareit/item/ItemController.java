package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private ItemService service;

    @PostMapping
    public Item create(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody Item item) {
        return service.create(userId, item);
    }

    @PatchMapping("/{id}")
    public Item update(@PathVariable long id, @RequestHeader("X-Sharer-User-Id") long userId, @RequestBody Item item) {
        return service.update(id, userId, item);
    }

    @GetMapping("/{id}")
    public Item getItemById(@PathVariable long id) {
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
