package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@Validated
public class UserController {

    private final UserClient userClient;

    @Autowired
    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Validated UserDto userDto) {
        log.info("Добавление пользователя");
        return userClient.createUser(userDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        log.info("Получение всех пользователей");
        return userClient.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findById(@PathVariable int userId) {
        log.info("Получение пользователя с id {}", userId);
        return userClient.findUserById(userId);
    }

    @GetMapping("/{userId}")
    public void delete(@PathVariable int userId) {
        log.info("Удаление пользователя {}", userId);
        userClient.deleteUser(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> patch(@PathVariable int userId, @RequestBody @Validated UserDto userDto) {
        log.info("Обновление пользователя");
        return userClient.patchUser(userId, userDto);
    }
}
