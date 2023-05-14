package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item { //Вещь
    private int id;     // id вещи
    private String name;    // имя вещи
    private String description;     //описание вещи
    private Boolean available;  // статус о том, доступна ли вещь для аренды
    private User owner;     // владелец вещи
    private String request; //ссылка на запрос (если была создана по запросу другого пользователя)
}
