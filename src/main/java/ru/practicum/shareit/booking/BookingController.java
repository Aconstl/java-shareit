package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.ItemDto;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto newBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @Valid @RequestBody BookingDto bookingDto) {
        return bookingService.create(userId,bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto changeStatus(@RequestHeader("X-Sharer-User-Id") Long ownerId,
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
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(value = "state", defaultValue = "ALL", required = false) String state) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @GetMapping ("/owner")
    public BookingDto getBookingOwner(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(value = "state", defaultValue = "ALL", required = false) String state) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

}
