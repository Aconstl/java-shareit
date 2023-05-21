package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
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
public class ItemServiceImpl implements  ItemService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    @Override
    public ItemDto create(Integer userId, ItemDto itemDto) {
        User user = userRepository.get(userId);
        Item item = itemRepository.create(user,ItemMapper.fromDto(itemDto));
        return ItemMapper.toDto(item);
    }

    @Override
    public ItemDto get(Integer id) {
        Item item = itemRepository.get(id);
        return ItemMapper.toDto(item);
    }

    @Override
    public List<ItemDto> getAllItemUsers(Integer userId) {
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
    public ItemDto update(Integer userId, Integer itemId, ItemDto itemDto) {
        User user = userRepository.get(userId);
        Item item =  itemRepository.update(user.getId(),itemId,ItemMapper.fromDto(itemDto));
        return ItemMapper.toDto(item);
    }

    @Override
    public void delete(Integer id) {
        itemRepository.delete(id);
    }

}