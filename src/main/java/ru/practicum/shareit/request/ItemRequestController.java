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
import ru.practicum.shareit.request.dto.RequestDtoOutcome;
import ru.practicum.shareit.request.dto.RequestDtoIncome;
import ru.practicum.shareit.request.dto.RequestDtoWithItemList;
import ru.practicum.shareit.util.Constants;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    private RequestService service;

    //POST /requests — добавить новый запрос вещи.
    // Основная часть запроса — текст запроса, где пользователь описывает,
    // какая именно вещь ему нужна.
    @PostMapping
    public RequestDtoOutcome create(@RequestHeader(Constants.X_SHARER_USER_ID) long requesterId,
                                    @Valid @RequestBody RequestDtoIncome income) {
        return service.create(requesterId, income);
    }

    //GET /requests/{requestId} — получить данные об одном конкретном запросе вместе
    // с данными об ответах на него в том же формате,
    // что и в эндпоинте GET /requests.
    // Посмотреть данные об отдельном запросе может любой пользователь.

    @GetMapping("/{requestId}")
    public RequestDtoWithItemList getByRequestId(@RequestHeader(Constants.X_SHARER_USER_ID) long userId,
                                                 @PathVariable long requestId) {
        return service.getByRequestId(userId, requestId);
    }
    //GET /requests — получить список своих запросов вместе с данными об ответах на них.
    // Для каждого запроса должны указываться описание,
    // дата и время создания и список ответов в формате:
    // id вещи, название, её описание description,
    // а также requestId запроса и признак доступности вещи available.
    // Так в дальнейшем, используя указанные id вещей,
    // можно будет получить подробную информацию о каждой вещи.
    // Запросы должны возвращаться в отсортированном порядке от более новых к более старым.
    @GetMapping
    public List<RequestDtoWithItemList> getAllByRequesterId(
            @RequestHeader(Constants.X_SHARER_USER_ID) long requesterId) {
        return service.getAllByRequesterId(requesterId);
    }

    //GET /requests/all?from={from}&size={size} — получить список запросов,
    // созданных другими пользователями.
    // С помощью этого эндпоинта пользователи смогут просматривать существующие запросы,
    // на которые они могли бы ответить.
    // Запросы сортируются по дате создания:
    // от более новых к более старым. Результаты должны возвращаться постранично.
    // Для этого нужно передать два параметра: from — индекс первого элемента,
    // начиная с 0, и size — количество элементов для отображения.
    @GetMapping("/all")
    public List<RequestDtoWithItemList> getAllWithParams(@RequestHeader(Constants.X_SHARER_USER_ID) long userId,
                                                         @RequestParam int from,
                                                         @RequestParam int size) {
        return null;
    }


}
