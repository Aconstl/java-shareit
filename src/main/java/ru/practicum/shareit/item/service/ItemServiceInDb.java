package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
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
@Qualifier("ItemServiceInDb")
@Primary
public class ItemServiceInDb implements ItemService {

    private final ItemRepositoryInDb itemRepository;
    private final UserRepositoryInDb userRepository;

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        Optional<User> user = userRepository.findById(userId.longValue());
        //ПРОДУМАТЬ ЧТО ДЕЛАТЬ С OWNER

        Item item = itemRepository.save(ItemMapper.fromDto(itemDto));
        //возвращает ли он item при методе save???
        return ItemMapper.toDto(item);
    }

    @Override
    public ItemDto get(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        return ItemMapper.toDto(item.get());
    }

    @Override
    public List<ItemDto> getAllItemUsers(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        //ПРОДУМАТЬ ЧТО ДЕЛАТЬ С OWNER???
       // List<Item> items = itemRepository.findAllBy(user.getId());
       // List<ItemDto> itemsDto = new ArrayList<>();
       // for (Item i : items) {
       //     itemsDto.add(ItemMapper.toDto(i));
       // }
       // return itemsDto;
        return null;
    }

    @Override
    public List<ItemDto> search(String text) {
    //    List<Item> items = itemRepository.findByNameContainingIgnoreCase(String text);
     //   List<ItemDto> itemsDto = new ArrayList<>();
    //    for (Item i : items) {
    //        itemsDto.add(ItemMapper.toDto(i));
    //    }
    //    return itemsDto;
        return null;
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        Optional<User> user = userRepository.findById(userId);

       // Item item =  itemRepository.updateItem(user.get().getId(),itemId,ItemMapper.fromDto(itemDto));
      //  return ItemMapper.toDto(item);
        return null;
    }

    @Override
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }
}
