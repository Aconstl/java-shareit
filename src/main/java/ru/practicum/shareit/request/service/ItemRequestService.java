package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequest newItemRequest(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequest> getMyItemRequest(Long userId);

    List<ItemRequest> getAllItemRequest(Long userId, Long from, Long size);

    ItemRequest getItemRequest(Long itemRequestId);
}
