package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.api.dto.UserDtoForAdd;
import ru.yandex.practicum.filmorate.api.dto.UserDtoForRead;
import ru.yandex.practicum.filmorate.service.UserFriendService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

/**
 * Controller.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    private final UserFriendService userFriendService;

    @GetMapping("/users")
    public List<UserDtoForRead> allUser() {
        log.info("Вызван метод GetUsers");
        log.info("Количество пользователей: {}", userService.getAllUsers().size());
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public UserDtoForRead users(@PathVariable("id") Integer id) {
        return userService.getUsersDto(id);
    }

    @PostMapping("/users")
    public UserDtoForRead create(
            @RequestBody @Validated UserDtoForAdd user
    ) {
        log.info("Вызван метод PostUsers");
        return userService.addUser(user);
    }

    @PutMapping("/users")
    public UserDtoForRead updateUser(
            @RequestBody @Validated UserDtoForRead user
    ) {
        log.info("Вызван метод PutUsers");
        return userService.updateUser(user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
    }

    @GetMapping("/users/{id}/friends")
    public List<UserDtoForRead> getFriendsById(@PathVariable("id") Integer id) {
        return userFriendService.getUsersFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<UserDtoForRead> commonFriends(@PathVariable("id") Integer id1,
                                              @PathVariable("otherId") Integer id2) {
        return userFriendService.commonFriend(id1, id2);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriendById(@PathVariable("id") Integer id,
                              @PathVariable("friendId") Integer friendId) {
        userFriendService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriendById(@PathVariable("id") Integer id,
                                 @PathVariable("friendId") Integer friendId) {
        userFriendService.deleteFriend(id, friendId);
    }
}
