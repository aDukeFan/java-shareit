package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDtoIncome;
import ru.practicum.shareit.booking.dto.BookingDtoOutcomeLong;
import ru.practicum.shareit.booking.booking_getter.BookingGetter;

import java.util.List;

public interface BookingService {

    BookingDtoOutcomeLong createBooking(long clientId, BookingDtoIncome bookingDto);

    BookingDtoOutcomeLong approveBooking(long ownerId, long bookingId, Boolean approved);

    BookingDtoOutcomeLong getBooking(long ownerOrClientId, long bookingId);

    List<BookingDtoOutcomeLong> getAllBookingsById(BookingGetter request);
}
