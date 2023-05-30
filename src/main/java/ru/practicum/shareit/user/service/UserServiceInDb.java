
package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.repository.UserRepositoryInDb;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Qualifier("UserServiceInDb")
@Primary
public class UserServiceInDb implements UserService {

    private final UserRepositoryInDb userRepository;

    @Override
    public UserDto create(UserDto userDto) {
        User user = userRepository.save(UserMapper.fromDto(userDto));
        return UserMapper.toDto(user);
    }

    @Override
    public UserDto get(Integer id) {
        Optional<User> user = userRepository.findById(id.longValue());
        return UserMapper.toDto(user.get());
    }

    @Override
    public List<UserDto> getAll() {
        List<User> users = userRepository.findAll();
        List<UserDto> usersDto = new ArrayList<>();
        for (User u : users) {
            usersDto.add(UserMapper.toDto(u));
        }
        return usersDto;
    }

    @Override
    public UserDto update(Integer id,UserDto userDto) {
        User user = userRepository.updateUser(id,userDto.getName(),userDto.getEmail());
        return UserMapper.toDto(user);
    }

    @Override
    public void delete(Integer id) {
        userRepository.deleteById(id.longValue());
    }
}

