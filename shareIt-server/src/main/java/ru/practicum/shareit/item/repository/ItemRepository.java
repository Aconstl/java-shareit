package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository {

    Item create(User user, Item item);

    Item get(Long id);

    List<Item> getAllItemUsers(Long userId);

    List<Item> search(String text);

    Item update(Long userId, Long itemId, Item item);

    void delete(Long id);

}