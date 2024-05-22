package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryItemRepository {

    private final List<Item> items = new ArrayList<>();
    private long nextId = 1;

    public Item save(Item item) {
        item.setId(nextId);
        items.add(item);
        nextId++;
        return item;
    }

    public Item update(Item item) {
        Item itemToUpdate = getById(item.getId());
        if (itemToUpdate != null) {
            if (item.getName() != null) {
                itemToUpdate.setName(item.getName());
            }
            if (item.getDescription() != null) {
                itemToUpdate.setDescription(item.getDescription());
            }
            if (item.getAvailable() != null) {
                itemToUpdate.setAvailable(item.getAvailable());
            }
        }
        return itemToUpdate;
    }

    public Item getById(long id) {
        return items.stream()
                .filter(item -> item.getId() == id)
                .findFirst().orElse(null);
    }

    public List<Item> getAllItemsByOwner(long userId) {
        return items.stream()
                .filter(item -> item.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    public List<Item> findByQuery(String text) {
        String target = text.toLowerCase();
        return items.stream().filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(target)
                        || item.getDescription().toLowerCase().contains(target))
                .collect(Collectors.toList());
    }
}
