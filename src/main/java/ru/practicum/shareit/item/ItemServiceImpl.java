package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDtoIncome;
import ru.practicum.shareit.item.dto.CommentDtoOutcome;
import ru.practicum.shareit.item.dto.ItemDtoIncome;
import ru.practicum.shareit.item.dto.ItemDtoOutcomeLong;
import ru.practicum.shareit.item.dto.ItemDtoOutcomeAvailableRequest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.Constants;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private CommentRepository commentRepository;
    private RequestRepository requestRepository;
    private CommentMapper commentMapper;
    private BookingRepository bookingRepository;
    private ItemMapper itemMapper;
    private BookingMapper bookingMapper;


    @Override
    public ItemDtoOutcomeAvailableRequest create(long userId, ItemDtoIncome itemDtoIncome) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.MESSAGE_BAD_OWNER_ID + userId));
        Long requestId = itemDtoIncome.getRequestId();
        if (requestId != null) {
            requestRepository.findById(requestId)
                    .orElseThrow(() -> new NotFoundException("No request with ID " + requestId));
        }
        Item item = itemMapper.toSave(itemDtoIncome)
                .setOwner(owner);
        return itemMapper.toSend(itemRepository.save(item));
    }

    @Override
    public ItemDtoOutcomeAvailableRequest update(long id, long userId, ItemDtoIncome itemDto) {
        Item itemToUpdate = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.NO_ITEM_WITH_SUCH_ID + id));
        if (itemToUpdate.getOwner().getId() != userId) {
            throw new NotFoundException(Constants.MESSAGE_BAD_OWNER_ID + userId);
        }
        Item itemToSave = itemMapper.updateItemFromDto(itemDto, itemToUpdate).setId(id);
        return itemMapper.toSend(itemRepository.save(itemToSave));
    }

    @Override
    public List<ItemDtoOutcomeLong> getAllItemsByOwner(long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Item> ownerItems = itemRepository.findByOwnerId(userId, pageable).toList();
        List<ItemDtoOutcomeLong> result = new ArrayList<>();
        ownerItems.forEach(item -> result.add(getItemById(item.getId(), item.getOwner().getId())));
        result.sort(Comparator.comparing(ItemDtoOutcomeLong::getId));
        return result;
    }

    @Override
    public List<ItemDtoOutcomeAvailableRequest> findByQuery(String text, Integer from, Integer size) {
        if (text.isEmpty()) {
            return List.of();
        } else {
            Pageable pageable = PageRequest.of(from / size, size);
            return itemRepository
                    .findAllByAvailableTrueAndNameIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(text, text, pageable)
                    .stream()
                    .map(item -> itemMapper.toSend(item))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public ItemDtoOutcomeLong getItemById(long itemId, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_ITEM_WITH_SUCH_ID + itemId));
        ItemDtoOutcomeLong itemDtoOutcomeLong = itemMapper.toGetById(item);
        List<CommentDtoOutcome> comments = commentRepository.findAllByItemId(itemId).stream()
                .map(comment -> commentMapper.toSend(comment))
                .collect(Collectors.toList());
        itemDtoOutcomeLong.getComments().addAll(comments);
        if (item.getOwner().getId() == userId) {
            List<Booking> bookings = bookingRepository.findAllByItemId(itemId);
            Booking nextBooking = bookings.stream()
                    .sorted(Comparator.comparing(Booking::getStart))
                    .filter(booking -> booking.getStatus() != BookingStatus.REJECTED)
                    .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                    .findFirst().orElse(null);
            Booking lastBooking = bookings.stream()
                    .sorted(Comparator.comparing(Booking::getStart).reversed())
                    .filter(booking -> booking.getStatus() != BookingStatus.REJECTED)
                    .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                    .findFirst().orElse(null);
            itemDtoOutcomeLong
                    .setLastBooking(bookingMapper.toOwnerItemShow(lastBooking))
                    .setNextBooking(bookingMapper.toOwnerItemShow(nextBooking));
        }
        return itemDtoOutcomeLong;
    }

    @Override
    public CommentDtoOutcome addComment(long userId, long itemId, CommentDtoIncome commentDtoIncome) {
        List<Booking> bookings = bookingRepository
                .findAllByBookerIdAndItemId(userId, itemId).stream()
                .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                .filter(booking -> booking.getStatus() == BookingStatus.APPROVED
                        || booking.getStatus() == BookingStatus.CANCELED)
                .collect(Collectors.toList());
        if (bookings.isEmpty()) {
            throw new BadRequestException("No bookings");
        }
        Item itemToComment = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_ITEM_WITH_SUCH_ID + itemId));
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.MESSAGE_BAD_OWNER_ID + userId));

        Comment comment = commentMapper.toSave(commentDtoIncome)
                .setAuthor(booker)
                .setItem(itemToComment);
        return commentMapper.toSend(commentRepository.save(comment));
    }
}
