package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
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
    private CommentMapper commentMapper;
    private BookingRepository bookingRepository;
    private ItemMapper itemMapper;
    private BookingMapper bookingMapper;


    @Override
    public ItemDtoOutcomeWithAvailable create(long userId, ItemDtoIncome itemDtoIncome) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.MESSAGE_BAD_OWNER_ID + userId));
        Item item = itemMapper.toSave(itemDtoIncome)
                .setOwner(owner);
        return itemMapper.toSendAfterSave(itemRepository.save(item));
    }

    @Override
    public ItemDtoOutcomeWithAvailable update(long id, long userId, ItemDtoIncome itemDto) {
        Item itemToUpdate = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.NO_ITEM_WITH_SUCH_ID + id));
        if (itemToUpdate.getOwner().getId() != userId) {
            throw new NotFoundException(Constants.MESSAGE_BAD_OWNER_ID + userId);
        }
        Item itemToSave = itemMapper.updateItemFromDto(itemDto, itemToUpdate).setId(id);
        return itemMapper.toSendAfterSave(itemRepository.save(itemToSave));
    }

    @Override
    public List<ItemDtoOutcomeLong> getAllItemsByOwner(long userId) {
        List<Item> ownerItems = itemRepository.findByOwnerId(userId);
        List<ItemDtoOutcomeLong> result = new ArrayList<>();
        ownerItems.forEach(item -> result.add(getItemById(item.getId(), item.getOwner().getId())));
        result.sort(Comparator.comparing(ItemDtoOutcomeLong::getId));
        return result;
    }

    @Override
    public List<ItemDtoOutcomeWithAvailable> findByQuery(String text) {
        if (text.isEmpty()) {
            return List.of();
        } else {
            return itemRepository
                    .findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text)
                    .stream()
                    .map(item -> itemMapper.toSendAfterSave(item))
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
                    .sorted(Comparator.comparing(Booking::getEnd).reversed())
                    .filter(booking -> booking.getStatus() != BookingStatus.REJECTED)
                    .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
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
