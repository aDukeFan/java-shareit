package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private long nextId = 1;

    public Item save(Item item) {
        item.setId(nextId);
        items.put(nextId, item);
        nextId++;
        return item;
    }

    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    public Item getById(long id) {
        return items.get(id);
    }

    public List<Item> getAllItemsByOwner(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    public List<Item> findByQuery(String text) {
        String target = text.toLowerCase();
        return items.values().stream().filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(target)
                        || item.getDescription().toLowerCase().contains(target))
                .collect(Collectors.toList());
    }
}
