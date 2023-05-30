package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item { //Вещь

    @Id
    @Column(name = "item_id")
    private Integer id;     // id вещи
    @NotBlank
    private String name;    // имя вещи
    @NotBlank
    private String description;     //описание вещи
    @NotNull
    private Boolean available;  // статус о том, доступна ли вещь для аренды
    @Transient
    private User owner;     // владелец вещи
    private String request; //ссылка на запрос (если была создана по запросу другого пользователя)

    public Item(Integer id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }

}