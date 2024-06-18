package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDtoIncome;
import ru.practicum.shareit.booking.dto.BookingDtoOutcomeLong;
import ru.practicum.shareit.booking.model.BookingRequestUserType;
import ru.practicum.shareit.util.Constants;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

    private BookingService service;

    @PostMapping
    public BookingDtoOutcomeLong createBooking(@RequestHeader(Constants.X_SHARER_USER_ID) long clientId,
                                               @Valid @RequestBody BookingDtoIncome income) {
        return service.createBooking(clientId, income);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOutcomeLong approveBooking(@RequestHeader(Constants.X_SHARER_USER_ID) long ownerId,
                                                @PathVariable long bookingId,
                                                @RequestParam Boolean approved) {
        return service.approveBooking(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOutcomeLong getBooking(@RequestHeader(Constants.X_SHARER_USER_ID) long ownerOrClientId,
                                            @PathVariable long bookingId) {
        return service.getBooking(ownerOrClientId, bookingId);
    }

    @GetMapping()
    public List<BookingDtoOutcomeLong> getBookerBookings(@RequestHeader(Constants.X_SHARER_USER_ID) long clientId,
                                                         @RequestParam(required = false, defaultValue = "ALL") String state) {
        return service.getAllBookingsById(BookingRequestUserType.BOOKER, clientId, state);
    }

    @GetMapping("/owner")
    public List<BookingDtoOutcomeLong> getOwnerBookings(@RequestHeader(Constants.X_SHARER_USER_ID) long ownerId,
                                                        @RequestParam(required = false, defaultValue = "ALL") String state) {
        return service.getAllBookingsById(BookingRequestUserType.OWNER, ownerId, state);
    }
}
