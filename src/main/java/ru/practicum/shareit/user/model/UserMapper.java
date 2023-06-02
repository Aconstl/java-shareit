package ru.practicum.shareit.user.model;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User fromDto(UserDto userDto) {
        return new User(userDto.getId(),userDto.getName(),userDto.getEmail());
    }

    public static List<UserDto> fromListDto(List<User> users) {
    List<UserDto> usersDto = new ArrayList<>();
        for (User u : users) {
        usersDto.add(UserMapper.toDto(u));
    }
        return usersDto;
    }
}
