package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@Getter
@Setter
public class UserDto {
    private Long id; // УНИКАЛЬНЫЙ id пользователя
    private String name;    // имя или логин пользователя
    @NotBlank
    @Email
    private String email;   // адрес эл.почты (УНИКАЛЬНЫЙ)
}
