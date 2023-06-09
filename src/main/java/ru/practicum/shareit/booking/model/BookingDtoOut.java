package ru.practicum.shareit.booking.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.ItemDtoForBooking;
import ru.practicum.shareit.user.model.UserDtoForBooking;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDtoOut {


    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private ItemDtoForBooking item;

    private UserDtoForBooking booker;

    private Status status;
}
