package ru.practicum.shareit.booking.creator_request.checker;

import ru.practicum.shareit.booking.creator_request.model.CreateRequest;
import ru.practicum.shareit.exception.BookingTimeException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class BookingTimeChecker extends CreateRequestChecker {

    public BookingTimeChecker(CreateRequestChecker next) {
        super(next);
    }

    @Override
    public void check(CreateRequest request) {
        LocalDateTime start = request.getStart().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime end = request.getEnd().truncatedTo(ChronoUnit.SECONDS);
        if (start.isAfter(end) || start.equals(end)) {
            throw new BookingTimeException("Wrong booking time");
        }

        checkNext(request);
    }
}
