package ru.yandex.practicum.filmorate.service;

import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.api.dto.*;
import ru.yandex.practicum.filmorate.dao.entity.FilmEntity;
import ru.yandex.practicum.filmorate.dao.entity.GenreEntity;
import ru.yandex.practicum.filmorate.dao.entity.MpaEntity;
import ru.yandex.practicum.filmorate.dao.entity.UserEntity;

import java.util.Collection;
import java.util.List;

/**
 * Мапер из DTO в доменную область и обратно.
 */
@Mapper(componentModel = "spring")
public interface FilmUserMapper {

    List<FilmDtoForRead> entityToFilmDtoForReadList(Collection<FilmEntity> value);

    FilmDtoForRead entityToFilmDtoForRead(FilmEntity value);

    FilmEntity filmDtoForAddToFilmEntity(FilmDtoForAdd value);

    List<UserDtoForRead> entityToUserDtoForReadList(Collection<UserEntity> value);

    UserDtoForRead entityToUserDtoForRead(UserEntity value);

    UserEntity userDtoForReadToUserEntity(UserDtoForRead value);

    UserEntity userDtoForAddToUserEntity(UserDtoForAdd value);

    DictionaryDto mpaEtityToDictionaryDto(MpaEntity value);

    DictionaryDto genreEntityToDictionaryDto(GenreEntity value);

    SimpleDto genreEntityToDto(GenreEntity value);

    SimpleDto mpaEntityToDto(MpaEntity value);
}
