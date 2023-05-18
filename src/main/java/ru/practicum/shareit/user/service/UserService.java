package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserService {
    User create(User user);

    User get(Integer id);

    List<User> getAll();

    User update(Integer id,User user);

    void delete(Integer id);

}