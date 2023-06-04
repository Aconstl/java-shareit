
package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDtoIn;

import java.util.List;

public interface BookingService {

    Booking create(Long userId, BookingDtoIn bookingDtoIn);

    Booking changeStatus(Long userId, Long bookingId, Boolean approved);

    Booking get(Long id);

    List<Booking> getBookingUser(Long userId, String state);

    List<Booking> getBookingOwner(Long ownerId, String state);

}