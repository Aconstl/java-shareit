package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto newItem(Integer userId, ItemDto itemDto);

    ItemDto getItem(Integer id);

    List<ItemDto> getAllItemUsers(Integer userId);

    List<ItemDto> searchFilm(String text);

    ItemDto updateItem(Integer userId, Integer itemId, ItemDto itemDto);

    void deleteItem(Integer id);

}
