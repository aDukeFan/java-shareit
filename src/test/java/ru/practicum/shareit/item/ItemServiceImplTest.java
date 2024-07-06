package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

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
    void updateTestWithException() {
        assertThrows(NotFoundException.class,
                () -> itemService.update(1L, 1L, new ItemDtoIncome()));
        verify(itemRepository).findById(anyLong());
    }

    @Test
    void getAllItemsByOwner() {
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
                .thenReturn(new PageImpl<>(List.of(item), pageable, 1));
        List<ItemDtoOutcomeLong> expectList = List.of(itemMapper.toGetById(item));
        List<ItemDtoOutcomeLong> outcomeList = itemService.getAllItemsByOwner(1L, 1, 1);
        assertEquals(outcomeList, expectList);
    }

    @Test
    void findByQuery() {
        assertEquals(0, itemService.findByQuery("", 0, 10).size());
    }

    @Test
    void getItemByIdWithException() {
        assertThrows(NotFoundException.class, () -> itemService.getItemById(1L, 1L));
    }

    @Test
    void addCommentWithNoBookings() {
        CommentDtoIncome income = new CommentDtoIncome().setText("text");
        assertThrows(BadRequestException.class, () -> itemService.addComment(1L, 1L, income));
    }

    @Test
    void addComment() {
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

}