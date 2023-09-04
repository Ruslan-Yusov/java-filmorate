package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.api.dto.FilmDtoForRead;
import ru.yandex.practicum.filmorate.dao.entity.FilmEntity;
import ru.yandex.practicum.filmorate.dao.entity.UserEntity;
import ru.yandex.practicum.filmorate.exeption.ResourceNotFoundException;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmLikesService {

    private final UserService userService;
    private final FilmService filmService;
    private final FilmUserMapper mapper;

    public List<FilmDtoForRead> getLikesFilm(Integer count) {
        return mapper.entityToFilmDtoForReadList(filmService.getAllFilms()
                .stream()
                .sorted(Comparator.comparing((FilmEntity film) -> film.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList()));
    }

    public void addLikes(Integer idFilm, Integer idUser) {
        UserEntity user = userService.getUser(idUser);
        FilmEntity film = filmService.getFilm(idFilm);
        if (film.getLikes().stream()
                .map(UserEntity::getId)
                .noneMatch(idUser::equals)) {
            Set<UserEntity> filmLikes = film.getLikes();
            filmLikes.add(user);
            film.setLikes(filmLikes);
            filmService.save(film);
        }
    }

    public void deleteLikes(Integer idFilm, Integer idUser) {
        UserEntity user = userService.getUser(idUser);
        FilmEntity film = filmService.getFilm(idFilm);
        Set<UserEntity> filmLikes = film.getLikes();
        if (!filmLikes.contains(user)) {
            throw new ResourceNotFoundException("Этот пользователь не ставил лайк фильму");
        }
        filmLikes.remove(user);
        film.setLikes(filmLikes);
        filmService.save(film);
    }
}
