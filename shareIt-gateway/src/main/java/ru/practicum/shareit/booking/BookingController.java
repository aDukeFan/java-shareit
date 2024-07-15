package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.booking_getter.model.BookingGetter;
import ru.practicum.shareit.booking.booking_getter.model.BookingGetterType;
import ru.practicum.shareit.booking.dto.BookingDtoIncome;
import ru.practicum.shareit.booking.dto.BookingDtoOutcomeLong;
import ru.practicum.shareit.util.Constants;
import ru.practicum.shareit.booking.booking_getter.validator.ValidState;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
@Validated
public class BookingController {

    private BookingServiceClient client;

    @PostMapping
    public BookingDtoOutcomeLong createBooking(@RequestHeader(Constants.X_SHARER_USER_ID) long bookerId,
                                               @Valid @RequestBody BookingDtoIncome income) {
        return client.createBooking(bookerId, income);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOutcomeLong approveBooking(@RequestHeader(Constants.X_SHARER_USER_ID) long ownerId,
                                                @PathVariable long bookingId,
                                                @RequestParam Boolean approved) {
        return client.approveBooking(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOutcomeLong getBooking(@RequestHeader(Constants.X_SHARER_USER_ID) long ownerOrClientId,
                                            @PathVariable long bookingId) {
        return client.getBooking(ownerOrClientId, bookingId);
    }

    @GetMapping()
    public List<BookingDtoOutcomeLong> getBookerBookings(@RequestHeader(Constants.X_SHARER_USER_ID) long bookerId,
                                                         @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                                         @RequestParam(required = false, defaultValue = "100") @Min(1) Integer size,
                                                         @RequestParam(required = false, defaultValue = "ALL") @ValidState String state) {
        BookingGetter getter = new BookingGetter()
                .setUserId(bookerId)
                .setState(state)
                .setType(BookingGetterType.BOOKER)
                .setSize(size)
                .setFrom(from);
        return client.getAllBookingsById(getter);
    }

    @GetMapping("/owner")
    public List<BookingDtoOutcomeLong> getOwnerBookings(@RequestHeader(Constants.X_SHARER_USER_ID) long ownerId,
                                                        @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                                        @RequestParam(required = false, defaultValue = "100") @Min(1) Integer size,
                                                        @RequestParam(required = false, defaultValue = "ALL") @ValidState String state) {
        BookingGetter getter = new BookingGetter()
                .setUserId(ownerId)
                .setState(state)
                .setType(BookingGetterType.OWNER)
                .setSize(size)
                .setFrom(from);
        return client.getAllBookingsById(getter);
    }
}
