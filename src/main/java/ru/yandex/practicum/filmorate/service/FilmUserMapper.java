package ru.yandex.practicum.filmorate.service;

import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.api.dto.FilmDtoForAdd;
import ru.yandex.practicum.filmorate.api.dto.FilmDtoForRead;
import ru.yandex.practicum.filmorate.api.dto.UserDtoForAdd;
import ru.yandex.practicum.filmorate.api.dto.UserDtoForRead;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

/**
 * Мапер из DTO в доменную область и обратно.
 */
@Mapper(componentModel = "spring")
public interface FilmUserMapper {

    List<FilmDtoForRead> toFilmDtoForReadList(Collection<Film> value);

    FilmDtoForRead toFilmDtoForRead(Film value);

    Film filmDtoForAddToFilm(FilmDtoForAdd value);

    Film filmDtoForReadToFilm(FilmDtoForRead value);

    List<UserDtoForRead> toUserDtoForReadList(Collection<User> value);

    UserDtoForRead toUserDtoForRead(User value);

    User userDtoForReadToUser(UserDtoForRead value);

    User userDtoForAddToUser(UserDtoForAdd value);
}
