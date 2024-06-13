package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByItemId(long itemId);

    List<Booking> findAllByBookerId(long bookerId);

    Optional<Booking> findByItemIdAndBookerId(long itemId, long bookerId);
}
