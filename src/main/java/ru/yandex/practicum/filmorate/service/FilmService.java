package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.api.dto.FilmDtoForAdd;
import ru.yandex.practicum.filmorate.api.dto.FilmDtoForRead;
import ru.yandex.practicum.filmorate.exeption.BadRequestException;
import ru.yandex.practicum.filmorate.exeption.ResourceAlreadyExistExeption;
import ru.yandex.practicum.filmorate.exeption.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Сервис с основной бизнес логикой для класса {@link Film}.
 */
@Service
@Getter
@Slf4j
public class FilmService extends BaseService<Film> {

    private static final String DATE_CONST = "1895-12-28";
    private static final LocalDate DATE_FILM =
            LocalDate.parse(DATE_CONST, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    private boolean checkFilmName(String filmName) {
        return items.stream().map(Film::getName).noneMatch(filmName::equals);
    }

    private boolean checkDateFilm(LocalDate filmDate) {
        return DATE_FILM.isBefore(filmDate);
    }

    public List<FilmDtoForRead> getAllFilms() {
        return mapper.toFilmDtoForReadList(items);
    }

    public FilmDtoForRead addFilm(FilmDtoForAdd filmDto) {
        if (checkFilmName(filmDto.getName())) {
           if (!checkDateFilm(filmDto.getReleaseDate())) {
               throw new BadRequestException(String.format("Дата не должна быть ранее %s", DATE_CONST));
            }
            Film film = mapper.filmDtoForAddToFilm(filmDto);
            add(film);
            return mapper.toFilmDtoForRead(film);
        } else {
            throw new ResourceAlreadyExistExeption("Такой фильм уже есть в списке");
        }
    }

    public FilmDtoForRead updateFilm(FilmDtoForRead filmDtoForRead) {
        if (!checkDateFilm(filmDtoForRead.getReleaseDate())) {
            throw new BadRequestException(String.format("Дата не должна быть ранее %s", DATE_CONST));
        }
        Film updatedFilm = getItem(filmDtoForRead.getId())
                .map(film -> {
                    items.remove(film);
                    return mapper.filmDtoForReadToFilm(filmDtoForRead);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Нет такого фильма"));
        items.add(updatedFilm);
        log.info("Обновлен фильм: {}", filmDtoForRead.getName());
        return filmDtoForRead;
    }
}
