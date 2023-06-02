package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Qualifier("UserServiceImpl")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {
        User user = userRepository.create(UserMapper.fromDto(userDto));
        return UserMapper.toDto(user);
    }

    @Override
    public UserDto get(Long id) {
        User user = userRepository.get(id);
        return UserMapper.toDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        List<User> users = userRepository.getAll();
        List<UserDto> usersDto = new ArrayList<>();
        for (User u : users) {
            usersDto.add(UserMapper.toDto(u));
        }
        return usersDto;
    }

    @Override
    public UserDto update(Long id,UserDto userDto) {
        User user = userRepository.update(id,UserMapper.fromDto(userDto));
        return UserMapper.toDto(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }

}