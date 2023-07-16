package ru.yandex.practicum.filmorate.entity;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для фильмов (добавление, модификация, удаление).
 */
public interface FilmStorage {

    void add(Film updatedFilm);

    void remove(Film film);

    Optional<Film> getById(Integer id);

    List<Film> getAll();

    void reset();

    void replace(Film oldItem, Film newItem);
}
