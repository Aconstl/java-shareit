package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {

    public static Booking fromDto(Item item, User user, BookingDto bookingDto) {
        return new Booking(item,user,bookingDto.getStart(),bookingDto.getEnd());
    }

}
