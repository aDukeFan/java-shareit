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
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    public void getAllByRequesterIdTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        assertEquals(0, requestService.getAllByRequesterId(1L).size());
        verify(userRepository).findById(anyLong());
        verify(requestRepository).findAllByRequesterId(anyLong());
    }
}