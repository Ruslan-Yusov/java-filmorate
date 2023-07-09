package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.api.dto.UserDtoForAdd;
import ru.yandex.practicum.filmorate.api.dto.UserDtoForRead;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public List<UserDtoForRead> allUser() {
        log.info("Вызван метод GetUsers");
        log.info("Количество пользователей: {}", userService.getItems().size());
        return userService.getAllUsers();
    }

    @PostMapping("/users")
    public UserDtoForRead create(
            @RequestBody @Validated UserDtoForAdd user
    ) {
        log.info("Вызван метод PostUsers");
        return userService.addUsers(user);
    }

    @PutMapping("/users")
    public UserDtoForRead updateUser(
            @RequestBody @Validated UserDtoForRead user
    ) {
        log.info("Вызван метод PutUsers");
        return userService.updateUser(user);
    }
}
