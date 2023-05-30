package ru.practicum.shareit.booking;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.ItemDto;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    @PostMapping
    public BookingDto newBooking() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @PatchMapping("/{bookingId}")
    public BookingDto changeStatus(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                   @PathVariable Integer bookingId,
                                   //@RequestBody BookingDto bookingDto,
                                   @RequestParam(value = "approved") String approved) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable Integer bookingId) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @GetMapping
    public BookingDto getBookingUser(
            @RequestParam(value = "state", defaultValue = "ALL", required = false) String state) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @GetMapping ("/owner")
    public BookingDto getBookingOwner(
            @RequestParam(value = "state", defaultValue = "ALL", required = false) String state) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

}
