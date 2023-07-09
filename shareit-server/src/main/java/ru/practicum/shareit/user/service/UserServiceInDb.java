
package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.repository.UserRepositoryInDb;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("UserServiceInDb")
@Primary
public class UserServiceInDb implements UserService {

    private final UserRepositoryInDb userRepository;

    @Override
    @Transactional
    public User create(UserDto userDto) {
        log.trace("добавление пользователя");
        User userTo = UserMapper.fromDto(userDto);
        return userRepository.save(userTo);
    }

    @Override
    public User get(Long id) {
        log.trace("получение пользователя");
        if (id == null || id == 0) {
            throw new NullPointerException("Id пользователя указан неверно");
        }
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Пользователь с Id № " + id + " не найден");
        }
        log.debug("Пользователь с id №{} получен", id);
        return user.get();
    }

    @Override
    public List<User> getAll() {
        log.trace("получение всех пользователей");
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User update(Long id,UserDto userDto) {
        log.trace("обновление пользователия");
        if (userDto.getEmail() != null) {
            userRepository.updateUserEmail(id,userDto.getEmail());
        }
        if (userDto.getName() != null) {
            userRepository.updateUsername(id,userDto.getName());
        }
        return get(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.trace("удаление пользователя");
        userRepository.deleteById(id);
    }
}