package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class ItemRepositoryInMemory implements ItemRepository {

    private final Map<Integer, Item> items = new HashMap<>();

    private int idItem = 1;
    @Override
    public Item newItem(User user, Item item) {
        log.trace("добавление новой вещи");
        item.setId(idItem++);
        item.setOwner(user);
        items.put(item.getId(),item);
        return item;
    }

    @Override
    public Item getItem(Integer id) {
        log.trace("получение вещи");
        if (id == null) {
            throw new NullPointerException("Id вещи указан неверно");
        } else if (!items.containsKey(id)) {
            throw new IllegalArgumentException("Вещь с Id № " + id + " не найдена");
        }
        Item item = items.get(id);
        log.debug("Вещь с id №{} получена", id);
        return  item;
    }

    @Override
    public List<Item> getAllItemUsers(Integer userId) {
        log.trace("вывод всех вещей пользователя");
        return items.values().stream()
                .filter(u -> u.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchFilm(String text) {
        log.trace("поиск вещи по строке");
        if (!text.isBlank()) {
            String textLow = text.toLowerCase();
            return items.values().stream()
                    .filter(u -> u.getAvailable() &&
                            (u.getName().toLowerCase().contains(textLow) ||
                            u.getDescription().toLowerCase().contains(textLow)))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public Item updateItem(Integer userId, Integer itemId, Item item) {
        log.trace("обновление вещи");
        if (!isValidId(itemId)) {
            Item itemOrig = items.get(itemId);
            if (itemOrig.getOwner().getId() == userId) {
                if (item.getName() != null) {
                    itemOrig.setName(item.getName());
                }

                if (item.getDescription() != null) {
                    itemOrig.setDescription(item.getDescription());
                }

                if (item.getAvailable() != null) {
                    itemOrig.setAvailable(item.getAvailable());
                }
            } else {
                throw new IllegalArgumentException("пользователь не является собственником указанной вещи");
            }
        } else {
            throw new ValidationException("вещь с данным id не существует");
        }
        Item itemUpd = items.get(itemId);
        log.debug("Вещь с id №{} обновлена", itemId);
        return  itemUpd;
    }

    @Override
    public void deleteItem(Integer id) {
        throw new UnsupportedOperationException("не готов");
    }


    private boolean isValidId(Integer id){
        if (id == null || id == 0) {
            throw new ValidationException("вещь имеет ошибочное id");
        } else return !items.containsKey(id); // если не найден - true; если найден - false
    }

}