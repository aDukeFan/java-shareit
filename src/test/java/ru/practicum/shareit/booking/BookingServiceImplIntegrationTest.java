package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDtoOutcomeLong;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookingServiceImplIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    @DirtiesContext
    public void getBooking() {
        User firstUser = new User()
                .setName("firstUser")
                .setEmail("ya@ya.ru");
        User owner = userRepository.save(firstUser);
        User secondUser = new User()
                .setName("secondUser")
                .setEmail("ya2@ya.ru");
        User booker = userRepository.save(secondUser);
        Item item = new Item()
                .setOwner(owner)
                .setAvailable(true)
                .setName("Book")
                .setDescription("War and peace");
        Item savedItem = itemRepository.save(item);
        Booking booking = new Booking()
                .setItem(savedItem)
                .setBooker(booker)
                .setStart(LocalDateTime.now())
                .setEnd(LocalDateTime.now().plusMinutes(100))
                .setStatus(BookingStatus.WAITING);
        Booking savedBooking = bookingRepository.save(booking);
        BookingDtoOutcomeLong bookingDtoOutcomeLong = bookingService.getBooking(owner.getId(), savedBooking.getId());
        assertEquals(1, bookingDtoOutcomeLong.getId());
        assertEquals(booker.getEmail(), bookingDtoOutcomeLong.getBooker().getEmail());
        assertEquals(1, bookingDtoOutcomeLong.getItem().getId());
        assertEquals(booking.getStart(), bookingDtoOutcomeLong.getStart());
    }
}