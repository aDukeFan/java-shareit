package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoIncome;
import ru.practicum.shareit.booking.dto.BookingDtoOutcomeLong;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.BookingTimeException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.Constants;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private BookingRepository bookingRepository;
    private BookingMapper bookingMapper;


    @Override
    public BookingDtoOutcomeLong createBooking(long bookerId, BookingDtoIncome bookingDtoIncome) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_USER_WITH_SUCH_ID + bookerId));
        Long itemId = bookingDtoIncome.getItemId();
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_ITEM_WITH_SUCH_ID + itemId));
        if (bookingDtoIncome.getStart().isAfter(bookingDtoIncome.getEnd()) ||
                bookingDtoIncome.getStart().equals(bookingDtoIncome.getEnd())) {
            throw new BookingTimeException("Wrong booking time");
        }
        if (item.getOwner().getId() == bookerId) {
            throw new NotFoundException("Owner can't booking item");
        }
        if (!item.getAvailable()) {
            throw new BookingTimeException("item is unavailable");
        }
        Booking booking = bookingMapper.toSave(bookingDtoIncome)
                .setItem(item)
                .setBooker(booker);

        return bookingMapper.toSendLong(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoOutcomeLong approveBooking(long ownerId, long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("No booking with such ID " + bookingId));
        if (booking.getItem().getOwner().getId() != ownerId) {
            throw new NotFoundException(Constants.MESSAGE_BAD_OWNER_ID + ownerId);
        }
        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new BadRequestException("Booking is already APPROVED");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return bookingMapper.toSendLong(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoOutcomeLong getBooking(long ownerOrClientId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("No booking with such ID " + bookingId));
        if (booking.getBooker().getId() == ownerOrClientId
                || booking.getItem().getOwner().getId() == ownerOrClientId) {
            return bookingMapper.toSendLong(booking);
        } else {
            throw new NotFoundException("Bad ID: " + ownerOrClientId);
        }
    }

    @Override
    public List<BookingDtoOutcomeLong> getClientBookings(long bookerId, String state) {
        userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_USER_WITH_SUCH_ID + bookerId));
        List<BookingDtoOutcomeLong> allBookerBookings = bookingRepository
                .findAllByBookerIdOrderByStartDesc(bookerId).stream()
                .map(booking -> bookingMapper.toSendLong(booking))
                .collect(Collectors.toList());
        return makeBookingListByState(allBookerBookings, state);
    }

    @Override
    public List<BookingDtoOutcomeLong> getOwnerBookings(long ownerId, String state) {
        List<Long> itemsIdsOfOwner = itemRepository.findByOwnerId(ownerId).stream()
                .map(Item::getId)
                .collect(Collectors.toList());
        if (itemsIdsOfOwner.isEmpty()) {
            throw new NotFoundException("There are no items of user with ID: " + ownerId);
        }
        List<BookingDtoOutcomeLong> bookingsOfItemOwner = bookingRepository
                .findAllByItemIdInOrderByStartDesc(itemsIdsOfOwner).stream()
                .map(booking -> bookingMapper.toSendLong(booking)).collect(Collectors.toList());
        return makeBookingListByState(bookingsOfItemOwner, state);
    }

    private List<BookingDtoOutcomeLong> makeBookingListByState(List<BookingDtoOutcomeLong> list, String state) {
        switch (state) {
            case "ALL":
                return list;
            case "CURRENT":
                return list.stream()
                        .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                        .filter(booking -> booking.getEnd().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case "PAST":
                return list.stream()
                        .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case "FUTURE":
                return list.stream()
                        .filter(booking -> booking.getStatus() == BookingStatus.APPROVED
                                || booking.getStatus() == BookingStatus.WAITING)
                        .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case "WAITING":
                return list.stream()
                        .filter(booking -> booking.getStatus() == BookingStatus.WAITING)
                        .collect(Collectors.toList());
            case "REJECTED":
                return list.stream()
                        .filter(booking -> booking.getStatus() == BookingStatus.REJECTED)
                        .collect(Collectors.toList());
            default:
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
