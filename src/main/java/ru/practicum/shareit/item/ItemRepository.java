package ru.practicum.shareit.item;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerId(long ownerId, Pageable pageable);

    List<Item> findAllByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContainingAndAvailableTrue(String name,
                                                                                                  String description,
                                                                                                  Pageable pageable);

    List<Item> findByRequestId(long requestId);
}
