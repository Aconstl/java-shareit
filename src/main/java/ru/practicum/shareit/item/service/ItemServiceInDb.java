package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepositoryInDb;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.service.UserServiceInDb;


import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("ItemServiceInDb")
@Primary
public class ItemServiceInDb implements ItemService {

    private final ItemRepositoryInDb itemRepository;
    private final UserServiceInDb userService;

    @Override
    @Transactional (propagation = Propagation.REQUIRES_NEW)
    public ItemDto create(Long userId, ItemDto itemDto) {
        log.trace("добавление предмета");
        User user = UserMapper.fromDto(userService.get(userId));
        Item item = ItemMapper.fromDto(itemDto);
        item.setOwner(user);
        Item itemRes = itemRepository.save(item);

        return ItemMapper.toDto(itemRes);
    }

    @Override
    @Transactional
    public ItemDto get(Long id) {
        log.trace("получение предмета");
        if (id == null || id == 0) {
            throw new NullPointerException("Id предмета указан неверно");
        }
        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty()) {
            throw new IllegalArgumentException("Предмет с Id № " + id + " не найден");
        }
        log.debug("Предмет с id №{} получен", id);
        return ItemMapper.toDto(item.get());
    }

    @Override
    @Transactional
    public List<ItemDto> getAllItemUsers(Long userId) {
        log.trace("вывод всех предметов пользователя");
        List<Item> items = itemRepository.findItemByOwner(userId);
       // List<Item> items = itemRepository.getItemsToOwner(userId);
        return ItemMapper.fromListDto(items);

    }

    @Override
    @Transactional
    public List<ItemDto> search(String text) {
        log.trace("поиск предмета по имени");
        if (!text.isBlank()) {
            List<Item> items = itemRepository.searchItem(text.toLowerCase());
            return ItemMapper.fromListDto(items);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        log.trace("обновление предмета");
        if (itemId == null || itemId == 0) {
            throw new ValidationException("предмет имеет ошибочное id");
        } else {
            if (!itemRepository.getOwnerId(itemId).equals(userId)) {
                throw new IllegalArgumentException("пользователь не является собственником указанного предмета");
           } else
            {
                String name = itemDto.getName();
                String description = itemDto.getDescription();
                Boolean available = itemDto.getAvailable();
                if (itemDto.getName() != null) {
                    itemRepository.updateName(itemId,name);
                }
                if (itemDto.getDescription() != null) {
                    itemRepository.updateDescription(itemId, description);
                }
                if (itemDto.getAvailable() != null) {
                    itemRepository.updateAvailable(itemId, available);
                }
            }
        }
        return get(itemId);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.trace("удаление предмета");
        itemRepository.deleteById(id);
    }
}
