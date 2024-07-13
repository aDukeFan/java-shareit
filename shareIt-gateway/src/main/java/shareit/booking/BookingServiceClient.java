package shareit.booking;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import shareit.booking.booking_getter.model.BookingGetter;
import shareit.booking.dto.BookingDtoIncome;
import shareit.booking.dto.BookingDtoOutcomeLong;
import shareit.util.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BookingServiceClient {

    public final RestTemplate template;

    public BookingServiceClient(@Value("${user-service.url}") String userServiceUrl, RestTemplateBuilder builder) {
        this.template = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(userServiceUrl + "/bookings"))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public BookingDtoOutcomeLong createBooking(long bookerId, BookingDtoIncome bookingDtoIncome) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(Constants.X_SHARER_USER_ID, String.valueOf(bookerId));
        String url = "/";
        HttpEntity<BookingDtoIncome> entity = new HttpEntity<>(bookingDtoIncome, headers);
        return template.postForEntity(url, entity, BookingDtoOutcomeLong.class).getBody();
    }

    public BookingDtoOutcomeLong approveBooking(long ownerId, long bookingId, Boolean approved) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(Constants.X_SHARER_USER_ID, String.valueOf(ownerId));
        String url = "/" + bookingId + "?approved={approved}";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("approved", approved);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        return template.exchange(url, HttpMethod.PATCH, entity, BookingDtoOutcomeLong.class, parameters).getBody();
    }

    public BookingDtoOutcomeLong getBooking(long ownerOrClientId, long bookingId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(Constants.X_SHARER_USER_ID, String.valueOf(ownerOrClientId));
        String url = "/" + bookingId;
        HttpEntity<BookingDtoIncome> entity = new HttpEntity<>(headers);
        return template.exchange(url, HttpMethod.GET, entity, BookingDtoOutcomeLong.class).getBody();
    }

    public List<BookingDtoOutcomeLong> getAllBookingsById(BookingGetter getter) {
        return null;
    }
}
