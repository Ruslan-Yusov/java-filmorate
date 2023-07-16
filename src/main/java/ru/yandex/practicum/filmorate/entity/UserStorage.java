package ru.yandex.practicum.filmorate.entity;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс пользователя (добавление, удаление, модификация).
 */
public interface UserStorage {


    void add(User user);

    void remove(User foundUser);

    Optional<User> getById(Integer id);

    List<User> getAll();

    void reset();

    void replace(User oldItem, User newItem);
}
