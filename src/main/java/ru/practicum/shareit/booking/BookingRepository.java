package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByItemId(long itemId);

    List<Booking> findAllByBookerIdAndItemId(long bookerId, long itemId);

    //for add comment available
    List<Booking> findAllByBookerIdAndItemIdAndStartIsBeforeAndStatusIsOrStatusIs(long bookerId,
                                                                                  long itemId,
                                                                                  LocalDateTime now,
                                                                                  BookingStatus first,
                                                                                  BookingStatus second);

    //all
    List<Booking> findAllByItemOwnerIdOrderByStartDesc(long ownerId, Pageable pageable);

    List<Booking> findAllByBookerIdOrderByStartDesc(long bookerId, Pageable pageable);

    //current
    List<Booking> findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(long ownerId,
                                                                                    LocalDateTime first,
                                                                                    LocalDateTime second,
                                                                                    Pageable pageable);

    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(long bookerId,
                                                                                 LocalDateTime first,
                                                                                 LocalDateTime second,
                                                                                 Pageable pageable);

    //past
    List<Booking> findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(long ownerId,
                                                                     LocalDateTime first,
                                                                     Pageable pageable);

    List<Booking> findAllByBookerIdAndEndIsBeforeOrderByStartDesc(long bookerId,
                                                                  LocalDateTime first,
                                                                  Pageable pageable);

    //future
    List<Booking> findAllByItemOwnerIdAndStartIsAfterAndStatusIsOrStatusIsOrderByStartDesc(long ownerId,
                                                                                           LocalDateTime first,
                                                                                           BookingStatus status,
                                                                                           BookingStatus statusNext,
                                                                                           Pageable pageable);

    List<Booking> findAllByBookerIdAndStartIsAfterAndStatusIsOrStatusIsOrderByStartDesc(long bookerId,
                                                                                        LocalDateTime first,
                                                                                        BookingStatus status,
                                                                                        BookingStatus statusNext,
                                                                                        Pageable pageable);

    //waiting // rejected
    List<Booking> findAllByItemOwnerIdAndStatusIsOrderByStartDesc(long ownerId,
                                                                  BookingStatus status,
                                                                  Pageable pageable);

    List<Booking> findAllByBookerIdAndStatusIsOrderByStartDesc(long bookerId,
                                                               BookingStatus status,
                                                               Pageable pageable);
}
