package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item { //Вещь
    private int id;     // id вещи
    @NotBlank
    private String name;    // имя вещи
    @NotBlank
    private String description;     //описание вещи
    @NotNull
    private Boolean available;  // статус о том, доступна ли вещь для аренды
    private User owner;     // владелец вещи
    private String request; //ссылка на запрос (если была создана по запросу другого пользователя)
}
