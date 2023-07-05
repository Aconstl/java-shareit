package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import javax.validation.ValidationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceInDbIntegrationTest {

    @Autowired
    UserService userService;

    @Test
    @DirtiesContext
    public void createAndGetUserTest() {
        UserDto userDto = UserDto.builder()
                .name("user1")
                .email("user1@mail.ru")
                .build();

        User user = userService.create(userDto);
        User userTest = userService.get(user.getId());
        assertEquals(user.getId(),userTest.getId());
        assertEquals(user.getName(),userTest.getName());
        assertEquals(user.getEmail(),userTest.getEmail());
        assertThrows(NullPointerException.class, () -> userService.get(0L));
        assertThrows(NullPointerException.class, () -> userService.get(null));
    }

    @Test
    @DirtiesContext
    public void getAllUsersAndDeleteTest() {
        List<User> emptyUserList = userService.getAll();
        assertTrue(emptyUserList.isEmpty());

        UserDto userDto1 = UserDto.builder()
                .name("user1")
                .email("user1@mail.ru")
                .build();

        UserDto userDto2 = UserDto.builder()
                .name("user2")
                .email("user2@mail.ru")
                .build();

        UserDto userDto3 = UserDto.builder()
                .name("user3")
                .email("user3@mail.ru")
                .build();

        userService.create(userDto1);
        userService.create(userDto2);
        userService.create(userDto3);

        List<User> userList = userService.getAll();
        assertEquals(userList.size(),3);
        assertEquals(userList.get(0).getId(),userService.get(1L).getId());
        assertEquals(userList.get(0).getName(),userService.get(1L).getName());
        assertEquals(userList.get(0).getEmail(),userService.get(1L).getEmail());

        assertEquals(userList.get(1).getId(),userService.get(2L).getId());
        assertEquals(userList.get(1).getName(),userService.get(2L).getName());
        assertEquals(userList.get(1).getEmail(),userService.get(2L).getEmail());

        assertEquals(userList.get(2).getId(),userService.get(3L).getId());
        assertEquals(userList.get(2).getName(),userService.get(3L).getName());
        assertEquals(userList.get(2).getEmail(),userService.get(3L).getEmail());

        userService.delete(1L);
        assertThrows(IllegalArgumentException.class, () -> userService.get(1L));
        assertEquals(userService.getAll().size(),2);
    }

    @Test
    @DirtiesContext
    public void updateUserTest() {
        UserDto userDto = UserDto.builder()
                .name("user1")
                .email("user1@mail.ru")
                .build();

        userService.create(userDto);

        UserDto userDtoUpdate = UserDto.builder()
                .name("user1Update")
                .email("user1Update@mail.ru")
                .build();

        User user = userService.update(1L,userDtoUpdate);
        assertEquals(user.getName(),"user1Update");
        assertEquals(user.getEmail(),"user1Update@mail.ru");
        assertThrows(ValidationException.class,() -> userService.update(null,userDtoUpdate));
        assertThrows(ValidationException.class,() -> userService.update(0L,userDtoUpdate));
    }

}