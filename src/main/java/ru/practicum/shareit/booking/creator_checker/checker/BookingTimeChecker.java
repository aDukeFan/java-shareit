package ru.practicum.shareit.booking.creator_checker.checker;

import ru.practicum.shareit.booking.creator_checker.model.Creator;
import ru.practicum.shareit.exception.BookingTimeException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class BookingTimeChecker extends CreatorChecker {

    public BookingTimeChecker(CreatorChecker next) {
        super(next);
    }

    @Override
    public void check(Creator request) {
        LocalDateTime start = request.getStart().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime end = request.getEnd().truncatedTo(ChronoUnit.SECONDS);
        if (start.isAfter(end) || start.equals(end)) {
            throw new BookingTimeException("Wrong booking time");
        }

        checkNext(request);
    }
}
