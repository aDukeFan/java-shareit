package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.user.dto.UserDto;

@Component
public class UserServiceClient {

    public final RestTemplate template;

    public UserServiceClient(@Value("${shareit-server.url}") String userServiceUrl, RestTemplateBuilder builder) {
        this.template = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(userServiceUrl + "/users"))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public UserDto create(UserDto userDto) {
        HttpEntity<UserDto> userDtoHttpEntity = new HttpEntity<>(userDto);
        return template.postForEntity("/", userDtoHttpEntity, UserDto.class).getBody();
    }

    public UserDto update(long id, UserDto userDto) {
        HttpEntity<UserDto> userDtoHttpEntity = new HttpEntity<>(userDto);
        return template.patchForObject("/" + id, userDtoHttpEntity, UserDto.class);
    }

    public void delete(long id) {
        template.delete("/" + id);
    }

    public UserDto getById(long userId) {
        return template.getForEntity("/" + userId, UserDto.class).getBody();
    }

    public ResponseEntity<Object> getAll() {
        return template.getForEntity("/", Object.class);
    }
}
