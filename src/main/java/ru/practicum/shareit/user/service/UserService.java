package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import java.util.List;

public interface UserService {
    User create(UserDto userDto);

    User get(Long id);

    List<User> getAll();

    User update(Long id,UserDto userDto);

    void delete(Long id);

}