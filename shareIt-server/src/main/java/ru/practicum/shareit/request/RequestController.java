package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.RequestDtoIncome;
import ru.practicum.shareit.request.dto.RequestDtoOutcome;
import ru.practicum.shareit.request.dto.RequestDtoWithItemList;
import ru.practicum.shareit.util.Constants;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class RequestController {

    private RequestService service;

    @PostMapping
    public RequestDtoOutcome create(@RequestHeader(Constants.X_SHARER_USER_ID) long requesterId,
                                    @RequestBody RequestDtoIncome income) {
        return service.create(requesterId, income);
    }

    @GetMapping("/{requestId}")
    public RequestDtoWithItemList getByRequestId(@RequestHeader(Constants.X_SHARER_USER_ID) long userId,
                                                 @PathVariable long requestId) {
        return service.getByRequestId(userId, requestId);
    }

    @GetMapping
    public List<RequestDtoWithItemList> getAllByRequesterId(
            @RequestHeader(Constants.X_SHARER_USER_ID) long requesterId) {
        return service.getAllByRequesterId(requesterId);
    }

    @GetMapping("/all")
    public List<RequestDtoWithItemList> getAllWithParams(@RequestHeader(Constants.X_SHARER_USER_ID) long userId,
                                                         @RequestParam(required = false) Integer from,
                                                         @RequestParam(required = false) Integer size) {
        return service.getAllWithParams(userId, from, size);
    }
}
