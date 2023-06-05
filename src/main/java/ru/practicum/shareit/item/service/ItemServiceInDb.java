package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepositoryInDb;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemDtoWithBooking;
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

    private final BookingRepositoryInDb bookingRepository;

    @Override
    @Transactional (propagation = Propagation.REQUIRES_NEW)
    public Item create(Long userId, ItemDto itemDto) {
        log.trace("добавление предмета");
        User user = userService.get(userId);
        Item item = ItemMapper.fromDto(itemDto);
        item.setOwner(user);
        return itemRepository.save(item);
    }

    public Item find(Long id) {
        if (id == null || id == 0) {
            throw new NullPointerException("Id предмета указан неверно");
        }
        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty()) {
            throw new IllegalArgumentException("Предмет с Id № " + id + " не найден");
        }
        return item.get();
    }

    @Override
    @Transactional
    public ItemDtoWithBooking get(Long id,Long userId) {
        log.trace("получение предмета");
        Item item = find(id);
        ItemDtoWithBooking itemFin = addBooking(item,userId);
        log.debug("Предмет с id №{} получен", id);
        return itemFin;
    }
    @Override
    @Transactional
    public List<ItemDtoWithBooking> getAllItemUsers(Long userId) {
        log.trace("вывод всех предметов пользователя");
        List<Long> listIdItem = itemRepository.findItemByOwner(userId);
        List<ItemDtoWithBooking> itemsDto = new ArrayList<>();
        for (Long id : listIdItem) {
            Item item = find(id);
            itemsDto.add(addBooking(item,userId));
        }
        return itemsDto;
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
        return find(itemId);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.trace("удаление предмета");
        itemRepository.deleteById(id);
    }

    private ItemDtoWithBooking addBooking (Item item,Long userId) {
        Booking lastBooking = null;
        Booking nextBooking = null;

        if (userId.equals(item.getOwner().getId())) {
            lastBooking = bookingRepository.getLastBooking(item.getId());
            if (lastBooking == null || lastBooking.getBooker().getId().equals(item.getOwner().getId())) {
                lastBooking = null;
            } else {
                nextBooking = bookingRepository.getNextBooking(item.getId(), lastBooking.getEnd());
            }
        }
        return ItemMapper.toDtoWithBooking(item,
                BookingMapper.toDtoForItem(lastBooking),
                BookingMapper.toDtoForItem(nextBooking));
    }

}
