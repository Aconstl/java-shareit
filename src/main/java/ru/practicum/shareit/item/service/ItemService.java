package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(Integer userId, ItemDto itemDto);

    ItemDto get(Integer id);

    List<ItemDto> getAllItemUsers(Integer userId);

    List<ItemDto> search(String text);

    ItemDto update(Integer userId, Integer itemId, ItemDto itemDto);

    void delete(Integer id);

}
