package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public User newUser(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        return userService.get(id);
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable Integer id,
            @RequestBody User user) {
        return userService.update(id,user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.delete(id);
    }
}
