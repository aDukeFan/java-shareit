package ru.practicum.shareit.booking;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookerDtoForOwnerItem;
import ru.practicum.shareit.booking.dto.BookingDtoIncome;
import ru.practicum.shareit.booking.dto.BookingDtoOutcomeLong;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    Booking toSave(BookingDtoIncome income);

    BookingDtoOutcomeLong toSendLong(Booking booking);

    @Mapping(target = "bookerId", source = "booker.id")
    @Mapping(target = "bookerName", source = "booker.name")
    BookerDtoForOwnerItem toOwnerItemShow(Booking booking);
}
