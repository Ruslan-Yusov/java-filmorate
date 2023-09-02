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
import ru.yandex.practicum.filmorate.dao.entity.UserEntity;
import ru.yandex.practicum.filmorate.dao.repository.UserRepository;
import ru.yandex.practicum.filmorate.exeption.BadRequestException;
import ru.yandex.practicum.filmorate.exeption.ResourceAlreadyExistExeption;
import ru.yandex.practicum.filmorate.exeption.ResourceNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@Getter
@Setter
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Autowired
    protected FilmUserMapper mapper;

    @Autowired
    private UserRepository repository;

    private boolean checkEmail(String userEmail) {
        return repository.findAll().stream().map(UserEntity::getEmail).noneMatch(userEmail::equals);
    }

    public List<UserDtoForRead> getAllUsers() {
        return mapper.entityToUserDtoForReadList(repository.findAll());
    }

    public UserDtoForRead getUsersDto(Integer id) {
        return repository.findById(id)
                .map(mapper::entityToUserDtoForRead)
                .orElseThrow(() -> new ResourceNotFoundException("Такого пользователя нет"));
    }

    public UserEntity getUser(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Такого пользователя нет"));
    }

    public UserDtoForRead addUser(UserDtoForAdd userDtoForAdd) {
        if (checkEmail(userDtoForAdd.getEmail())) {
            if (userDtoForAdd.getLogin().contains(" ") || userDtoForAdd.getLogin().isBlank()) {
                throw new BadRequestException("Поле логина не может быть пустым и не должен содержать пробелы");
            }
            if (StringUtils.isBlank(userDtoForAdd.getName())) {
                userDtoForAdd.setName(userDtoForAdd.getLogin());
            }
            UserEntity user = mapper.userDtoForAddToUserEntity(userDtoForAdd);
            repository.save(user);
            return mapper.entityToUserDtoForRead(user);
        } else {
            throw new ResourceAlreadyExistExeption("Такой пользователь уже есть");
        }
    }

    public UserDtoForRead updateUser(UserDtoForRead userDtoForRead) {
        repository.findById(userDtoForRead.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Такого пользователя нет"));
        if (StringUtils.isBlank(userDtoForRead.getLogin()) || userDtoForRead.getLogin().contains(" ")) {
            throw new BadRequestException("Поле логина не может быть пустым и не должен содержать пробелы");
        }
        repository.save(mapper.userDtoForReadToUserEntity(userDtoForRead));
        log.info("Обновлена информация о пользователе: {}", userDtoForRead.getName());
        return userDtoForRead;
    }

    public void deleteUser(Integer id) {
        UserEntity user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Такого пользователя нет"));
        log.info("Пользователь удален {}", user.getName());
        repository.delete(user);
    }

    public Set<UserEntity> getUserFriendsById(Integer id) {
        return repository.findById(id)
                .map(UserEntity::getFriends)
                .orElse(Collections.emptySet());
    }

    public void save(UserEntity user) {
        repository.saveAndFlush(user);
    }
}
