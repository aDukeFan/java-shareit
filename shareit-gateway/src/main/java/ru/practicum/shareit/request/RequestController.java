package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
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

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
@Validated
public class RequestController {

    private RequestServiceClient client;

    @PostMapping
    public RequestDtoOutcome create(@RequestHeader(Constants.X_SHARER_USER_ID) long requesterId,
                                    @Valid @RequestBody RequestDtoIncome income) {
        return client.create(requesterId, income);
    }

    @GetMapping("/{requestId}")
    public RequestDtoWithItemList getByRequestId(@RequestHeader(Constants.X_SHARER_USER_ID) long userId,
                                                 @PathVariable long requestId) {
        return client.getByRequestId(userId, requestId);
    }

    @GetMapping
    public List<RequestDtoWithItemList> getAllByRequesterId(
            @RequestHeader(Constants.X_SHARER_USER_ID) long requesterId) {
        return client.getAllByRequesterId(requesterId);
    }

    @GetMapping("/all")
    public List<RequestDtoWithItemList> getAllWithParams(@RequestHeader(Constants.X_SHARER_USER_ID) long userId,
                                                         @RequestParam(required = false) @Min(0) Integer from,
                                                         @RequestParam(required = false) @Min(1) Integer size) {
        return client.getAllWithParams(userId, from, size);
    }
}
