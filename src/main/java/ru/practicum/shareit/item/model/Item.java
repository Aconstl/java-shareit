package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
@Getter @Setter
public class Item { //Вещь

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;     // id вещи
    @NotBlank
    private String name;    // имя вещи
    @NotBlank
    private String description;     //описание вещи
    @NotNull
    private Boolean available;  // статус о том, доступна ли вещь для аренды
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;     // владелец вещи
    @Transient
    private String request; //ссылка на запрос (если была создана по запросу другого пользователя)

    public Item(Long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }

}