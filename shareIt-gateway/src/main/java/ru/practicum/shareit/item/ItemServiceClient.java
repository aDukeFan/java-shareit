package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.item.dto.CommentDtoIncome;
import ru.practicum.shareit.item.dto.CommentDtoOutcome;
import ru.practicum.shareit.item.dto.ItemDtoIncome;
import ru.practicum.shareit.item.dto.ItemDtoOutcomeAvailableRequest;
import ru.practicum.shareit.item.dto.ItemDtoOutcomeLong;
import ru.practicum.shareit.util.Constants;

import java.util.List;
import java.util.Map;

@Component
public class ItemServiceClient {

    public final RestTemplate template;

    public ItemServiceClient(@Value("${shareit-server.url}") String userServiceUrl, RestTemplateBuilder builder) {
        this.template = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(userServiceUrl + "/items"))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public ItemDtoOutcomeAvailableRequest create(long userId, ItemDtoIncome itemDtoIncome) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(Constants.X_SHARER_USER_ID, String.valueOf(userId));
        HttpEntity<ItemDtoIncome> entity = new HttpEntity<>(itemDtoIncome, headers);
        return template.postForEntity("/", entity, ItemDtoOutcomeAvailableRequest.class).getBody();
    }

    public ItemDtoOutcomeAvailableRequest update(long id, long userId, ItemDtoIncome itemDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(Constants.X_SHARER_USER_ID, String.valueOf(userId));
        HttpEntity<ItemDtoIncome> entity = new HttpEntity<>(itemDto, headers);
        String url = "/" + id;
        return template.patchForObject(url, entity, ItemDtoOutcomeAvailableRequest.class);
    }

    public List<ItemDtoOutcomeLong> getAllItemsByOwner(long userId, Integer from, Integer size) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(Constants.X_SHARER_USER_ID, String.valueOf(userId));
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size);
        String url = "/";
        return template.exchange(url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<ItemDtoOutcomeLong>>() {
                }, parameters).getBody();
    }

    public List<ItemDtoOutcomeAvailableRequest> findByQuery(String text, Integer from, Integer size) {
        String url = "/search/?text={text}&from={from}&size={size}";
        Map<String, String> parameters = Map.of(
                "text", text,
                "from", from.toString(),
                "size", size.toString());
        return template.exchange(url,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<List<ItemDtoOutcomeAvailableRequest>>() {},
                        parameters)
                .getBody();
    }

    public ItemDtoOutcomeLong getItemById(long itemId, long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(Constants.X_SHARER_USER_ID, String.valueOf(userId));
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        String url = "/" + itemId;
        return template.exchange(url, HttpMethod.GET, entity, ItemDtoOutcomeLong.class).getBody();
    }

    public CommentDtoOutcome addComment(long userId, long itemId, CommentDtoIncome commentDtoIncome) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(Constants.X_SHARER_USER_ID, String.valueOf(userId));
        HttpEntity<CommentDtoIncome> entity = new HttpEntity<>(commentDtoIncome, headers);
        String url = "/" + itemId + "/comment";
        return template.postForEntity(url, entity, CommentDtoOutcome.class).getBody();
    }
}
