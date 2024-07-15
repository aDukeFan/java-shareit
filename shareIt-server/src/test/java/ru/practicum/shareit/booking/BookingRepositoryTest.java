package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    @DirtiesContext
    public void findAllByItemIdTest() {
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
        assertTrue(bookingRepository.findAllByItemId(2L).isEmpty());
        assertEquals(savedBooking, bookingRepository.findAllByItemId(savedItem.getId())
                .stream()
                .findFirst()
                .get());
    }

    @Test
    @DirtiesContext
    public void findAllByBookerIdAndItemIdTest() {
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
        assertTrue(bookingRepository.findAllByBookerIdAndItemId(2L, 2L).isEmpty());
        assertEquals(savedBooking, bookingRepository.findAllByBookerIdAndItemId(secondUser.getId(), savedItem.getId())
                .stream()
                .findFirst()
                .get());
    }

    @Test
    @DirtiesContext
    public void findAllByItemOwnerIdOrderByStartDescTest() {
        User firstUser = new User()
                .setName("firstUser")
                .setEmail("ya@ya.ru");
        User owner = userRepository.save(firstUser);
        User secondUser = new User()
                .setName("secondUser")
                .setEmail("ya2@ya.ru");
        User booker = userRepository.save(secondUser);
        Item firstItem = new Item()
                .setOwner(owner)
                .setAvailable(true)
                .setName("War and Peace")
                .setDescription("About life, Tolstoy");
        Item secondItem = new Item()
                .setOwner(owner)
                .setAvailable(true)
                .setName("Anna Karenina")
                .setDescription("About life, Tolstoy");
        Item firstSavedItem = itemRepository.save(firstItem);
        Item secondSavedItem = itemRepository.save(secondItem);
        Booking firstBooking = new Booking()
                .setItem(firstSavedItem)
                .setBooker(booker)
                .setStart(LocalDateTime.now())
                .setEnd(LocalDateTime.now().plusMinutes(100))
                .setStatus(BookingStatus.WAITING);
        Booking secondBooking = new Booking()
                .setItem(secondSavedItem)
                .setBooker(booker)
                .setStart(LocalDateTime.now().plusMinutes(100))
                .setEnd(LocalDateTime.now().plusMinutes(200))
                .setStatus(BookingStatus.WAITING);
        Booking savedBooking = bookingRepository.save(firstBooking);
        Booking savedBooking2 = bookingRepository.save(secondBooking);
        List<Booking> bookings = List.of(savedBooking2, savedBooking);
        assertEquals(bookings, bookingRepository
                .findAllByItemOwnerIdOrderByStartDesc(owner.getId(), PageRequest.of(0, 100)));
    }

    @Test
    @DirtiesContext
    public void findAllByBookerIdOrderByStartDescTest() {
        User firstUser = new User()
                .setName("firstUser")
                .setEmail("ya@ya.ru");
        User owner = userRepository.save(firstUser);
        User secondUser = new User()
                .setName("secondUser")
                .setEmail("ya2@ya.ru");
        User booker = userRepository.save(secondUser);
        Item firstItem = new Item()
                .setOwner(owner)
                .setAvailable(true)
                .setName("War and Peace")
                .setDescription("About life, Tolstoy");
        Item secondItem = new Item()
                .setOwner(owner)
                .setAvailable(true)
                .setName("Anna Karenina")
                .setDescription("About life, Tolstoy");
        Item firstSavedItem = itemRepository.save(firstItem);
        Item secondSavedItem = itemRepository.save(secondItem);
        Booking firstBooking = new Booking()
                .setItem(firstSavedItem)
                .setBooker(booker)
                .setStart(LocalDateTime.now())
                .setEnd(LocalDateTime.now().plusMinutes(100))
                .setStatus(BookingStatus.WAITING);
        Booking secondBooking = new Booking()
                .setItem(secondSavedItem)
                .setBooker(booker)
                .setStart(LocalDateTime.now().plusMinutes(100))
                .setEnd(LocalDateTime.now().plusMinutes(200))
                .setStatus(BookingStatus.WAITING);
        Booking savedBooking = bookingRepository.save(firstBooking);
        Booking savedBooking2 = bookingRepository.save(secondBooking);
        List<Booking> bookings = List.of(savedBooking2, savedBooking);
        assertEquals(bookings, bookingRepository
                .findAllByBookerIdOrderByStartDesc(booker.getId(), PageRequest.of(0, 100)));
    }

    @Test
    @DirtiesContext
    public void findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDescTest() {
        User firstUser = new User()
                .setName("firstUser")
                .setEmail("ya@ya.ru");
        User owner = userRepository.save(firstUser);
        User secondUser = new User()
                .setName("secondUser")
                .setEmail("ya2@ya.ru");
        User booker = userRepository.save(secondUser);
        Item firstItem = new Item()
                .setOwner(owner)
                .setAvailable(true)
                .setName("War and Peace")
                .setDescription("About life, Tolstoy");
        Item secondItem = new Item()
                .setOwner(owner)
                .setAvailable(true)
                .setName("Anna Karenina")
                .setDescription("About life, Tolstoy");
        Item firstSavedItem = itemRepository.save(firstItem);
        Item secondSavedItem = itemRepository.save(secondItem);
        Booking firstBooking = new Booking()
                .setItem(firstSavedItem)
                .setBooker(booker)
                .setStart(LocalDateTime.now().minusMinutes(100))
                .setEnd(LocalDateTime.now().plusMinutes(100))
                .setStatus(BookingStatus.WAITING);
        Booking secondBooking = new Booking()
                .setItem(secondSavedItem)
                .setBooker(booker)
                .setStart(LocalDateTime.now().minusMinutes(50))
                .setEnd(LocalDateTime.now().plusMinutes(100))
                .setStatus(BookingStatus.WAITING);
        Booking savedBooking = bookingRepository.save(firstBooking);
        Booking savedBooking2 = bookingRepository.save(secondBooking);
        List<Booking> bookings = List.of(savedBooking2, savedBooking);
        assertEquals(bookings, bookingRepository
                .findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(owner.getId(),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        PageRequest.of(0, 100)));
    }

    @Test
    @DirtiesContext
    public void findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDescTest() {
        User firstUser = new User()
                .setName("firstUser")
                .setEmail("ya@ya.ru");
        User owner = userRepository.save(firstUser);
        User secondUser = new User()
                .setName("secondUser")
                .setEmail("ya2@ya.ru");
        User booker = userRepository.save(secondUser);
        Item firstItem = new Item()
                .setOwner(owner)
                .setAvailable(true)
                .setName("War and Peace")
                .setDescription("About life, Tolstoy");
        Item secondItem = new Item()
                .setOwner(owner)
                .setAvailable(true)
                .setName("Anna Karenina")
                .setDescription("About life, Tolstoy");
        Item firstSavedItem = itemRepository.save(firstItem);
        Item secondSavedItem = itemRepository.save(secondItem);
        Booking firstBooking = new Booking()
                .setItem(firstSavedItem)
                .setBooker(booker)
                .setStart(LocalDateTime.now().minusMinutes(100))
                .setEnd(LocalDateTime.now().plusMinutes(100))
                .setStatus(BookingStatus.WAITING);
        Booking secondBooking = new Booking()
                .setItem(secondSavedItem)
                .setBooker(booker)
                .setStart(LocalDateTime.now().minusMinutes(50))
                .setEnd(LocalDateTime.now().plusMinutes(100))
                .setStatus(BookingStatus.WAITING);
        Booking savedBooking = bookingRepository.save(firstBooking);
        Booking savedBooking2 = bookingRepository.save(secondBooking);
        List<Booking> bookings = List.of(savedBooking2, savedBooking);
        assertEquals(bookings, bookingRepository
                .findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(booker.getId(),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        PageRequest.of(0, 100)));
    }

    @Test
    @DirtiesContext
    public void findAllByItemOwnerIdAndEndIsBeforeOrderByStartDescTest() {
        User firstUser = new User()
                .setName("firstUser")
                .setEmail("ya@ya.ru");
        User owner = userRepository.save(firstUser);
        User secondUser = new User()
                .setName("secondUser")
                .setEmail("ya2@ya.ru");
        User booker = userRepository.save(secondUser);
        Item firstItem = new Item()
                .setOwner(owner)
                .setAvailable(true)
                .setName("War and Peace")
                .setDescription("About life, Tolstoy");
        Item secondItem = new Item()
                .setOwner(owner)
                .setAvailable(true)
                .setName("Anna Karenina")
                .setDescription("About life, Tolstoy");
        Item firstSavedItem = itemRepository.save(firstItem);
        Item secondSavedItem = itemRepository.save(secondItem);
        Booking firstBooking = new Booking()
                .setItem(firstSavedItem)
                .setBooker(booker)
                .setStart(LocalDateTime.now().minusMinutes(100))
                .setEnd(LocalDateTime.now().minusMinutes(10))
                .setStatus(BookingStatus.WAITING);
        Booking secondBooking = new Booking()
                .setItem(secondSavedItem)
                .setBooker(booker)
                .setStart(LocalDateTime.now().minusMinutes(50))
                .setEnd(LocalDateTime.now().minusMinutes(10))
                .setStatus(BookingStatus.WAITING);
        Booking savedBooking = bookingRepository.save(firstBooking);
        Booking savedBooking2 = bookingRepository.save(secondBooking);
        List<Booking> bookings = List.of(savedBooking2, savedBooking);
        assertEquals(bookings, bookingRepository
                .findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(owner.getId(),
                        LocalDateTime.now(),
                        PageRequest.of(0, 100)));
    }

    @Test
    @DirtiesContext
    public void findAllByBookerIdAndEndIsBeforeOrderByStartDescTest() {
        User firstUser = new User()
                .setName("firstUser")
                .setEmail("ya@ya.ru");
        User owner = userRepository.save(firstUser);
        User secondUser = new User()
                .setName("secondUser")
                .setEmail("ya2@ya.ru");
        User booker = userRepository.save(secondUser);
        Item firstItem = new Item()
                .setOwner(owner)
                .setAvailable(true)
                .setName("War and Peace")
                .setDescription("About life, Tolstoy");
        Item secondItem = new Item()
                .setOwner(owner)
                .setAvailable(true)
                .setName("Anna Karenina")
                .setDescription("About life, Tolstoy");
        Item firstSavedItem = itemRepository.save(firstItem);
        Item secondSavedItem = itemRepository.save(secondItem);
        Booking firstBooking = new Booking()
                .setItem(firstSavedItem)
                .setBooker(booker)
                .setStart(LocalDateTime.now().minusMinutes(100))
                .setEnd(LocalDateTime.now().minusMinutes(10))
                .setStatus(BookingStatus.WAITING);
        Booking secondBooking = new Booking()
                .setItem(secondSavedItem)
                .setBooker(booker)
                .setStart(LocalDateTime.now().minusMinutes(50))
                .setEnd(LocalDateTime.now().minusMinutes(10))
                .setStatus(BookingStatus.WAITING);
        Booking savedBooking = bookingRepository.save(firstBooking);
        Booking savedBooking2 = bookingRepository.save(secondBooking);
        List<Booking> bookings = List.of(savedBooking2, savedBooking);
        assertEquals(bookings, bookingRepository
                .findAllByBookerIdAndEndIsBeforeOrderByStartDesc(booker.getId(),
                        LocalDateTime.now(),
                        PageRequest.of(0, 100)));
    }

    @Test
    @DirtiesContext
    public void findAllByItemOwnerIdAndStartIsAfterAndStatusIsOrStatusIsOrderByStartDescTest() {
        User firstUser = new User()
                .setName("firstUser")
                .setEmail("ya@ya.ru");
        User owner = userRepository.save(firstUser);
        User secondUser = new User()
                .setName("secondUser")
                .setEmail("ya2@ya.ru");
        User booker = userRepository.save(secondUser);
        Item firstItem = new Item()
                .setOwner(owner)
                .setAvailable(true)
                .setName("War and Peace")
                .setDescription("About life, Tolstoy");
        Item secondItem = new Item()
                .setOwner(owner)
                .setAvailable(true)
                .setName("Anna Karenina")
                .setDescription("About life, Tolstoy");
        Item firstSavedItem = itemRepository.save(firstItem);
        Item secondSavedItem = itemRepository.save(secondItem);
        Booking firstBooking = new Booking()
                .setItem(firstSavedItem)
                .setBooker(booker)
                .setStart(LocalDateTime.now().plusMinutes(10))
                .setEnd(LocalDateTime.now().plusMinutes(200))
                .setStatus(BookingStatus.WAITING);
        Booking secondBooking = new Booking()
                .setItem(secondSavedItem)
                .setBooker(booker)
                .setStart(LocalDateTime.now().plusMinutes(50))
                .setEnd(LocalDateTime.now().plusMinutes(200))
                .setStatus(BookingStatus.WAITING);
        Booking savedBooking = bookingRepository.save(firstBooking);
        Booking savedBooking2 = bookingRepository.save(secondBooking);
        List<Booking> bookings = List.of(savedBooking2, savedBooking);
        assertEquals(bookings, bookingRepository
                .findAllByItemOwnerIdAndStartIsAfterAndStatusIsOrStatusIsOrderByStartDesc(owner.getId(),
                        LocalDateTime.now(), BookingStatus.APPROVED, BookingStatus.WAITING,
                        PageRequest.of(0, 100)));
    }

    @Test
    @DirtiesContext
    public void findAllByBookerIdAndStartIsAfterAndStatusIsOrStatusIsOrderByStartDescTest() {
        User firstUser = new User()
                .setName("firstUser")
                .setEmail("ya@ya.ru");
        User owner = userRepository.save(firstUser);
        User secondUser = new User()
                .setName("secondUser")
                .setEmail("ya2@ya.ru");
        User booker = userRepository.save(secondUser);
        Item firstItem = new Item()
                .setOwner(owner)
                .setAvailable(true)
                .setName("War and Peace")
                .setDescription("About life, Tolstoy");
        Item secondItem = new Item()
                .setOwner(owner)
                .setAvailable(true)
                .setName("Anna Karenina")
                .setDescription("About life, Tolstoy");
        Item firstSavedItem = itemRepository.save(firstItem);
        Item secondSavedItem = itemRepository.save(secondItem);
        Booking firstBooking = new Booking()
                .setItem(firstSavedItem)
                .setBooker(booker)
                .setStart(LocalDateTime.now().plusMinutes(10))
                .setEnd(LocalDateTime.now().plusMinutes(200))
                .setStatus(BookingStatus.WAITING);
        Booking secondBooking = new Booking()
                .setItem(secondSavedItem)
                .setBooker(booker)
                .setStart(LocalDateTime.now().plusMinutes(50))
                .setEnd(LocalDateTime.now().plusMinutes(200))
                .setStatus(BookingStatus.WAITING);
        Booking savedBooking = bookingRepository.save(firstBooking);
        Booking savedBooking2 = bookingRepository.save(secondBooking);
        List<Booking> bookings = List.of(savedBooking2, savedBooking);
        assertEquals(bookings, bookingRepository
                .findAllByBookerIdAndStartIsAfterAndStatusIsOrStatusIsOrderByStartDesc(booker.getId(),
                        LocalDateTime.now(), BookingStatus.APPROVED, BookingStatus.WAITING,
                        PageRequest.of(0, 100)));
    }

    @Test
    @DirtiesContext
    public void findAllByItemOwnerIdAndStatusIsOrderByStartDescTest() {
        User firstUser = new User()
                .setName("firstUser")
                .setEmail("ya@ya.ru");
        User owner = userRepository.save(firstUser);
        User secondUser = new User()
                .setName("secondUser")
                .setEmail("ya2@ya.ru");
        User booker = userRepository.save(secondUser);
        Item firstItem = new Item()
                .setOwner(owner)
                .setAvailable(true)
                .setName("War and Peace")
                .setDescription("About life, Tolstoy");
        Item secondItem = new Item()
                .setOwner(owner)
                .setAvailable(true)
                .setName("Anna Karenina")
                .setDescription("About life, Tolstoy");
        Item firstSavedItem = itemRepository.save(firstItem);
        Item secondSavedItem = itemRepository.save(secondItem);
        Booking firstBooking = new Booking()
                .setItem(firstSavedItem)
                .setBooker(booker)
                .setStart(LocalDateTime.now().plusMinutes(10))
                .setEnd(LocalDateTime.now().plusMinutes(200))
                .setStatus(BookingStatus.WAITING);
        Booking secondBooking = new Booking()
                .setItem(secondSavedItem)
                .setBooker(booker)
                .setStart(LocalDateTime.now().plusMinutes(50))
                .setEnd(LocalDateTime.now().plusMinutes(200))
                .setStatus(BookingStatus.WAITING);
        Booking savedBooking = bookingRepository.save(firstBooking);
        Booking savedBooking2 = bookingRepository.save(secondBooking);
        List<Booking> bookings = List.of(savedBooking2, savedBooking);
        assertEquals(bookings, bookingRepository
                .findAllByItemOwnerIdAndStatusIsOrderByStartDesc(owner.getId(),
                        BookingStatus.WAITING,
                        PageRequest.of(0, 100)));
        assertTrue(bookingRepository
                .findAllByItemOwnerIdAndStatusIsOrderByStartDesc(owner.getId(),
                        BookingStatus.REJECTED,
                        PageRequest.of(0, 100)).isEmpty());
    }

    @Test
    @DirtiesContext
    public void findAllByBookerIdAndStatusIsOrderByStartDescTest() {
        User firstUser = new User()
                .setName("firstUser")
                .setEmail("ya@ya.ru");
        User owner = userRepository.save(firstUser);
        User secondUser = new User()
                .setName("secondUser")
                .setEmail("ya2@ya.ru");
        User booker = userRepository.save(secondUser);
        Item firstItem = new Item()
                .setOwner(owner)
                .setAvailable(true)
                .setName("War and Peace")
                .setDescription("About life, Tolstoy");
        Item secondItem = new Item()
                .setOwner(owner)
                .setAvailable(true)
                .setName("Anna Karenina")
                .setDescription("About life, Tolstoy");
        Item firstSavedItem = itemRepository.save(firstItem);
        Item secondSavedItem = itemRepository.save(secondItem);
        Booking firstBooking = new Booking()
                .setItem(firstSavedItem)
                .setBooker(booker)
                .setStart(LocalDateTime.now().plusMinutes(10))
                .setEnd(LocalDateTime.now().plusMinutes(200))
                .setStatus(BookingStatus.WAITING);
        Booking secondBooking = new Booking()
                .setItem(secondSavedItem)
                .setBooker(booker)
                .setStart(LocalDateTime.now().plusMinutes(50))
                .setEnd(LocalDateTime.now().plusMinutes(200))
                .setStatus(BookingStatus.WAITING);
        Booking savedBooking = bookingRepository.save(firstBooking);
        Booking savedBooking2 = bookingRepository.save(secondBooking);
        List<Booking> bookings = List.of(savedBooking2, savedBooking);
        assertEquals(bookings, bookingRepository
                .findAllByBookerIdAndStatusIsOrderByStartDesc(booker.getId(),
                        BookingStatus.WAITING,
                        PageRequest.of(0, 100)));
        assertTrue(bookingRepository
                .findAllByBookerIdAndStatusIsOrderByStartDesc(booker.getId(),
                        BookingStatus.REJECTED,
                        PageRequest.of(0, 100)).isEmpty());
    }
}