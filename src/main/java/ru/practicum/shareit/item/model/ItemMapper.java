package ru.practicum.shareit.item.model;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.BookingDtoForItem;
import ru.practicum.shareit.item.comment.model.CommentDtoOut;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public  class ItemMapper {
    public static ItemDto toDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
                .build();
    }

    public static ItemDtoWithBooking toDtoWithBooking(Item item, BookingDtoForItem lastBooking,
                                                      BookingDtoForItem nextBooking, List<CommentDtoOut> comments) {
        return ItemDtoWithBooking.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments)
                .build();
    }

    public static Item fromDto(ItemDto itemDto) {
        return new Item(itemDto.getId(),itemDto.getName(),itemDto.getDescription(),itemDto.getAvailable());
    }

    public static List<ItemDto> toListDto(List<Item> items) {
        List<ItemDto> itemsDto = new ArrayList<>();
        if (items != null) {
            for (Item i : items) {
                itemsDto.add(ItemMapper.toDto(i));
            }
        }
            return itemsDto;
    }


    public static ItemDtoForBooking toDtoBooking(Item item) {
        return ItemDtoForBooking.builder()
                .id(item.getId())
                .name(item.getName())
                .build();
    }

}