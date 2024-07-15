package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDtoOutcomeLong;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ItemServiceImplIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    @DirtiesContext
    public void getItemById() {
        User user1 = new User().setName("user").setEmail("ya@ya.ru");
        User user2 = new User().setName("user2").setEmail("ya2@ya.ru");
        User owner = userRepository.save(user1);
        User booker = userRepository.save(user2);
        Item item = new Item()
                .setOwner(owner)
                .setName("item")
                .setDescription("smth")
                .setAvailable(true);
        Item savedItem = itemRepository.save(item);
        Booking booking = new Booking()
                .setItem(savedItem)
                .setStart(LocalDateTime.now().plusMinutes(10))
                .setEnd(LocalDateTime.now().plusMinutes(20))
                .setBooker(booker)
                .setStatus(BookingStatus.WAITING);
        Booking next = bookingRepository.save(booking);
        Booking booking2 = new Booking()
                .setItem(savedItem)
                .setStart(LocalDateTime.now().minusMinutes(10))
                .setEnd(LocalDateTime.now().minusMinutes(1))
                .setBooker(booker)
                .setStatus(BookingStatus.WAITING);
        Booking last = bookingRepository.save(booking2);
        ItemDtoOutcomeLong outcome = itemService.getItemById(savedItem.getId(), owner.getId());
        assertEquals(item.getDescription(), outcome.getDescription());
        assertEquals(next.getStart().withNano(0),
                outcome.getNextBooking().getStart().withNano(0));
        assertEquals(last.getStart().withNano(0),
                outcome.getLastBooking().getStart().withNano(0));
    }
}