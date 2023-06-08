package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingDtoForItem;
import ru.practicum.shareit.item.comment.model.CommentDtoOut;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class ItemDtoWithBooking {

    private Long id;
    @NotBlank
    private String name;    // имя вещи
    @NotBlank
    private String description;     //описание вещи
    @NotNull
    private Boolean available;  // статус о том, доступна ли вещь для аренды

    private BookingDtoForItem lastBooking;
    private BookingDtoForItem nextBooking;

    private List<CommentDtoOut> comments;

}
