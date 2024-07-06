package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemMapperImpl;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDtoIncome;
import ru.practicum.shareit.request.dto.RequestDtoWithItemList;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestServiceImplTest {

    private RequestService requestService;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    private final RequestMapper requestMapper = new RequestMapperImpl();

    private final ItemMapper itemMapper = new ItemMapperImpl();

    @BeforeEach
    public void setUp() {
        requestService = new RequestServiceImpl(
                requestRepository,
                userRepository,
                itemRepository,
                requestMapper,
                itemMapper);
    }

    @Test
    public void createBadUserIdTest() {
        assertThrows(NotFoundException.class,
                () -> requestService.create(1, new RequestDtoIncome()));
        verify(userRepository).findById(anyLong());
    }

    @Test
    public void getByRequestIdTest() {
        User user = new User()
                .setId(1L)
                .setName("Name")
                .setEmail("ya@ya.ru");
        Request firstRequest = new Request()
                .setRequester(user)
                .setCreated(LocalDateTime.now())
                .setDescription("some needs")
                .setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(requestRepository.findById(1L)).thenReturn(Optional.of(firstRequest));
        RequestDtoWithItemList expected = requestMapper.toSendAll(firstRequest);
        assertEquals(expected, requestService.getByRequestId(1L, 1L));
        verify(userRepository).findById(anyLong());
        verify(requestRepository).findById(anyLong());
    }

    @Test
    public void getAllByRequesterIdEmptyTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        assertEquals(0, requestService.getAllByRequesterId(1L).size());
        verify(userRepository).findById(anyLong());
        verify(requestRepository).findAllByRequesterId(anyLong());
    }

    @Test
    public void getAllByRequesterIdTest() {
        User user = new User()
                .setId(1L)
                .setName("Name")
                .setEmail("ya@ya.ru");
        Request firstRequest = new Request()
                .setRequester(user)
                .setCreated(LocalDateTime.now())
                .setDescription("some needs");
        Request second = new Request()
                .setRequester(user)
                .setCreated(LocalDateTime.now().minusDays(1))
                .setDescription("another needs");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(requestRepository.findAllByRequesterId(1L)).thenReturn(List.of(second, firstRequest));
        List<RequestDtoWithItemList> result = requestService.getAllByRequesterId(1L);
        assertEquals(2, result.size());
        assertEquals(requestMapper.toSend(second).getDescription(),
                result.get(1).getDescription());
        verify(userRepository).findById(1L);
        verify(requestRepository).findAllByRequesterId(anyLong());
    }
}