package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements  ItemService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    @Override
    public ItemDto newItem(Integer userId, ItemDto itemDto) {
        User user = userRepository.get(userId);
        Item item = itemRepository.newItem(user,ItemDto.fromDto(itemDto));
        return ItemDto.toDto(item);
    }

    @Override
    public ItemDto getItem(Integer id) {
        Item item = itemRepository.getItem(id);
        return ItemDto.toDto(item);
    }

    @Override
    public List<ItemDto> getAllItemUsers(Integer userId) {
        User user = userRepository.get(userId);
        List<Item> items = itemRepository.getAllItemUsers(user.getId());
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item i : items) {
            itemsDto.add(ItemDto.toDto(i));
        }
        return itemsDto;
    }

    @Override
    public List<ItemDto> searchFilm(String text) {
        List<Item> items = itemRepository.searchFilm(text);
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item i : items) {
            itemsDto.add(ItemDto.toDto(i));
        }
        return itemsDto;
    }

    @Override
    public ItemDto updateItem(Integer userId, Integer itemId, ItemDto itemDto) {
        User user = userRepository.get(userId);
        Item item =  itemRepository.updateItem(user.getId(),itemId,ItemDto.fromDto(itemDto));
        return ItemDto.toDto(item);
    }

    @Override
    public void deleteItem(Integer id) {
        itemRepository.deleteItem(id);
    }

}
