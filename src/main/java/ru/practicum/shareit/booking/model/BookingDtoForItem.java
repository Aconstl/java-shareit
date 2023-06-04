package ru.practicum.shareit.booking.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingDtoForItem {

    Long id;
    Long booker_id;
}
