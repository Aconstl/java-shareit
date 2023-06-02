package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto get(Long id);

    List<ItemDto> getAllItemUsers(Long userId);

    List<ItemDto> search(String text);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    void delete(Long id);

}
