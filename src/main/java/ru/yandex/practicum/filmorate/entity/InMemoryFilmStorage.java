package ru.yandex.practicum.filmorate.entity;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Component
public class InMemoryFilmStorage extends BaseStorage<Film> implements FilmStorage {

    @Override
    public void add(Film updatedFilm) {
        super.add(updatedFilm);
    }

    @Override
    public void remove(Film film) {
        items.remove(film);
    }

    @Override
    public Optional<Film> getById(Integer id) {
        return items.stream().filter(film -> film.getId().equals(id)).findAny();
    }

    @Override
    public List<Film> getAll() {
        return items;
    }

    @Override
    public void replace(Film oldItem, Film newItem) {
        super.replace(oldItem, newItem);
    }
}
