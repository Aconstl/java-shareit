package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        return userRepository.create(user);
    }

    @Override
    public User get(Integer id) {
       return userRepository.get(id);
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User update(Integer id,User user){
        return userRepository.update(id,user);
    }

    @Override
    public void delete(Integer id){
        userRepository.delete(id);
    }


}
