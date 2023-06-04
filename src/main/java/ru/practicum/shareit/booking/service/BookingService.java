
package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto create(Long userId, BookingDto bookingDto);

    BookingDto changeStatus(Long userId, Long bookingId, String approved);

    BookingDto get(Long id);

    List<BookingDto> getBookingUser(Long userId, String state);

    List<BookingDto> getBookingOwner(Long ownerId, String state);

}