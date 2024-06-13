package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private BookingRepository bookingRepository;


    @Override
    public Booking createBookingRequest(long clientId, BookingDto bookingDto) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_USER_WITH_SUCH_ID + clientId));
        Long itemId = bookingDto.getItemId();
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_ITEM_WITH_SUCH_ID + itemId));
        if (bookingDto.getStart().isAfter(bookingDto.getEnd()) ||
        bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new BookingTimeException("Wrong Booking time!");
        }
        if (item.getOwner().getId() == clientId) {
            throw new NotFoundException("Owner cant booking item");
        }
        if (!item.getAvailable()) {
            throw new BookingTimeException("item is unavailable");
        }

        Booking booking = new Booking()
                .setBooker(client)
                .setItem(item)
                .setStart(bookingDto.getStart())
                .setEnd(bookingDto.getEnd())
                .setStatus(BookingStatus.WAITING);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking bookingConfirmation(long ownerId, long bookingId, Boolean approved) {
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
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBookingStatus(long ownerOrClientId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("No booking with such ID " + bookingId));
        if (booking.getBooker().getId() == ownerOrClientId
                || booking.getItem().getOwner().getId() == ownerOrClientId) {
            return booking;
        } else {
            throw new NotFoundException("No way, man");
        }

    }

    @Override
    public List<Booking> getClientBookings(long bookerId, String state) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_USER_WITH_SUCH_ID + bookerId));
        List<Booking> allBookerBookings = bookingRepository.findAllByBookerId(bookerId);
        switch (state) {
            case "ALL":
                return allBookerBookings.stream()
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .collect(Collectors.toList());
            case "CURRENT":
                return allBookerBookings.stream()
                        .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                        .filter(booking -> booking.getEnd().isAfter(LocalDateTime.now()))
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .collect(Collectors.toList());
            case "PAST":
                return allBookerBookings.stream()
                        .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .collect(Collectors.toList());
            case "FUTURE":
                return allBookerBookings.stream()
                        .filter(booking -> booking.getStatus() == BookingStatus.APPROVED  || booking.getStatus() == BookingStatus.WAITING)
                        .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .collect(Collectors.toList());
            case "WAITING":
                return allBookerBookings.stream()
                        .filter(booking -> booking.getStatus() == BookingStatus.WAITING)
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .collect(Collectors.toList());
            case "REJECTED":
                return allBookerBookings.stream()
                        .filter(booking -> booking.getStatus() == BookingStatus.REJECTED)
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .collect(Collectors.toList());
            default:
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
        //        // Получение списка всех бронирований текущего пользователя.
        //        // Эндпоинт — GET /bookings?state={state}.
        //        // Параметр state необязательный и по умолчанию равен ALL (англ. «все»).
        //        // Также он может принимать значения CURRENT (англ. «текущие»),
        //        // **PAST** (англ. «завершённые»), FUTURE (англ. «будущие»),
        //        // WAITING (англ. «ожидающие подтверждения»),
        //        // REJECTED (англ. «отклонённые»).
        //        // Бронирования должны возвращаться отсортированными по дате от более новых к более старым.
    }

    @Override
    public List<Booking> getOwnerBookings(long ownerId, String state) {
        // Этот запрос имеет смысл для владельца хотя бы одной вещи.
        List<Item> userItems = itemRepository.findAll().stream()
                .filter(item -> item.getOwner().getId() == ownerId)
                .collect(Collectors.toList());
        if (userItems.isEmpty()) {
            throw new NotFoundException("There is no item of user with such ID: " + ownerId);
        }
        switch (state) {
            case "ALL":
                return bookingRepository.findAll().stream()
                        .filter(booking -> booking.getItem().getOwner().getId() == ownerId)
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .collect(Collectors.toList());
            case "CURRENT":
                return bookingRepository.findAll().stream()
                        .filter(booking -> booking.getItem().getOwner().getId() == ownerId)
                        .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                        .filter(booking -> booking.getEnd().isAfter(LocalDateTime.now()))
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .collect(Collectors.toList());
            case "PAST":
                return bookingRepository.findAll().stream()
                        .filter(booking -> booking.getItem().getOwner().getId() == ownerId)
                        .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .collect(Collectors.toList());
            case "FUTURE":
                return bookingRepository.findAll().stream()
                        .filter(booking -> booking.getItem().getOwner().getId() == ownerId)
                        .filter(booking -> booking.getStatus() == BookingStatus.APPROVED || booking.getStatus() == BookingStatus.WAITING)
                        .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .collect(Collectors.toList());
            case "WAITING":
                return bookingRepository.findAll().stream()
                        .filter(booking -> booking.getItem().getOwner().getId() == ownerId)
                        .filter(booking -> booking.getStatus() == BookingStatus.WAITING)
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .collect(Collectors.toList());
            case "REJECTED":
                return bookingRepository.findAll().stream()
                        .filter(booking -> booking.getItem().getOwner().getId() == ownerId)
                        .filter(booking -> booking.getStatus() == BookingStatus.REJECTED)
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .collect(Collectors.toList());
            default:
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
