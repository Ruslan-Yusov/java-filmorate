package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.api.dto.UserDtoForRead;
import ru.yandex.practicum.filmorate.exeption.BadRequestException;
import ru.yandex.practicum.filmorate.exeption.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserFriendService {

    private final UserService userService;

    public List<UserDtoForRead> getUsersFriends(Integer id1) {
        return userService.getUsers(id1)
                .getFriends()
                .stream()
                .map(userService::getUsersDto)
                .collect(Collectors.toList());
    }

    public void addFriend(Integer id1, Integer id2) {
        User user1 = userService.getUsers(id1);
        Set<Integer> friends1 = user1.getFriends();
        if (friends1.contains(id2)) {
            throw new BadRequestException(String.format("Друг %d уже в друга %d", id2, id1));
        }
        User user2 = userService.getUsers(id2);
        Set<Integer> friends2 = user2.getFriends();
        if (friends2.contains(id1)) {
            throw new BadRequestException(String.format("Друг %d уже в друга %d", id1, id2));
        }
        friends1.add(id2);
        user1.setFriends(friends1);
        friends2.add(id1);
        user2.setFriends(friends2);
    }

    public void deleteFriend(Integer id1, Integer id2) {
        User user1 = userService.getUsers(id1);
        Set<Integer> friends1 = user1.getFriends();
        if (!friends1.contains(id2)) {
            throw new ResourceNotFoundException(String.format("У друга %d нет друга %d", id2, id1));
        }
        User user2 = userService.getUsers(id2);
        Set<Integer> friends2 = user2.getFriends();
        if (!friends2.contains(id1)) {
            throw new ResourceNotFoundException(String.format("У друга %d нет друга %d", id1, id2));
        }
        friends2.remove(id1);
        friends1.remove(id2);
        user1.setFriends(friends1);
        user2.setFriends(friends2);
    }

    public List<UserDtoForRead> commonFriend(Integer id1, Integer id2) {
        Set<Integer> friends1 = userService
                .getUsers(id1)
                .getFriends();
        return userService.getUsers(id2).getFriends()
                .stream()
                .filter(friends1::contains)
                .map(userService::getUsersDto)
                .collect(Collectors.toList());
    }
}
