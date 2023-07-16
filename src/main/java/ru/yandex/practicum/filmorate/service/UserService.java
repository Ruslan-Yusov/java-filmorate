package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.api.dto.UserDtoForAdd;
import ru.yandex.practicum.filmorate.api.dto.UserDtoForRead;
import ru.yandex.practicum.filmorate.entity.UserStorage;
import ru.yandex.practicum.filmorate.exeption.BadRequestException;
import ru.yandex.practicum.filmorate.exeption.ResourceAlreadyExistExeption;
import ru.yandex.practicum.filmorate.exeption.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
@Getter
@Setter
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Autowired
    protected FilmUserMapper mapper;

    @Autowired
    private UserStorage storage;

    private boolean checkEmail(String userEmail) {
        return storage.getAll().stream().map(User::getEmail).noneMatch(userEmail::equals);
    }

    public List<UserDtoForRead> getAllUsers() {
        return mapper.toUserDtoForReadList(storage.getAll());
    }

    public UserDtoForRead getUsersDto(Integer id) {
        return storage.getById(id)
                .map(mapper::toUserDtoForRead)
                .orElseThrow(() -> new ResourceNotFoundException("Такого пользователя нет"));
    }

    public User getUsers(Integer id) {
        return storage.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Такого пользователя нет"));
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
            storage.add(user);
            return mapper.toUserDtoForRead(user);
        } else {
            throw new ResourceAlreadyExistExeption("Такой пользователь уже есть");
        }
    }

    public UserDtoForRead updateUser(UserDtoForRead userDtoForRead) {
        User foundUser = storage.getById(userDtoForRead.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Такого пользователя нет"));
        if (StringUtils.isBlank(userDtoForRead.getLogin()) || userDtoForRead.getLogin().contains(" ")) {
            throw new BadRequestException("Поле логина не может быть пустым и не должен содержать пробелы");
        }
        storage.replace(foundUser, mapper.userDtoForReadToUser(userDtoForRead));
        log.info("Обновлена информация о пользователе: {}", userDtoForRead.getName());
        return userDtoForRead;
    }

    public void deleteUser(Integer id) {
        User user = storage.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Такого пользователя нет"));
        log.info("Пользователь удален {}", user.getName());
        storage.remove(user);
    }
}
