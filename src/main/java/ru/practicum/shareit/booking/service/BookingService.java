
package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDtoIn;

import java.util.List;

public interface BookingService {

    Booking create(Long userId, BookingDtoIn bookingDtoIn);

    Booking changeStatus(Long userId, Long bookingId, Boolean approved);

    Booking get(Long bookingId,Long userId);

    List<Booking> getBookingUser(Long userId, String state, Long from, Long size);

    List<Booking> getBookingOwner(Long ownerId, String state, Long from, Long size);

}