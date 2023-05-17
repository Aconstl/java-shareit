package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements  ItemService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    @Override
    public Item newItem(Integer userId, Item item) {
        User user = userRepository.get(userId);
        return itemRepository.newItem(user,item);
    }

    @Override
    public Item getItem(Integer id) {
        return itemRepository.getItem(id);
    }

    @Override
    public List<Item> getAllItemUsers(Integer userId) {
        User user = userRepository.get(userId);
        return itemRepository.getAllItemUsers(user.getId());
    }

    @Override
    public List<Item> searchFilm(String text) {
        return itemRepository.searchFilm(text);
    }

    @Override
    public Item updateItem(Integer userId, Integer itemId, Item item) {
        User user = userRepository.get(userId);
        return itemRepository.updateItem(user.getId(),itemId,item);
    }

    @Override
    public void deleteItem(Integer id) {
        itemRepository.deleteItem(id);
    }

}
