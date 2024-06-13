package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoToSend;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.Constants;

import java.time.LocalDateTime;
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
    private CommentDtoMapper commentDtoMapper;
    private CommentMapper commentMapper;
    private BookingRepository bookingRepository;
    private ItemMapper itemMapper;
    private BookingMapper bookingMapper;


    @Override
    public ItemDto create(long userId, ItemDto itemDto) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.MESSAGE_BAD_OWNER_ID + userId));
        Item itemToSave = itemMapper.toItem(itemDto);
        itemToSave.setOwner(owner);
        return itemMapper.toDto(itemRepository.save(itemToSave));
    }

    @Override
    public ItemDto update(long id, long userId, ItemDto itemDto) {
        Item itemToUpdate = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.NO_ITEM_WITH_SUCH_ID + id));
        if (itemToUpdate.getOwner().getId() != userId) {
            throw new NotFoundException(Constants.MESSAGE_BAD_OWNER_ID + userId);
        }
        itemDto.setId(id);
        Item itemToSave = itemMapper.updateItemFromDto(itemDto, itemToUpdate);// itemDtoMapper.toItemFromDtoForUpdate(itemDto, itemToUpdate);
        return itemMapper.toDto(itemRepository.save(itemToSave));
    }

    //Добавление дат бронирования при просмотре вещей
    // Осталась пара штрихов.
    // Итак, вы добавили возможность бронировать вещи.
    // Теперь нужно, чтобы владелец видел даты последнего
    // и ближайшего следующего бронирования для каждой вещи,
    // когда просматривает список (GET /items).

    @Override
    public List<ItemDto> getAllItemsByOwner(long userId) {
        List<Item> ownerItems = itemRepository.findByOwnerId(userId);
        return ownerItems.stream()
                .map(this::setBookingsAndCommentsToDto)
                .sorted(Comparator.comparing(ItemDto::getId))
                .collect(Collectors.toList());
    }

    private ItemDto setBookingsAndCommentsToDto(Item item) {
        long itemId = item.getId();
        List<Booking> allItemBookings = bookingRepository.findAll().stream()
                .filter(booking -> booking.getItem().getId() == itemId).collect(Collectors.toList());
        Booking nextBooking = allItemBookings.stream()
                .sorted(Comparator.comparing(Booking::getStart))
                .filter(booking -> booking.getStart().isAfter(LocalDateTime.now())).findFirst().orElse(null);
        Booking lastBooking = allItemBookings.stream()
                .sorted(Comparator.comparing(Booking::getEnd).reversed())
                .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now())).findFirst().orElse(null);
        ItemDto itemDto = itemMapper.toDto(item);
        List<CommentDto> itemCommentDtos = commentRepository.findAllByItemId(itemId).stream()
                .map(comment -> commentMapper.toDto(comment).setAuthorName(comment.getAuthor().getName()))
                .collect(Collectors.toList());
        BookingDtoToSend last = bookingMapper.toSend(lastBooking);
        BookingDtoToSend next = bookingMapper.toSend(nextBooking);
        if (nextBooking != null) {
            next.setBookerId(nextBooking.getBooker().getId());
        }
        if (lastBooking != null) {
            last.setBookerId(lastBooking.getBooker().getId());
        }
        itemDto.setLastBooking(last);
        itemDto.setNextBooking(next);
        itemDto.getComments().addAll(itemCommentDtos);
        return itemDto;
    }

    @Override
    public List<ItemDto> findByQuery(String text) {
        if (text.isEmpty()) {
            return List.of();
        } else {
            return itemRepository.findAllByNameOrDescriptionContainingIgnoreCase(text, text)
                    .stream().filter(Item::getAvailable).map(itemMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public ItemDto getItemById(long itemId, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_ITEM_WITH_SUCH_ID + itemId));
        if (item.getOwner().getId() == userId) {
            ItemDto itemDto = itemMapper.toDto(item);
            List<Comment> comments = commentRepository.findAllByItemId(itemId);
            List<CommentDto> commentDtos = comments.stream()
                    .map(comment -> commentMapper.toDto(comment).setAuthorName(comment.getAuthor().getName()))
                    .collect(Collectors.toList());
            itemDto.getComments().addAll(commentDtos);
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
            BookingDtoToSend last = bookingMapper.toSend(lastBooking);
            BookingDtoToSend next = bookingMapper.toSend(nextBooking);
            if (nextBooking != null) {
                next.setBookerId(nextBooking.getBooker().getId());
            }
            if (lastBooking != null) {
                last.setBookerId(lastBooking.getBooker().getId());
            }
            itemDto.setLastBooking(last);
            itemDto.setNextBooking(next);
            return itemDto;
        } else {
            ItemDto itemDto = itemMapper.toDto(item);
            List<Comment> comments = commentRepository.findAllByItemId(itemId);
            List<CommentDto> commentDtos = comments.stream()
                    .map(comment -> commentMapper.toDto(comment).setAuthorName(comment.getAuthor().getName()))
                    .collect(Collectors.toList());
            itemDto.getComments().addAll(commentDtos);
            return itemDto;
        }
    }

    @Override
    public CommentDto addComment(long userId, long itemId, CommentDto commentDto) {
        List<Booking> bookings = bookingRepository.findAllByItemId(itemId).stream()
                .filter(booking -> booking.getBooker().getId() == userId)
                .filter(booking -> booking.getStatus() == BookingStatus.APPROVED || booking.getStatus() == BookingStatus.CANCELED)
                .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());

//        if (bookingRepository.findByItemIdAndBookerId(itemId, userId).isEmpty()) {
//            throw new BadRequestException("No bookings");
//        }

        if (bookings.isEmpty()) {
            throw new BadRequestException("No bookings");
        }
        Comment comment = commentDtoMapper.toCommentFromDto(commentDto);
        Item itemToComment = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_ITEM_WITH_SUCH_ID + itemId));
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.MESSAGE_BAD_OWNER_ID + userId));
        comment
                .setAuthor(booker)
                .setItem(itemToComment);
        commentRepository.save(comment);
        CommentDto commentDto1 = new CommentDto()
                .setId(comment.getId())
                .setText(comment.getText())
                .setAuthorName(comment.getAuthor().getName());
        return commentDto1;

    }

    // Комментарий можно добавить по эндпоинту POST /items/{itemId}/comment, создайте в контроллере метод для него.
    // Реализуйте логику по добавлению нового комментария к вещи в сервисе ItemServiceImpl.
    // Для этого также понадобится создать интерфейс CommentRepository.
    // Не забудьте добавить проверку, что пользователь, который пишет комментарий, действительно брал вещь в аренду.
    // Осталось разрешить пользователям просматривать комментарии других пользователей.
    // Отзывы можно будет увидеть по двум эндпоинтам — по GET /items/{itemId}
    // для одной конкретной вещи и по GET /items для всех вещей данного пользователя.

}
