package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.booking_getter.model.BookingGetter;
import ru.practicum.shareit.booking.booking_getter.model.BookingGetterType;
import ru.practicum.shareit.booking.dto.BookingDtoIncome;
import ru.practicum.shareit.booking.dto.BookingDtoOutcomeLong;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.BookingTimeException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    private BookingService bookingService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingRepository bookingRepository;

    private final BookingMapper bookingMapper = new BookingMapperImpl();

    @BeforeEach
    public void setUp() {
        bookingService = new BookingServiceImpl(
                userRepository,
                itemRepository,
                bookingRepository,
                bookingMapper);
    }

    private static Stream<Arguments> badBookingTimeStream() {
        return Stream.of(
                Arguments.of(LocalDateTime.now(), LocalDateTime.now()),
                Arguments.of(LocalDateTime.now().minusMinutes(10), LocalDateTime.now().minusMinutes(20)),
                Arguments.of(LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(5)));
    }

    @ParameterizedTest()
    @MethodSource("badBookingTimeStream")
    public void createBookingWithWrongTimeTest(LocalDateTime start, LocalDateTime end) {
        BookingDtoIncome income = new BookingDtoIncome()
                .setStart(start)
                .setEnd(end)
                .setItemId(1L);
        User booker = new User()
                .setId(1L)
                .setName("booker")
                .setEmail("ya@ya.ru");
        User owner = new User()
                .setId(2L)
                .setName("owner")
                .setEmail("ya2@ya.ru");
        Item item = new Item()
                .setId(1L)
                .setOwner(owner).setName("Item")
                .setDescription("Desc")
                .setAvailable(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        assertThrows(BookingTimeException.class, () -> bookingService.createBooking(1L, income));
    }

    @Test
    public void createBookingNotAvailableTest() {
        BookingDtoIncome income = new BookingDtoIncome()
                .setStart(LocalDateTime.now())
                .setEnd(LocalDateTime.now().plusDays(5))
                .setItemId(1L);
        User booker = new User()
                .setId(1L)
                .setName("booker")
                .setEmail("ya@ya.ru");
        User owner = new User()
                .setId(2L)
                .setName("owner")
                .setEmail("ya2@ya.ru");
        Item item = new Item()
                .setId(1L)
                .setOwner(owner).setName("Item")
                .setDescription("Desc")
                .setAvailable(false);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        assertThrows(BookingTimeException.class, () -> bookingService.createBooking(1L, income));
    }

    @Test()
    public void createBookingTest() {
        BookingDtoIncome income = new BookingDtoIncome()
                .setStart(LocalDateTime.now())
                .setEnd(LocalDateTime.now().plusDays(1L))
                .setItemId(1L);
        User booker = new User()
                .setId(1L)
                .setName("booker")
                .setEmail("ya@ya.ru");
        User owner = new User()
                .setId(2L)
                .setName("owner")
                .setEmail("ya2@ya.ru");
        Item item = new Item()
                .setId(1L)
                .setOwner(owner).setName("Item")
                .setDescription("Desc")
                .setAvailable(true);
        Booking booking = new Booking()
                .setId(1L)
                .setItem(item)
                .setBooker(booker)
                .setStart(income.getStart())
                .setEnd(income.getEnd())
                .setStatus(BookingStatus.WAITING);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(ArgumentMatchers.any(Booking.class))).thenReturn(booking);
        BookingDtoOutcomeLong outcome = bookingService.createBooking(1L, income);
        BookingDtoOutcomeLong expected = bookingMapper.toSendLong(booking);
        assertEquals(expected, outcome);

        verify(userRepository).findById(anyLong());
        verify(itemRepository).findById(anyLong());
        verify(bookingRepository).save(ArgumentMatchers.any(Booking.class));
    }

    @Test
    public void approveBookingWhichIsAlreadyApprovedTest() {
        BookingDtoIncome income = new BookingDtoIncome()
                .setStart(LocalDateTime.now())
                .setEnd(LocalDateTime.now().plusDays(1L))
                .setItemId(1L);
        User booker = new User()
                .setId(1L)
                .setName("booker")
                .setEmail("ya@ya.ru");
        User owner = new User()
                .setId(2L)
                .setName("owner")
                .setEmail("ya2@ya.ru");
        Item item = new Item()
                .setId(1L)
                .setOwner(owner).setName("Item")
                .setDescription("Desc")
                .setAvailable(true);
        Booking booking = new Booking()
                .setId(1L)
                .setItem(item)
                .setBooker(booker)
                .setStart(income.getStart())
                .setEnd(income.getEnd())
                .setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        assertThrows(BadRequestException.class, () -> bookingService
                .approveBooking(2L, 1L, true));
    }

    @Test
    public void approveBookingTest() {
        User booker = new User()
                .setId(1L)
                .setName("booker")
                .setEmail("ya@ya.ru");
        User owner = new User()
                .setId(2L)
                .setName("owner")
                .setEmail("ya2@ya.ru");
        Item item = new Item()
                .setId(1L)
                .setOwner(owner)
                .setName("Item")
                .setDescription("Desc")
                .setAvailable(true);
        Booking booking = new Booking()
                .setId(1L)
                .setItem(item)
                .setBooker(booker)
                .setStart(LocalDateTime.now().withNano(0))
                .setEnd(LocalDateTime.now().withNano(0).plusDays(1))
                .setStatus(BookingStatus.WAITING);
        Booking approvedBooking = new Booking()
                .setId(1L)
                .setItem(item)
                .setBooker(booker)
                .setStart(LocalDateTime.now().withNano(0))
                .setEnd(LocalDateTime.now().withNano(0).plusDays(1))
                .setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(approvedBooking)).thenReturn(approvedBooking);
        assertEquals(BookingStatus.APPROVED,
                bookingService.approveBooking(2L, 1L, true).getStatus());
    }

    @Test
    public void getAllBookingsByIdWithWrongState() {
        BookingGetter getter = new BookingGetter()
                .setFrom(1)
                .setSize(1)
                .setType(BookingGetterType.BOOKER)
                .setState("Wrong State")
                .setUserId(1L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        assertThrows(BadRequestException.class, () -> bookingService.getAllBookingsById(getter));
    }
}