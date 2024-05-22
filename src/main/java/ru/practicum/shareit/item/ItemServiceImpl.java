package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.InMemoryUserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private InMemoryItemRepository repository;
    private InMemoryUserRepository userRepository;

    @Override
    public Item create(long userId, Item item) {
        if (userRepository.getById(userId) == null) {
            throw new NotFoundException("no owner with such ID " + userId);
        }
        item.setOwner(userRepository.getById(userId));
        return repository.save(item);
    }

    @Override
    public Item update(long id, long userId, Item item) {
        if (userRepository.getById(userId) == null) {
            throw new NotFoundException("no owner with such ID " + userId);
        }
        if (repository.getById(id).getOwner().getId() != userId) {
            throw new NotFoundException("Item has another owner");
        }
        item.setId(id);
        return repository.update(item);
    }

    @Override
    public List<Item> getAllItemsByOwner(long userId) {
        return repository.getAllItemsByOwner(userId);
    }

    @Override
    public List<Item> findByQuery(String text) {
        if (text.isEmpty()) {
            return List.of();
        } else {
            return repository.findByQuery(text);
        }
    }

    @Override
    public Item getItemById(long itemId) {
        return repository.getById(itemId);
    }


}
