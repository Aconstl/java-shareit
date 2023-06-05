package ru.practicum.shareit.booking.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepositoryInDb extends JpaRepository<Booking,Long> {
    List<Booking> findAllByBookerIdOrderByIdDesc(Long id);

    List<Booking> findAllByBookerIdAndStatusOrderByIdDesc(Long id,String status);

    @Query(value = "select * " +
            "From bookings " +
            "Where booker_id = :id " +
            "AND start_date < :now AND end_date > :now " +
            "order by booking_id desc", nativeQuery = true)
    List<Booking> findBookingUserCurrent(@Param("id") Long id,
                                        @Param("now") LocalDateTime now);

    @Query(value = "select * " +
            "From bookings " +
            "Where booker_id = :id " +
            "AND start_date > :now " +
            "order by booking_id desc", nativeQuery = true)
    List<Booking> findBookingUserFuture(@Param("id") Long id,
                                         @Param("now") LocalDateTime now);

    @Query(value = "select * " +
            "From bookings " +
            "Where booker_id = :id " +
            "AND end_date < :now " +
            "order by booking_id desc", nativeQuery = true)
    List<Booking> findBookingUserPast(@Param("id") Long id,
                                      @Param("now") LocalDateTime now);

    @Query(value = "select b.* " +
            "From bookings b " +
            "Where booker_id = :id " +
            "AND b.status like 'WAITING' " +
            "order by booking_id desc", nativeQuery = true)
    List<Booking> findBookingUserStatusWaiting(@Param("id") Long id);

    @Query(value = "select b.* " +
            "From bookings b " +
            "Where booker_id = :id " +
            "AND b.status like 'REJECTED' " +
            "order by booking_id desc", nativeQuery = true)
    List<Booking> findBookingUserStatusRejected(@Param("id") Long id);

    @Query(value = "select b.* " +
            "From bookings b " +
            "left join items i on i.item_id = b.item_id " +
            "Where i.user_id = :id " +
            "order by booking_id desc", nativeQuery = true)
    List<Booking> findBookingOwnerAll(@Param("id") Long id);

    @Query(value = "select b.* " +
            "From bookings b " +
            "left join items i on i.item_id = b.item_id " +
            "Where i.user_id = :id " +
            "AND start_date < :now AND end_date > :now " +
            "order by booking_id desc", nativeQuery = true)
    List<Booking> findBookingOwnerCurrent(@Param("id") Long id,
                                          @Param("now") LocalDateTime now);

    @Query(value = "select b.* " +
            "From bookings b " +
            "left join items i on i.item_id = b.item_id " +
            "Where i.user_id = :id " +
            "AND start_date > :now " +
            "order by booking_id desc", nativeQuery = true)
    List<Booking> findBookingOwnerFuture(@Param("id") Long id,
                                         @Param("now") LocalDateTime now);

    @Query(value = "select b.* " +
            "From bookings b " +
            "left join items i on i.item_id = b.item_id " +
            "Where i.user_id = :id " +
            "AND end_date < :now " +
            "order by booking_id desc", nativeQuery = true)
    List<Booking> findBookingOwnerPast(@Param("id") Long id,
                                       @Param("now") LocalDateTime now);

    @Query(value = "select b.* " +
            "From bookings b " +
            "left join items i on i.item_id = b.item_id " +
            "Where i.user_id = :id " +
            "AND b.status like 'WAITING' " +
            "order by booking_id desc", nativeQuery = true)
    List<Booking> findBookingOwnerStatusWaiting(@Param("id") Long id);

    @Query(value = "select b.* " +
            "From bookings b " +
            "left join items i on i.item_id = b.item_id " +
            "Where i.user_id = :id " +
            "AND b.status like 'REJECTED' " +
            "order by booking_id desc", nativeQuery = true)
    List<Booking> findBookingOwnerStatusRejected(@Param("id") Long id);

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

    @Query (value = "select b.booker_id " +
            "from bookings b " +
            "where b.booking_id = :booking_id", nativeQuery = true)
    Long getIdBooker(@Param("booking_id") Long id);

    @Query (value = "select b.status " +
            "From bookings b " +
            "Where b.booking_id = :booking_id", nativeQuery = true)
    Status getStatusBooker(@Param("booking_id") Long id);

    @Query (value = "select * " +
            "From bookings " +
            "Where item_id  = :idItem " +
            "AND end_date < now() " +
            "limit 1" , nativeQuery = true)
    Booking getLastBooking(@Param("idItem") Long id);

    @Query (value = "select * " +
            "From bookings " +
            "Where item_id  = :idItem " +
            "AND start_date > :time " +
            "order by start_date asc " +
            "limit 1" , nativeQuery = true)
    Booking getNextBooking(@Param("idItem") Long id,
                           @Param("time") LocalDateTime time);
}
