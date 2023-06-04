package ru.practicum.shareit.booking.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

public interface BookingRepositoryInDb extends JpaRepository<Booking,Long> {
    @Modifying
    @Query(value = "UPDATE PUBLIC.BOOKINGS " +
            "SET status = :status " +
            "WHERE booking_id = :booking_id", nativeQuery = true)
    void changeStatus(@Param("booking_id") Long id,
            @Param("status") String status);


    @Query (value = "select user_id " +
            "from items i " +
            "LEFT JOIN bookings b ON b.item_id  = i.item_id " +
            "where b.booking_id = :booking_id", nativeQuery = true)
    Long getIdOwnerItem(@Param("booking_id") Long id);
}
