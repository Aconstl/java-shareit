package ru.practicum.shareit.user.model;

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
}
