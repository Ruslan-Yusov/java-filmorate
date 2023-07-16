package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.api.dto.FilmDtoForRead;
import ru.yandex.practicum.filmorate.exeption.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmLikesService {

    @Autowired
    private UserService userService;

    @Autowired
    private FilmService filmService;

    @Autowired
    protected FilmUserMapper mapper;

    public List<FilmDtoForRead> getLikesFilm(Integer count) {
        return mapper.toFilmDtoForReadList(filmService.getAllFilms()
                .stream()
                .sorted(Comparator.comparing(Film::getLikesCount).reversed())
                .limit(count)
                .collect(Collectors.toList()));
    }

    public void addLikes(Integer idFilm, Integer idUser) {
        userService.getUsers(idUser);
        Film film = filmService.getFilms(idFilm);
        Set<Integer> filmLikes = film.getLikes();
        filmLikes.add(idUser);
        film.setLikes(filmLikes);
    }

    public void deleteLikes(Integer idFilm, Integer idUser) {
        userService.getUsers(idUser);
        Film film = filmService.getFilms(idFilm);
        Set<Integer> filmLikes = film.getLikes();
        if (!filmLikes.contains(idUser)) {
            throw new ResourceNotFoundException("Этот пользователь не ставил лайк фильму");
        }
        filmLikes.remove(idUser);
        film.setLikes(filmLikes);
    }
}