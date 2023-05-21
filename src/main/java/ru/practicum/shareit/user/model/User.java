package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class User {
    private Integer id; // УНИКАЛЬНЫЙ id пользователя
    private String name;    // имя или логин пользователя
    @NotBlank
    @Email
    private String email;   // адрес эл.почты (УНИКАЛЬНЫЙ)

    public User(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

}
