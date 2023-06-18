package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceInDbIntegrationTest {

    @Autowired
    UserServiceInDb userService;

    @Test
    public void createUser() {
        UserDto userDto = UserDto.builder()
                .id(null)
                .name("user1")
                .email("user1@mail.ru")
                .build();

        User user = userService.create(userDto);
        User userTest =userService.get(user.getId());
        assertEquals(user.getId(),userTest.getId());
        assertEquals(user.getName(),userTest.getName());
        assertEquals(user.getEmail(),userTest.getEmail());
    }
}