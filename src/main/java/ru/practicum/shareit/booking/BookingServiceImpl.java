package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.creator_request.checker.BookingTimeChecker;
import ru.practicum.shareit.booking.creator_request.model.CreateRequest;
import ru.practicum.shareit.booking.creator_request.checker.CreateRequestChecker;
import ru.practicum.shareit.booking.creator_request.checker.IsAvailableChecker;
import ru.practicum.shareit.booking.creator_request.checker.IsBookerOwnerChecker;
import ru.practicum.shareit.booking.dto.BookingDtoIncome;
import ru.practicum.shareit.booking.dto.BookingDtoOutcomeLong;
import ru.practicum.shareit.booking.getter_request.checker.GetterRequestChecker;
import ru.practicum.shareit.booking.getter_request.checker.StateTypeChecker;
import ru.practicum.shareit.booking.getter_request.checker.UserExistChecker;
import ru.practicum.shareit.booking.getter_request.model.GetterRequest;
import ru.practicum.shareit.booking.getter_request.model.GetterRequestState;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.BadRequestException;
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
        LocalDateTime start = bookingDtoIncome.getStart();
        LocalDateTime end = bookingDtoIncome.getEnd();
        CreateRequest request = new CreateRequest()
                .setStart(start)
                .setEnd(end)
                .setBookerId(bookerId)
                .setOwnerId(item.getOwner().getId())
                .setAvailable(item.getAvailable());
        CreateRequestChecker timeChecker = new BookingTimeChecker();
        CreateRequestChecker isBookerOwnerChecker = new IsBookerOwnerChecker();
        CreateRequestChecker isAvailableChecker = new IsAvailableChecker();
        timeChecker.setNext(isBookerOwnerChecker);
        isBookerOwnerChecker.setNext(isAvailableChecker);
        timeChecker.check(request);
        Booking booking = bookingMapper.toSave(bookingDtoIncome)
                .setItem(item)
                .setBooker(booker);

        return bookingMapper.toSendLong(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoOutcomeLong approveBooking(long ownerId, long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_BOOKING_WITH_SUCH_ID + bookingId));
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
                .orElseThrow(() -> new NotFoundException(Constants.NO_BOOKING_WITH_SUCH_ID + bookingId));
        if (booking.getBooker().getId() == ownerOrClientId
                || booking.getItem().getOwner().getId() == ownerOrClientId) {
            return bookingMapper.toSendLong(booking);
        } else {
            throw new NotFoundException("Bad ID: " + ownerOrClientId);
        }
    }

    @Override
    public List<BookingDtoOutcomeLong> getAllBookingsById(GetterRequest request) {
        GetterRequestChecker requestValidator = new UserExistChecker(userRepository);
        GetterRequestChecker checkStateType = new StateTypeChecker();
        requestValidator.setNext(checkStateType);
        requestValidator.check(request);
        switch (request.getType()) {
            case BOOKER:
                return makeBookingListByState(bookingRepository
                        .findAllByBookerIdOrderByStartDesc(request.getUserId()), request.getState());
            case OWNER:
                List<Booking> ownerBookings = bookingRepository
                        .findAllByItemOwnerId(request.getUserId());
                if (ownerBookings.isEmpty()) {
                    throw new NotFoundException("There are no items of user with ID: " + request.getUserId());
                }
                return makeBookingListByState(ownerBookings, request.getState());
            default:
                return List.of();
        }
    }

    private List<BookingDtoOutcomeLong> makeBookingListByState(List<Booking> bookings, String state) {
        List<BookingDtoOutcomeLong> list = bookings.stream()
                .map(booking -> bookingMapper.toSendLong(booking))
                .collect(Collectors.toList());
        switch (GetterRequestState.valueOf(state)) {
            case ALL:
                return list;
            case CURRENT:
                return list.stream()
                        .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                        .filter(booking -> booking.getEnd().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case PAST:
                return list.stream()
                        .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case FUTURE:
                return list.stream()
                        .filter(booking -> booking.getStatus() == BookingStatus.APPROVED
                                || booking.getStatus() == BookingStatus.WAITING)
                        .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case WAITING:
                return list.stream()
                        .filter(booking -> booking.getStatus() == BookingStatus.WAITING)
                        .collect(Collectors.toList());
            case REJECTED:
                return list.stream()
                        .filter(booking -> booking.getStatus() == BookingStatus.REJECTED)
                        .collect(Collectors.toList());
            default:
                return List.of();
        }
    }
}
