package ru.practicum.shareit.request.model;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ItemRequestMapper {
    public static ItemRequest fromDto(ItemRequestDto itemRequestDto, User user) {
        return new ItemRequest(itemRequestDto.getDescription(),user, LocalDateTime.now());
    }

    public static ItemRequestDto toDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .listsItem(itemRequest.getListsItem())
                .build();
    }

    public static List<ItemRequestDto> toDtoList(List<ItemRequest> requests) {
        List<ItemRequestDto> itemRequest = new ArrayList<>();
        for(ItemRequest i : requests) {
            itemRequest.add(ItemRequestMapper.toDto(i));
        }
        return itemRequest;
    }
}
