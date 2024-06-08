package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.util.Constants;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

    private BookingService service;

    @PostMapping
    public Booking createBookingRequest(@RequestHeader(Constants.X_SHARER_USER_ID) long clientId, @Valid @RequestBody BookingDto bookingDto) {
        // После создания запрос находится в статусе WAITING — «ожидает подтверждения».
        return service.createBookingRequest(clientId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public Booking bookingConfirmation(@RequestHeader(Constants.X_SHARER_USER_ID) long ownerId,
                                       @PathVariable long bookingId,
                                       @RequestParam Boolean approved) {
        // Подтверждение или отклонение запроса на бронирование.
        // Может быть выполнено только владельцем вещи.
        // Затем статус бронирования становится либо APPROVED, либо REJECTED.
        // Эндпоинт — PATCH /bookings/{bookingId}?approved={approved},
        // параметр approved может принимать значения true или false.
        return service.bookingConfirmation(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingStatus(@RequestHeader(Constants.X_SHARER_USER_ID) long ownerOrClientId,
                                    @PathVariable long bookingId) {
        // Получение данных о конкретном бронировании (включая его статус).
        // Может быть выполнено либо автором бронирования,
        // либо владельцем вещи, к которой относится бронирование.
        // Эндпоинт — GET /bookings/{bookingI
        return service.getBookingStatus(ownerOrClientId, bookingId);
    }

    @GetMapping()
    public List<Booking> getClientBookings(@RequestHeader(Constants.X_SHARER_USER_ID) long clientId,
                                           @RequestParam(required = false, defaultValue = "ALL") String state) {
        return service.getClientBookings(clientId, state);
    }

    @GetMapping("/owner")
    public List<Booking> getOwnerBookings(@RequestHeader(Constants.X_SHARER_USER_ID) long ownerId,
                                          @RequestParam(required = false, defaultValue = "ALL") String state) {
        return service.getOwnerBookings(ownerId, state);
    }
}
