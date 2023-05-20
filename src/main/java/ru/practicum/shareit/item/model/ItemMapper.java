package ru.practicum.shareit.item.model;

public  class ItemMapper {
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