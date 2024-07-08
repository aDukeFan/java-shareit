package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.booking.booking_getter.model.BookingGetterType.BOOKER;
import static ru.practicum.shareit.booking.booking_getter.model.BookingGetterType.OWNER;

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
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        BookingDtoOutcomeLong outcome = bookingService.createBooking(1L, income);
        BookingDtoOutcomeLong expected = bookingMapper.toSendLong(booking);
        assertEquals(expected, outcome);

        verify(userRepository).findById(anyLong());
        verify(itemRepository).findById(anyLong());
        verify(bookingRepository).save(any(Booking.class));
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
        BookingGetter getter = makeGetter(BOOKER, "Wrong state");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        assertThrows(BadRequestException.class, () -> bookingService.getAllBookingsById(getter));
    }

    @Test
    public void getAllBookingsByIdByOwnerAll() {
        BookingGetter getter = makeGetter(OWNER, "ALL");
        Pageable pageable = makePageable();
        User user = makeUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository
                .findAllByItemOwnerIdOrderByStartDesc(1L, pageable))
                .thenReturn(List.of());
        bookingService.getAllBookingsById(getter);
        verify(bookingRepository).findAllByItemOwnerIdOrderByStartDesc(1, pageable);
    }

    @Test
    public void getAllBookingsByIdByOwnerCurrent() {
        BookingGetter getter = makeGetter(OWNER, "CURRENT");
        Pageable pageable = makePageable();
        User user = makeUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository
                .findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                        1L,
                        LocalDateTime.now().withNano(0),
                        LocalDateTime.now().withNano(0),
                        pageable))
                .thenReturn(List.of());
        bookingService.getAllBookingsById(getter);
        verify(bookingRepository)
                .findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                        1L,
                        LocalDateTime.now().withNano(0),
                        LocalDateTime.now().withNano(0),
                        pageable);
    }

    @Test
    public void getAllBookingsByIdByOwnerPast() {
        BookingGetter getter = makeGetter(OWNER, "PAST");
        Pageable pageable = makePageable();
        User user = makeUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository
                .findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(
                        1L,
                        LocalDateTime.now().withNano(0),
                        pageable))
                .thenReturn(List.of());
        bookingService.getAllBookingsById(getter);
        verify(bookingRepository)
                .findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(
                        1L,
                        LocalDateTime.now().withNano(0),
                        pageable);
    }

    @Test
    public void getAllBookingsByIdByOwnerFuture() {
        BookingGetter getter = makeGetter(OWNER, "FUTURE");
        Pageable pageable = makePageable();
        User user = makeUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository
                .findAllByItemOwnerIdAndStartIsAfterAndStatusIsOrStatusIsOrderByStartDesc(
                        1L,
                        LocalDateTime.now().withNano(0),
                        BookingStatus.APPROVED,
                        BookingStatus.WAITING,
                        pageable))
                .thenReturn(List.of());
        bookingService.getAllBookingsById(getter);
        verify(bookingRepository)
                .findAllByItemOwnerIdAndStartIsAfterAndStatusIsOrStatusIsOrderByStartDesc(
                        1L,
                        LocalDateTime.now().withNano(0),
                        BookingStatus.APPROVED,
                        BookingStatus.WAITING,
                        pageable);
    }

    @Test
    public void getAllBookingsByIdByOwnerWaiting() {
        BookingGetter getter = makeGetter(OWNER,"WAITING");
        Pageable pageable = makePageable();
        User user = makeUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository
                .findAllByItemOwnerIdAndStatusIsOrderByStartDesc(1L,
                        BookingStatus.WAITING,
                        pageable))
                .thenReturn(List.of());
        bookingService.getAllBookingsById(getter);
        verify(bookingRepository)
                .findAllByItemOwnerIdAndStatusIsOrderByStartDesc(1L,
                        BookingStatus.WAITING,
                        pageable);
    }

    @Test
    public void getAllBookingsByIdByOwnerRejected() {
        BookingGetter getter = makeGetter(OWNER, "REJECTED");
        Pageable pageable = makePageable();
        User user = makeUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository
                .findAllByItemOwnerIdAndStatusIsOrderByStartDesc(1L,
                        BookingStatus.REJECTED,
                        pageable))
                .thenReturn(List.of());
        bookingService.getAllBookingsById(getter);
        verify(bookingRepository)
                .findAllByItemOwnerIdAndStatusIsOrderByStartDesc(1L,
                        BookingStatus.REJECTED,
                        pageable);
    }

    @Test
    public void getAllBookingsByIdByBookerAll() {
        BookingGetter getter = makeGetter(BOOKER, "ALL");
        Pageable pageable = makePageable();
        User user = makeUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository
                .findAllByBookerIdOrderByStartDesc(1L, pageable))
                .thenReturn(List.of());
        bookingService.getAllBookingsById(getter);
        verify(bookingRepository).findAllByBookerIdOrderByStartDesc(1L, pageable);
    }

    @Test
    public void getAllBookingsByIdByBookerCurrent() {
        BookingGetter getter = makeGetter(BOOKER, "CURRENT");
        Pageable pageable = makePageable();
        User user = makeUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository
                .findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(1L,
                        LocalDateTime.now().withNano(0),
                        LocalDateTime.now().withNano(0),
                        pageable))
                .thenReturn(List.of());
        bookingService.getAllBookingsById(getter);
        verify(bookingRepository)
                .findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(1L,
                        LocalDateTime.now().withNano(0),
                        LocalDateTime.now().withNano(0),
                        pageable);
    }

    @Test
    public void getAllBookingsByIdByBookerPast() {
        BookingGetter getter = makeGetter(BOOKER, "PAST");
        Pageable pageable = makePageable();
        User user = makeUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        when(bookingRepository
                .findAllByBookerIdAndEndIsBeforeOrderByStartDesc(1L,
                        LocalDateTime.now().withNano(0),
                        pageable))
                .thenReturn(List.of());
        bookingService.getAllBookingsById(getter);
        verify(bookingRepository)
                .findAllByBookerIdAndEndIsBeforeOrderByStartDesc(1L,
                        LocalDateTime.now().withNano(0),
                        pageable);
    }

    @Test
    public void getAllBookingsByIdByBookerFuture() {
        BookingGetter getter = makeGetter(BOOKER, "FUTURE");
        Pageable pageable = makePageable();
        User user = makeUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository
                .findAllByBookerIdAndStartIsAfterAndStatusIsOrStatusIsOrderByStartDesc(1L,
                        LocalDateTime.now().withNano(0),
                        BookingStatus.APPROVED,
                        BookingStatus.WAITING,
                        pageable))
                .thenReturn(List.of());
        bookingService.getAllBookingsById(getter);
        verify(bookingRepository)
                .findAllByBookerIdAndStartIsAfterAndStatusIsOrStatusIsOrderByStartDesc(1L,
                        LocalDateTime.now().withNano(0),
                        BookingStatus.APPROVED,
                        BookingStatus.WAITING,
                        pageable);
    }

    @Test
    public void getAllBookingsByIdByBookerWaiting() {
        BookingGetter getter = makeGetter(BOOKER, "WAITING");
        Pageable pageable = makePageable();
        User user = makeUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        when(bookingRepository
                .findAllByBookerIdAndStatusIsOrderByStartDesc(1L,
                        BookingStatus.WAITING,
                        pageable))
                .thenReturn(List.of());
        bookingService.getAllBookingsById(getter);
        verify(bookingRepository)
                .findAllByBookerIdAndStatusIsOrderByStartDesc(1L,
                        BookingStatus.WAITING,
                        pageable);
    }

    @Test
    public void getAllBookingsByIdByBookerRejected() {
        BookingGetter getter = makeGetter(BOOKER, "REJECTED");
        Pageable pageable = makePageable();
        User user = makeUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository
                .findAllByBookerIdAndStatusIsOrderByStartDesc(1L,
                        BookingStatus.REJECTED,
                        pageable))
                .thenReturn(List.of());
        bookingService.getAllBookingsById(getter);
        verify(bookingRepository)
                .findAllByBookerIdAndStatusIsOrderByStartDesc(1L,
                        BookingStatus.REJECTED,
                        pageable);
    }

    private BookingGetter makeGetter(BookingGetterType type, String state) {
        return new BookingGetter()
                .setFrom(1)
                .setSize(1)
                .setType(type)
                .setState(state)
                .setUserId(1L);
    }
     private User makeUser() {
        return new User()
                .setId(1L)
                .setName("Name")
                .setEmail("ya@ya.ru");
     }

     private Pageable makePageable() {
        return PageRequest.of(1, 1);
     }
}


