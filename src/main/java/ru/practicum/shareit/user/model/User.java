package ru.practicum.shareit.user.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // УНИКАЛЬНЫЙ id пользователя
    @Column(name = "username")
    private String name;    // имя или логин пользователя
    @NotBlank
    @Email
    @Column(unique = true)
    private String email;   // адрес эл.почты (УНИКАЛЬНЫЙ)

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

}
