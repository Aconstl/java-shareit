package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemRepository {

    Item newItem(User user, Item item);

    Item getItem(Integer id);

    List<Item> getAllItemUsers(Integer userId);

    List<Item> searchFilm(String text);

    Item updateItem(Integer userId, Integer itemId, Item item);

    void deleteItem(Integer id);

}