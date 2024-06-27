package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDtoOutcomeAvailableRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDtoIncome;
import ru.practicum.shareit.request.dto.RequestDtoOutcome;
import ru.practicum.shareit.request.dto.RequestDtoWithItemList;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.Constants;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {

    private RequestRepository requestRepository;
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private RequestMapper requestMapper;
    private ItemMapper itemMapper;

    @Override
    public RequestDtoOutcome create(long requesterId, RequestDtoIncome income) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_USER_WITH_SUCH_ID + requesterId));
        Request request = requestMapper.toSave(income).setRequester(requester);
        return requestMapper.toSend(requestRepository.save(request));
    }

    @Override
    public List<RequestDtoWithItemList> getAllByRequesterId(long requesterId) {
        userRepository.findById(requesterId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_USER_WITH_SUCH_ID + requesterId));
        List<Request> requests = requestRepository.findAllByRequesterId(requesterId);
        if (requests.isEmpty()) {
            return List.of();
        }
        List<RequestDtoWithItemList> requestDtoWithItemLists = requests.stream()
                .map(request -> requestMapper.toSendAll(request))
                .sorted(Comparator.comparing(RequestDtoWithItemList::getCreated).reversed())
                .collect(Collectors.toList());
        requestDtoWithItemLists.forEach(this::setItemsDto);
        return requestDtoWithItemLists;

    }

    @Override
    public RequestDtoWithItemList getByRequestId(long userId, long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.NO_USER_WITH_SUCH_ID + userId));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("No request with Id " + requestId));
        RequestDtoWithItemList requestDtoWithItemList = requestMapper.toSendAll(request);
        return setItemsDto(requestDtoWithItemList);
    }

    private RequestDtoWithItemList setItemsDto(RequestDtoWithItemList requestDtoWithItemList) {
        List<Item> items = itemRepository.findByRequestId(requestDtoWithItemList.getId());
        List<ItemDtoOutcomeAvailableRequest> itemDtoForRequests = items.stream()
                .filter(Item::getAvailable)
                .sorted(Comparator.comparing(Item::getId))
                .map(item -> itemMapper.toSend(item))
                .collect(Collectors.toList());
        requestDtoWithItemList.getItems().addAll(itemDtoForRequests);
        return requestDtoWithItemList;
    }
}
