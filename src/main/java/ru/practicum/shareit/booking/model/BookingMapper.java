package ru.practicum.shareit.booking.model;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class BookingMapper {

    public static Booking fromDto(Item item, User user, BookingDtoIn bookingDtoIn, Status status) {
        return new Booking(item,user, bookingDtoIn.getStart(), bookingDtoIn.getEnd(),status);
    }

    public static BookingDtoOut toDto(Booking booking) {
        return BookingDtoOut.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemMapper.toDtoBooking(booking.getItem()))
                .booker(UserMapper.toDtoBooking(booking.getBooker()))
                .status(booking.getStatus())
                .build();
    }

    public static List<BookingDtoOut> toDtoList(List<Booking> bookings) {
        List<BookingDtoOut> bookingsDtoOut = new ArrayList<>();
        for (Booking b : bookings) {
            bookingsDtoOut.add(BookingMapper.toDto(b));
        }
        return bookingsDtoOut;
    }

    public static BookingDtoForItem toDtoForItem(Booking booking) {
        if (booking != null) {
            return BookingDtoForItem.builder()
                    .id(booking.getId())
                    .bookerId(booking.getBooker().getId())
                    .build();
        } else {
            return null;
        }
    }
}