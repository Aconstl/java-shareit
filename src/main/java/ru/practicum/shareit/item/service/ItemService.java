package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

public interface ItemService {
    Item newItem(Integer userId, Item item);

    Item getItem(Integer id);

    List<Item> getAllItemUsers(Integer userId);

    List<Item> searchFilm(String text);

    Item updateItem(Integer userId, Integer itemId, Item item);

    void deleteItem(Integer id);

}
