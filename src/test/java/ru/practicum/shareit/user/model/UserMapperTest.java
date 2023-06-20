package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserMapperTest {

    @Autowired
    JacksonTester<User> json;

    @Test
    public void test() throws IOException {
        User user = new User(1L,"name", "name@mail.com");

        JsonContent<User> userJson = json.write(user);
        assertThat(userJson).hasJsonPath("$.email");
        assertThat(userJson).extractingJsonPathStringValue("$.email").isEqualTo(user.getEmail());
    }

    /*
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
    */
}