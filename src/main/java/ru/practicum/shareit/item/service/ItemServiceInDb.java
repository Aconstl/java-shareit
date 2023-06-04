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
    public Item create(Long userId, ItemDto itemDto) {
        log.trace("добавление предмета");
        User user = userService.get(userId);
        Item item = ItemMapper.fromDto(itemDto);
        item.setOwner(user);
        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public Item get(Long id) {
        log.trace("получение предмета");
        if (id == null || id == 0) {
            throw new NullPointerException("Id предмета указан неверно");
        }
        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty()) {
            throw new IllegalArgumentException("Предмет с Id № " + id + " не найден");
        }
        log.debug("Предмет с id №{} получен", id);
        return item.get();
    }

    @Override
    @Transactional
    public List<Item> getAllItemUsers(Long userId) {
        log.trace("вывод всех предметов пользователя");
        return itemRepository.findItemByOwner(userId);
    }

    @Override
    @Transactional
    public List<Item> search(String text) {
        log.trace("поиск предмета по имени");
        if (!text.isBlank()) {
            return itemRepository.searchItem(text.toLowerCase());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public Item update(Long userId, Long itemId, ItemDto itemDto) {
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
