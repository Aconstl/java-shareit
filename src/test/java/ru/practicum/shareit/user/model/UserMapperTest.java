package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void toDto() {
        User user = new User(1L,"user1","user1@mail.ru");
        UserDto userDto = UserMapper.toDto(user);
        assertEquals(user.getId(),userDto.getId());
        assertEquals(user.getName(),userDto.getName());
        assertEquals(user.getEmail(),userDto.getEmail());

    }

    @Test
    void fromDto() {
        UserDto userDto = UserDto.builder()
                        .id(1L)
                        .name("user1")
                        .email("user1@mail.ru")
                        .build();
        User user = UserMapper.fromDto(userDto);
        assertEquals(user.getId(),userDto.getId());
        assertEquals(user.getName(),userDto.getName());
        assertEquals(user.getEmail(),userDto.getEmail());
    }

    @Test
    void toDtoBooking() {
        User user = new User(1L,"user1","user1@mail.ru");
        UserDtoForBooking userDtoForBooking = UserMapper.toDtoBooking(user);
        assertEquals(user.getId(),userDtoForBooking.getId());
    }
}