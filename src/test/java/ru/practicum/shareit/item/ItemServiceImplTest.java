package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingMapperImpl;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDtoIncome;
import ru.practicum.shareit.item.dto.CommentDtoOutcome;
import ru.practicum.shareit.item.dto.ItemDtoIncome;
import ru.practicum.shareit.item.dto.ItemDtoOutcomeAvailableRequest;
import ru.practicum.shareit.item.dto.ItemDtoOutcomeLong;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private BookingRepository bookingRepository;

    private final ItemMapper itemMapper = new ItemMapperImpl();

    private final CommentMapper commentMapper = new CommentMapperImpl();

    private final BookingMapper bookingMapper = new BookingMapperImpl();

    @BeforeEach
    public void setUp() {
        itemService = new ItemServiceImpl(
                itemRepository,
                userRepository,
                commentRepository,
                requestRepository,
                bookingRepository,
                commentMapper,
                itemMapper,
                bookingMapper);
    }

    @Test
    public void createTestWithException() {
        assertThrows(NotFoundException.class,
                () -> itemService.create(2, new ItemDtoIncome()));
    }

    @Test
    public void createTest() {
        ItemDtoIncome itemDtoIncome = new ItemDtoIncome()
                .setName("ItemName")
                .setDescription("ItemDesc")
                .setAvailable(true);
        Item item = itemMapper.toSave(itemDtoIncome);
        User user = new User()
                .setId(1L)
                .setEmail("ya@ya.ru")
                .setName("User Name");
        Item savedItem = item
                .setId(1L)
                .setOwner(user);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class)))
                .thenReturn(savedItem);

        assertEquals(itemMapper.toSend(savedItem),
                itemService.create(1, itemDtoIncome));
        verify(userRepository).findById(anyLong());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    public void createBadRequestIdTest() {
        ItemDtoIncome itemDtoIncome = new ItemDtoIncome()
                .setName("ItemName")
                .setDescription("ItemDesc")
                .setAvailable(true)
                .setRequestId(1L);
        User user = new User()
                .setId(1L)
                .setEmail("ya@ya.ru")
                .setName("User Name");
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(requestRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> itemService.create(1, itemDtoIncome));
    }

    @Test
    public void updateTestWithNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> itemService.update(1L, 1L, new ItemDtoIncome()));
        verify(itemRepository).findById(anyLong());
    }

    @Test
    public void updateTestWithBadOwner() {
        Item item = new Item()
                .setAvailable(true)
                .setDescription("some")
                .setId(1L)
                .setOwner(new User().setId(3))
                .setRequestId(1L);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        assertThrows(NotFoundException.class,
                () -> itemService.update(1L, 1L, new ItemDtoIncome()));
        verify(itemRepository).findById(anyLong());
    }

    @Test
    public void updateTest() {
        Item item = new Item()
                .setAvailable(true)
                .setName("name")
                .setDescription("some")
                .setId(1L)
                .setOwner(new User().setId(1L))
                .setRequestId(1L);
        ItemDtoIncome income = new ItemDtoIncome()
                .setAvailable(true)
                .setDescription("some")
                .setName("name")
                .setRequestId(1L);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);
        ItemDtoOutcomeAvailableRequest outcome = itemService
                .update(1L, 1L, income);
        assertEquals(income.getName(), outcome.getName());
        verify(itemRepository).findById(anyLong());
        verify(itemRepository).save(item);
    }

    @Test
    public void getAllItemsByOwner() {
        User user = new User()
                .setId(1L)
                .setEmail("ya@ya.ru")
                .setName("User Name");
        Item item = new Item()
                .setId(1L)
                .setName("name")
                .setDescription("Description")
                .setAvailable(true)
                .setOwner(user);
        Pageable pageable = PageRequest.of(1, 1);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.findByOwnerId(1L, pageable))
                .thenReturn(List.of(item));
        List<ItemDtoOutcomeLong> expectList = List.of(itemMapper.toGetById(item));
        List<ItemDtoOutcomeLong> outcomeList = itemService.getAllItemsByOwner(1L, 1, 1);
        assertEquals(outcomeList, expectList);
    }

    @Test
    public void findByQueryNoTextTest() {
        assertEquals(0, itemService.findByQuery("", 0, 10).size());
    }

    @Test
    public void findByQueryWithText() {
        when(itemRepository
                .findAllByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContainingAndAvailableTrue(
                        anyString(),
                        anyString(), any(Pageable.class))).thenReturn(List.of());
        assertEquals(0, itemService.findByQuery("some text", 0, 10).size());
        verify(itemRepository)
                .findAllByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContainingAndAvailableTrue(
                        anyString(), anyString(),
                        any(Pageable.class));
    }

    @Test
    public void getItemByIdWithException() {
        assertThrows(NotFoundException.class, () -> itemService.getItemById(1L, 1L));
    }

    @Test
    public void addCommentWithNoBookings() {
        CommentDtoIncome income = new CommentDtoIncome().setText("text");
        assertThrows(BadRequestException.class, () -> itemService.addComment(1L, 1L, income));
    }

    @Test
    public void addComment() {
        CommentDtoIncome income = new CommentDtoIncome()
                .setText("text");
        when(bookingRepository.findAllByBookerIdAndItemIdAndStartIsBeforeAndStatusIsOrStatusIs(1L, 2L,
                LocalDateTime.now().withNano(0),
                BookingStatus.APPROVED,
                BookingStatus.CANCELED))
                .thenReturn(List.of(new Booking()));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(new Item()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User().setName("name")));

        Comment comment = new Comment()
                .setItem(new Item().setId(2L))
                .setCreatedTime(LocalDateTime.now().withNano(0))
                .setAuthor(new User().setName("name"))
                .setId(1L)
                .setText("text");
        CommentDtoOutcome expected = commentMapper.toSend(comment);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        assertEquals(expected, itemService.addComment(1L, 2L, income));

        verify(commentRepository).save(any(Comment.class));
        verify(itemRepository).findById(anyLong());
        verify(userRepository).findById(anyLong());
        verify(bookingRepository).findAllByBookerIdAndItemIdAndStartIsBeforeAndStatusIsOrStatusIs(1L, 2L,
                LocalDateTime.now().withNano(0),
                BookingStatus.APPROVED,
                BookingStatus.CANCELED);
    }

    @Test
    public void addCommendExceptionNoBookingsTest() {
        long userId = 1L;
        long itemId = 1L;
        CommentDtoIncome income = new CommentDtoIncome()
                .setText("text");
        assertThrows(BadRequestException.class,
                () -> itemService.addComment(userId, itemId, income));
    }

    @Test
    public void addCommendExceptionBadOwnerTest() {
        long userId = 1L;
        long itemId = 1L;
        when(bookingRepository.findAllByBookerIdAndItemIdAndStartIsBeforeAndStatusIsOrStatusIs(userId, itemId,
                LocalDateTime.now().withNano(0),
                BookingStatus.APPROVED,
                BookingStatus.CANCELED)).thenReturn(List.of(new Booking()));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(new Item()));
        CommentDtoIncome income = new CommentDtoIncome()
                .setText("text");
        assertThrows(NotFoundException.class,
                () -> itemService.addComment(userId, itemId, income));
    }
}