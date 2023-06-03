
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

import javax.validation.ValidationException;
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
    public UserDto create(UserDto userDto) {
        log.trace("добавление пользователя");
        User userTo = UserMapper.fromDto(userDto);
            User user = userRepository.save(userTo);
            return UserMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto get(Long id) {
        log.trace("получение пользователя");
        if (id == null || id == 0) {
            throw new NullPointerException("Id пользователя указан неверно");
        }
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Пользователь с Id № " + id + " не найден");
        }
        log.debug("Пользователь с id №{} получен", id);
        return UserMapper.toDto(user.get());
    }

    @Override
    @Transactional
    public List<UserDto> getAll() {
        log.trace("получение всех пользователей");
        List<User> users = userRepository.findAll();
        return UserMapper.fromListDto(users);
    }

    @Override
    @Transactional
    public UserDto update(Long id,UserDto userDto) {
        log.trace("обновление пользователия");
        if (isValidId(id)) {
            if (userDto.getEmail() != null) {
                userRepository.updateUserEmail(id,userDto.getEmail());
            }
            if (userDto.getName() != null) {
                userRepository.updateUsername(id,userDto.getName());
            }
            return get(id);
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.trace("удаление пользователя");
        userRepository.deleteById(id);
    }

    private boolean isValidId(Long id) {
        if (id == null || id == 0) {
            throw new ValidationException("пользователь имеет ошибочное id");
        }
        else return true;
        //else return !users.containsKey(id); // если не найден - true; если найден - false
    }

}