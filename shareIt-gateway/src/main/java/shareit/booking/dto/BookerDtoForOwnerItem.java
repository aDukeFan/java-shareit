package shareit.booking.dto;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
@Setter
@Getter
@EqualsAndHashCode
public class BookerDtoForOwnerItem {

    long id;
    long bookerId;
    String bookerName;
    LocalDateTime start;
    LocalDateTime end;
    BookingStatus status;
}
