package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto newUser(@Valid @RequestBody UserDto userDto) {
        return UserMapper.toDto(userService.create(userDto));
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return UserMapper.toDto(userService.get(id));
    }

    @GetMapping
    public List<UserDto> getAll() {
        return UserMapper.toListDto(userService.getAll());
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id,
            @RequestBody UserDto userDto) {
        return UserMapper.toDto(userService.update(id,userDto));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}
