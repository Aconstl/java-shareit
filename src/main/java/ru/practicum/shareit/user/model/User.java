package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    private Integer id; // УНИКАЛЬНЫЙ id пользователя
    @Column(name = "username")
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
