package ru.practicum.shareit.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    private int id; // УНИКАЛЬНЫЙ id пользователя
    private String name;    // имя или логин пользователя
    @NotBlank
    @Email
    private String email;   // адрес эл.почты (УНИКАЛЬНЫЙ)
}
