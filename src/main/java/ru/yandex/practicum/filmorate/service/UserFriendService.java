package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.api.dto.UserDtoForRead;
import ru.yandex.practicum.filmorate.dao.entity.UserEntity;
import ru.yandex.practicum.filmorate.exeption.BadRequestException;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFriendService {

    private final UserService userService;

    private final FilmUserMapper mapper;

    public List<UserDtoForRead> getUsersFriends(Integer id) {
        return userService.getUserFriendsById(id)
                .stream()
                .map(mapper::entityToUserDtoForRead)
                .sorted(Comparator.comparing(UserDtoForRead::getId))
                .collect(Collectors.toList());
    }

    public void addFriend(Integer id1, Integer id2) {
        if (userService.getUserFriendsById(id1)
                .stream()
                .map(UserEntity::getId)
                .anyMatch(id2::equals)) {
            throw new BadRequestException(String.format("Друг %d уже в друга %d", id2, id1));
        }
        UserEntity user1 = userService.getUser(id1);
        UserEntity user2 = userService.getUser(id2);
        Set<UserEntity> friends = user1.getFriends();
        friends.add(user2);
        user1.setFriends(friends);
        userService.save(user1);
    }

    public void deleteFriend(Integer id1, Integer id2) {
        UserEntity user1 = userService.getUser(id1);
        UserEntity user2 = userService.getUser(id2);
        Set<UserEntity> friends = user1.getFriends();
        friends.remove(user2);
        user1.setFriends(friends);
        userService.save(user1);
        log.info("Friend deleted");

    }

    public List<UserDtoForRead> commonFriend(Integer id1, Integer id2) {
        Set<UserEntity> friends1 = userService.getUserFriendsById(id1);
        return userService.getUserFriendsById(id2).stream()
                .filter(friends1::contains)
                .map(mapper::entityToUserDtoForRead)
                .sorted(Comparator.comparing(UserDtoForRead::getId))
                .collect(Collectors.toList());
    }
}
