package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
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

    //all
    Page<Booking> findAllByItemOwnerIdOrderByStartDesc(long ownerId,
                                                       Pageable pageable);

    Page<Booking> findAllByBookerIdOrderByStartDesc(long bookerId,
                                                    Pageable pageable);

    //current
    Page<Booking> findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(long ownerId,
                                                                                    LocalDateTime first,
                                                                                    LocalDateTime second,
                                                                                    Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(long bookerId,
                                                                                 LocalDateTime first,
                                                                                 LocalDateTime second,
                                                                                 Pageable pageable);
    //past
    Page<Booking> findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(long ownerId,
                                                                     LocalDateTime first,
                                                                     Pageable pageable);

    Page<Booking> findAllByBookerIdAndEndIsBeforeOrderByStartDesc(long bookerId,
                                                                  LocalDateTime first,
                                                                  Pageable pageable);
    //future
    Page<Booking> findAllByItemOwnerIdAndStartIsAfterOrderByStartDesc(long ownerId,
                                                                      LocalDateTime first,
                                                                      Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartIsAfterOrderByStartDesc(long bookerId,
                                                                   LocalDateTime first,
                                                                   Pageable pageable);

    //waiting // rejected
    Page<Booking> findAllByItemOwnerIdAndStatusIsOrderByStartDesc(long ownerId,
                                                                  BookingStatus status,
                                                                  Pageable pageable);

    Page<Booking> findAllByBookerIdAndStatusIsOrderByStartDesc(long bookerId,
                                                               BookingStatus status,
                                                               Pageable pageable);



}
