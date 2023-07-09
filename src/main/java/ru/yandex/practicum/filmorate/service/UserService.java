package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.api.dto.UserDtoForAdd;
import ru.yandex.practicum.filmorate.api.dto.UserDtoForRead;
import ru.yandex.practicum.filmorate.exeption.BadRequestException;
import ru.yandex.practicum.filmorate.exeption.ResourceAlreadyExistExeption;
import ru.yandex.practicum.filmorate.exeption.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
@Getter
@RequiredArgsConstructor
@Slf4j
public class UserService extends BaseService<User> {

    private boolean checkEmail(String userEmail) {
        return items.stream().map(User::getEmail).noneMatch(userEmail::equals);
    }

    public List<UserDtoForRead> getAllUsers() {
        return mapper.toUserDtoForReadList(items);
    }

    public UserDtoForRead addUsers(UserDtoForAdd userDtoForAdd) {
        if (checkEmail(userDtoForAdd.getEmail())) {
            if (userDtoForAdd.getLogin().contains(" ") || userDtoForAdd.getLogin().isBlank()) {
                throw new BadRequestException("Поле логина не может быть пустым и не должен содержать пробелы");
            }
            if (StringUtils.isBlank(userDtoForAdd.getName())) {
                userDtoForAdd.setName(userDtoForAdd.getLogin());
            }
            User user = mapper.userDtoForAddToUser(userDtoForAdd);
            add(user);
            return mapper.toUserDtoForRead(user);
        } else {
            throw new ResourceAlreadyExistExeption("Такой пользователь уже есть");
        }
    }

    public UserDtoForRead updateUser(UserDtoForRead userDtoForRead) {
        User foundUser = getItem(userDtoForRead.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Такого пользователя нет"));
        if (StringUtils.isBlank(userDtoForRead.getLogin()) || userDtoForRead.getLogin().contains(" ")) {
            throw new BadRequestException("Поле логина не может быть пустым и не должен содержать пробелы");
        }
        items.remove(foundUser);
        items.add(mapper.userDtoForReadToUser(userDtoForRead));
        log.info("Обновлена информация о пользователе: {}", userDtoForRead.getName());
        return userDtoForRead;
    }
}
