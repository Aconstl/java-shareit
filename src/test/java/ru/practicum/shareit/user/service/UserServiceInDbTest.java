package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.repository.UserRepositoryInDb;
import org.mockito.*;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceInDbTest {
    private UserServiceInDb userService;

    @Mock
    UserRepositoryInDb userRepository;


    @BeforeEach
    public void setUp() {
        userService = new UserServiceInDb(userRepository);
    }

    @Test
    public void getUserTest() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("user1")
                .email("user1@mail.ru")
                .build();

        userService.create(userDto);

           when(userRepository.findById(userDto.getId()))
                  .thenReturn(Optional.of(UserMapper.fromDto(userDto)));

        User user = userService.get(userDto.getId());
        assertEquals(user.getId(),userDto.getId());
        assertEquals(user.getName(),userDto.getName());
        assertEquals(user.getEmail(),userDto.getEmail());

        assertThrows(NullPointerException.class,() -> userService.get(null));
        assertThrows(NullPointerException.class,() -> userService.get(0L));
        assertThrows(IllegalArgumentException.class, () -> userService.get(2L));

        verify(userRepository,times(1)).save(Mockito.any(User.class));
        verify(userRepository,times(2)).findById(Mockito.anyLong());
    }

    @Test
    public void getAllUsersAndDeleteTest() {
        when(userRepository.findAll())
                .thenReturn(new ArrayList<>());
        List<User> emptyUserList = userService.getAll();
        assertTrue(emptyUserList.isEmpty());


        UserDto userDto1 = UserDto.builder()
                .id(1L)
                .name("user1")
                .email("user1@mail.ru")
                .build();

        UserDto userDto2 = UserDto.builder()
                .id(2L)
                .name("user2")
                .email("user2@mail.ru")
                .build();

        UserDto userDto3 = UserDto.builder()
                .id(3L)
                .name("user3")
                .email("user3@mail.ru")
                .build();

        userService.create(userDto1);
        userService.create(userDto2);
        userService.create(userDto3);

        List<User> users = new ArrayList<>();
        users.add(UserMapper.fromDto(userDto1));
        users.add(UserMapper.fromDto(userDto2));
        users.add(UserMapper.fromDto(userDto3));

        when(userRepository.findAll())
                .thenReturn(users);

        List<User> userList = userService.getAll();
        assertEquals(users.size(), userList.size());
        assertEquals(users.get(0), userList.get(0));
        assertEquals(users.get(1), userList.get(1));
        assertEquals(users.get(2), userList.get(2));

        userService.delete(1L);
        verify(userRepository,times(1)).deleteById(1L);
    }

    @Test
    public void updateUserTest() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("user1")
                .email("user1@mail.ru")
                .build();

        userService.create(userDto);

        UserDto userDtoUpdate = UserDto.builder()
                .id(1L)
                .name("user1Update")
                .email("user1Update@mail.ru")
                .build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(UserMapper.fromDto(userDtoUpdate)));

        User user = userService.update(userDtoUpdate.getId(),userDtoUpdate);
        assertEquals(user.getName(),"user1Update");
        assertEquals(user.getEmail(),"user1Update@mail.ru");
        verify(userRepository,times(1)).updateUsername(1L,userDtoUpdate.getName());
        verify(userRepository,times(1)).updateUserEmail(1L,userDtoUpdate.getEmail());
        assertThrows(ValidationException.class,() -> userService.update(null,userDtoUpdate));
        assertThrows(ValidationException.class,() -> userService.update(0L,userDtoUpdate));
    }
}