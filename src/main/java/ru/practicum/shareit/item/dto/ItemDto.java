package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class ItemDto {
    private Integer id;
    @NotBlank
    private String name;    // имя вещи
    @NotBlank
    private String description;     //описание вещи
    @NotNull
    private Boolean available;  // статус о том, доступна ли вещь для аренды


    public static ItemDto toDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static Item fromDto(ItemDto itemDto) {
        return new Item(itemDto.getId(),itemDto.getName(),itemDto.getDescription(),itemDto.getAvailable());
    }
}