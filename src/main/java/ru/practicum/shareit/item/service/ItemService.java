package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;

import java.util.List;

public interface ItemService {
    Item create(Long userId, ItemDto itemDto);

    Item get(Long id);

    List<Item> getAllItemUsers(Long userId);

    List<Item> search(String text);

    Item update(Long userId, Long itemId, ItemDto itemDto);

    void delete(Long id);

}
