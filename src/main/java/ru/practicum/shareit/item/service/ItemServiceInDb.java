package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepositoryInDb;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryInDb;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceInDb implements ItemService {

    private final ItemRepositoryInDb itemRepository;
    private final UserRepositoryInDb userRepository;

    @Override
    public ItemDto create(Integer userId, ItemDto itemDto) {
        Optional<User> user = userRepository.findById(userId.longValue());
        //ПРОДУМАТЬ ЧТО ДЕЛАТЬ С OWNER

        Item item = itemRepository.save(ItemMapper.fromDto(itemDto));
        //возвращает ли он item при методе save???
        return ItemMapper.toDto(item);
    }

    @Override
    public ItemDto get(Integer id) {
        Optional<Item> item = itemRepository.findById(id.longValue());
        return ItemMapper.toDto(item.get());
    }

    @Override
    List<ItemDto> getAllItemUsers(Integer userId) {
        Optional<User> user = userRepository.findById(userId.longValue());

        //ПРОДУМАТЬ ЧТО ДЕЛАТЬ С OWNER???
       // List<Item> items = itemRepository.findAllBy(user.getId());
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item i : items) {
            itemsDto.add(ItemMapper.toDto(i));
        }
        return itemsDto;
    }

    @Override
    public List<ItemDto> search(String text) {
        List<Item> items = itemRepository.findByNameContainingIgnoreCase(String text);
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item i : items) {
            itemsDto.add(ItemMapper.toDto(i));
        }
        return itemsDto;
    }

    @Override
    public ItemDto update(Integer userId, Integer itemId, ItemDto itemDto) {
        Optional<User> user = userRepository.findById(userId.longValue());

        Item item =  itemRepository.updateItem(user.get().getId(),itemId,ItemMapper.fromDto(itemDto));
        return ItemMapper.toDto(item);
    }

    @Override
    public void delete(Integer id) {
        itemRepository.deleteById(id.longValue());
    }
}
