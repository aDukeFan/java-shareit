package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    Booking createBookingRequest(long clientId, BookingDto bookingDto);

    Booking bookingConfirmation(long ownerId, long bookingId, Boolean approved);

    Booking getBookingStatus(long ownerOrClientId, long bookingId);

    List<Booking> getClientBookings(long clientId, String state);

    List<Booking> getOwnerBookings(long ownerId, String state);
}
