package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import ru.practicum.shareit.booking.model.BookingDtoIn;
import ru.practicum.shareit.booking.model.BookingDtoOut;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDtoOut newBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @Valid @RequestBody BookingDtoIn bookingDtoIn) {
        return BookingMapper.toDto(bookingService.create(userId,bookingDtoIn));
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut changeStatus(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                     @PathVariable Long bookingId,
                                     @RequestParam(value = "approved") Boolean approved) {
        return BookingMapper.toDto(bookingService.changeStatus(ownerId,bookingId,approved));
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOut getBooking(@PathVariable Long bookingId,
                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        return BookingMapper.toDto(bookingService.get(bookingId, userId));
    }

    @GetMapping
    public List<BookingDtoOut> getBookingUser(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(value = "state", defaultValue = "ALL") String state,
            @RequestParam(value = "from", required = false) Long from,
            @RequestParam(value = "size", required = false) Long size) {
        return BookingMapper.toDtoList(bookingService.getBookingUser(userId,state,from,size));
    }

    @GetMapping ("/owner")
    public List<BookingDtoOut> getBookingOwner(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(value = "state", defaultValue = "ALL", required = false) String state,
            @RequestParam(value = "from", required = false) Long from,
            @RequestParam(value = "size", required = false) Long size) {
        return BookingMapper.toDtoList(bookingService.getBookingOwner(ownerId,state,from,size));
    }

}
