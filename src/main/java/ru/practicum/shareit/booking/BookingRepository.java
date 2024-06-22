package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByItemId(long itemId);

    List<Booking> findAllByBookerIdOrderByStartDesc(long bookerId);

    List<Booking> findAllByBookerIdAndItemId(long bookerId, long itemId);

    @Query(value = "SELECT " +
            "b.id AS id, " +
            "b.rent_start AS rent_start, " +
            "b.rent_end AS rent_end, " +
            "b.item_id  AS item_id, " +
            "b.booker_id AS booker_id, " +
            "b.status  AS status " +
            "FROM bookings AS b JOIN items AS i ON i.id = b.item_id " +
            "WHERE i.owner_id = ?1 " +
            "ORDER BY b.rent_start DESC", nativeQuery = true)
    List<Booking> findAllByItemOwnerId(long ownerId);

}
