package ru.practicum.shareit.booking.creator_checker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.shareit.booking.creator_checker.checker.BookingTimeChecker;
import ru.practicum.shareit.booking.creator_checker.checker.CreatorChecker;
import ru.practicum.shareit.booking.creator_checker.checker.IsAvailableChecker;
import ru.practicum.shareit.booking.creator_checker.checker.IsBookerOwnerChecker;

@Configuration
public class CheckerCreatorChain {

    @Bean
    public CreatorChecker createCreatorChecker() {
        return new BookingTimeChecker(new IsBookerOwnerChecker(new IsAvailableChecker(null)));
    }
}
