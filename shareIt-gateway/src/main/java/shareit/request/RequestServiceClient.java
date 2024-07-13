package shareit.request;

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
import shareit.request.dto.RequestDtoIncome;
import shareit.request.dto.RequestDtoOutcome;
import shareit.request.dto.RequestDtoWithItemList;
import shareit.util.Constants;

import java.util.List;
import java.util.Map;

@Component
public class RequestServiceClient {

    public final RestTemplate template;

    public RequestServiceClient(@Value("${user-service.url}") String userServiceUrl, RestTemplateBuilder builder) {
        this.template = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(userServiceUrl + "/requests"))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public RequestDtoOutcome create(long requesterId, RequestDtoIncome income) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(Constants.X_SHARER_USER_ID, String.valueOf(requesterId));
        HttpEntity<RequestDtoIncome> entity = new HttpEntity<>(income, headers);
        return template.postForEntity("/", entity, RequestDtoOutcome.class).getBody();
    }

    public List<RequestDtoWithItemList> getAllByRequesterId(long requesterId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(Constants.X_SHARER_USER_ID, String.valueOf(requesterId));
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        return template.exchange("/",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<RequestDtoWithItemList>>() {
        }).getBody();
    }

    public RequestDtoWithItemList getByRequestId(long userId, long requestId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(Constants.X_SHARER_USER_ID, String.valueOf(userId));
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        String url = "/" + requestId;
        return template.exchange(url,
                        HttpMethod.GET,
                        entity,
                        RequestDtoWithItemList.class)
                .getBody();
    }

    public List<RequestDtoWithItemList> getAllWithParams(long userId, Integer from, Integer size) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(Constants.X_SHARER_USER_ID, String.valueOf(userId));
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size);
        return template.exchange("/all", 
                        HttpMethod.GET, entity, 
                        new ParameterizedTypeReference<List<RequestDtoWithItemList>>() {}, 
                        parameters)
                .getBody();
    }
}
