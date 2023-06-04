/*
package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Qualifier("ItemServiceInMemory")
public class ItemServiceImpl implements  ItemService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        User user = userRepository.get(userId);
        Item item = itemRepository.create(user,ItemMapper.fromDto(itemDto));
        return ItemMapper.toDto(item);
    }

    @Override
    public ItemDto get(Long id) {
        Item item = itemRepository.get(id);
        return ItemMapper.toDto(item);
    }

    @Override
    public List<ItemDto> getAllItemUsers(Long userId) {
        User user = userRepository.get(userId);
        List<Item> items = itemRepository.getAllItemUsers(user.getId());
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item i : items) {
            itemsDto.add(ItemMapper.toDto(i));
        }
        return itemsDto;
    }

    @Override
    public List<ItemDto> search(String text) {
        List<Item> items = itemRepository.search(text);
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item i : items) {
            itemsDto.add(ItemMapper.toDto(i));
        }
        return itemsDto;
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        User user = userRepository.get(userId);
        Item item =  itemRepository.update(user.getId(),itemId,ItemMapper.fromDto(itemDto));
        return ItemMapper.toDto(item);
    }

    @Override
    public void delete(Long id) {
        itemRepository.delete(id);
    }

}*/
