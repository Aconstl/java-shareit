package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository {

    Item create(User user, Item item);

    Item get(Integer id);

    List<Item> getAllItemUsers(Integer userId);

    List<Item> search(String text);

    Item update(Integer userId, Integer itemId, Item item);

    void delete(Integer id);

}